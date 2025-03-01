package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.LoadX509Certificate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

public class X509CertificateTest {


    private static final String CERT_PATH = "cert.pem";
    private static final String KEY_PATH = "key.pem";

    @Test(priority = 1)
    public void testGenerateCertificateUsingOpenSSL() throws Exception {
        System.out.println("Generating X.509 Certificate using OpenSSL...");

        // OpenSSL command
        String opensslCommand = "openssl req -x509 -newkey rsa:2048 -keyout key.pem -out cert.pem -days 365 -nodes "
                + "-subj \"/C=US/ST=California/L=San Francisco/O=MyCompany/OU=QA/CN=mydomain.com\"";

        Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", opensslCommand});

        // Capture errors (if any)
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = errorReader.readLine()) != null) {
            System.err.println("ERROR: " + line);
        }

        int exitCode = process.waitFor();
        Assert.assertEquals(exitCode, 0, "OpenSSL command execution failed!");

        // Verify if files are generated
        Assert.assertTrue(Files.exists(Paths.get(CERT_PATH)), "Certificate file not generated!");
        Assert.assertTrue(Files.exists(Paths.get(KEY_PATH)), "Key file not generated!");

        System.out.println("OpenSSL Certificate and Key generated successfully!");
    }

    @Test(priority = 2, dependsOnMethods = "testGenerateCertificateUsingOpenSSL")
    public void testValidateGeneratedCertificate() throws Exception {
        System.out.println("Validating Generated Certificate...");

        // Load the generated certificate
        LoadX509Certificate loadX509Certificate = new LoadX509Certificate();
        X509Certificate cert = loadX509Certificate.loadCertificate(CERT_PATH);
        System.out.println(cert);
        Assert.assertNotNull(cert, "Failed to load the generated certificate!");

        // Validate Common Name (CN)
        String subject = cert.getSubjectX500Principal().getName();
        System.out.println(subject);
        Assert.assertTrue(subject.contains("CN=mydomain.com"), "CN does not match!");

        // Validate Issuer
        String issuer = cert.getIssuerX500Principal().getName();
        System.out.println(issuer);
        Assert.assertTrue(issuer.contains("O=MyCompany"), "Issuer does not match!");

        // Validate Expiration Date (not expired)
        try {
            cert.checkValidity(); // Throws an exception if expired
        } catch (CertificateExpiredException e) {
            Assert.fail("Certificate has expired!");
        } catch (CertificateNotYetValidException e) {
            Assert.fail("Certificate is not yet valid!");
        }

        // Validate Signature Algorithm (should be RSA 2048 / SHA-256)
        String sigAlgo = cert.getSigAlgName();
        System.out.println(sigAlgo);
        Assert.assertTrue(sigAlgo.contains("SHA256withRSA"), "Weak Signature Algorithm detected!");

        System.out.println("Certificate validation passed successfully!");
    }


}




