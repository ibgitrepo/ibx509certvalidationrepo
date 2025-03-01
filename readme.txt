Running the X.509 Certificate Test Cases


Prerequisites

Before running the tests, ensure you have the following installed:
✔ Java 11+
✔ TestNG (for running test cases)
✔ OpenSSL (for generating test certificates)
✔ Maven/Gradle (for dependency management)

Step 1: Clone the Repository:

git clone https://github.com/your-repo/ibx509certvalidationrepo.git


Step 2: Generate a Self-Signed Certificate (Optional):

To generate a self-signed certificate, run the following command:
openssl req -x509 -newkey rsa:2048 -keyout key.pem -out cert.pem -days 365 -nodes \
-subj "/C=US/ST=California/L=San Francisco/O=MyCompany/OU=QA/CN=mydomain.com"
This will create:
	•	cert.pem → The self-signed X.509 certificate
	•	key.pem → The private key


Step 3: Run Test Cases:

➡ Using Maven
mvn test


Step 4: View Test Results:
	•	Console Output: Shows pass/fail status of each test.
	•	TestNG Report: Open target/surefire-reports/index.html in a browser.


Troubleshooting:

	•	Test failures? Ensure the correct Java version & dependencies are installed.
	•	OpenSSL not found? Install it using:
	◦	Linux/Mac: brew install openssl or sudo apt install openssl
	◦	Windows: Use Win32 OpenSSL


