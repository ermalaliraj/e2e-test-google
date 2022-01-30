package com.ea.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Configuration {

    public static final String SCREENSHOT_PATH = "test-output/Screenshots/" ;
    public static final String DRIVER_PATH = "src/resources/driver/chromedriver.exe"; // v97
    private static final String CONFIG_FILE_PATH = "config/" + TestContext.getInstance().getEnvironment() + ".properties";
    private static final String USERS_FILE_PATH = "config/users.properties";
    private final Properties properties;

    public Configuration() {
        File configUserFile = new File(this.getClass().getClassLoader().getResource(USERS_FILE_PATH).getFile());
        File environmentConfigFile = new File(this.getClass().getClassLoader().getResource(CONFIG_FILE_PATH).getFile());
        try {
            properties = new Properties();
            properties.load(new FileReader(configUserFile));
            properties.load(new FileReader(environmentConfigFile));
        } catch (IOException e) {
            throw new RuntimeException("The config file format is not as expected", e);
        }
    }

    public String getProperty(String value) {
        return properties.getProperty(value);
    }

    public String getProperty(String value, String defaultValue) {
        return properties.getProperty(value, defaultValue);
    }

}