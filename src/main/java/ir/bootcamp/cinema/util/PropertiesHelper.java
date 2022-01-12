package ir.bootcamp.cinema.util;

import ir.bootcamp.cinema.App;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {
    public static Properties loadPropertiesFile(String path) throws IOException {
        InputStream iStream = null;
        Properties properties = new Properties();
        try {
            iStream = App.class.getClassLoader().getResourceAsStream(path);
            properties.load(iStream);
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (iStream != null) {
                    iStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }
}
