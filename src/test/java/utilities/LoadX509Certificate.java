package utilities;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class LoadX509Certificate {

    // Load the X.509 Certificate
    public X509Certificate loadCertificate(String filePath) throws Exception {
        byte[] certBytes = Files.readAllBytes(Paths.get(filePath));
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        try (InputStream certStream = new ByteArrayInputStream(certBytes)) {
            return (X509Certificate) factory.generateCertificate(certStream);
        }
    }

}
