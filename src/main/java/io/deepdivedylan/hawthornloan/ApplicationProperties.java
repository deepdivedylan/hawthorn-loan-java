package io.deepdivedylan.hawthornloan;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
    public static String getHMACKey() {
        try {
            InputStream input = ApplicationProperties.class.getClassLoader().getResourceAsStream("hawthornloan.properties");
            Properties properties = new Properties();
            if (input == null) {
                throw (new IOException("404 Properties File Not Found"));
            }
            properties.load(input);
            return properties.getProperty("hawthornloan.hmac_key");
        } catch(IOException ioException) {
            System.err.println("Unable to load HMAC key: " + ioException.getMessage());
            System.exit(1);
            return "thisIsNotTheHMACKey1701";
        }
    }
}
