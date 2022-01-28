package org.ea.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Configuration {

    public static final String SCREENSHOT = "target/results/Screenshots/Screenshot_" + getTimeStamp() + ".PNG";
    public static final String DRIVER_PATH = "src/resources/driver/chromedriver.exe"; // v97
    private static final String MAIN_CONFIG_FILE_PATH = "filters/selenium.properties";
    private static final String USERS_FILE_PATH = "filters/users.properties";
    private static final String ENVIRONMENT_CONFIG_FILE_PATH = "filters/env/" + TestNgParameters.getInstance().getEnvironment() + ".properties";
    private final Properties properties;

    public Configuration() {
        File configBaseFile = new File(this.getClass().getClassLoader().getResource(MAIN_CONFIG_FILE_PATH).getFile());
        File configUserFile = new File(this.getClass().getClassLoader().getResource(USERS_FILE_PATH).getFile());
        File environmentConfigFile = new File(this.getClass().getClassLoader().getResource(ENVIRONMENT_CONFIG_FILE_PATH).getFile());
        try {
            properties = new Properties();
            properties.load(new FileReader(configBaseFile));
            properties.load(new FileReader(configUserFile));
            properties.load(new FileReader(environmentConfigFile));
        } catch (IOException e) {
            throw new RuntimeException("The config file format is not as expected", e);
        }
    }

    public String getProperty(String value) {
        String propertyValue = getProperty(value, null);
        if (propertyValue != null) {
            return propertyValue;
        } else {
            throw new RuntimeException(value + " not specified in the selenium.properties file.");
        }
    }

    public String getProperty(String value, String defaultValue) {
        return properties.getProperty(value, defaultValue);
    }

    public static String getTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}