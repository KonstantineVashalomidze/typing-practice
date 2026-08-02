package com.github.konstantinevashalomidze.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private final Properties properties;
    private final Logger logger = LoggerFactory.getLogger(Config.class);

    public Config() {
        properties = new Properties();

        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.info("Sorry, unable to find application.properties");
                return;
            }

            properties.load(input);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public String genaiApiKey() {
        return properties.getProperty("genai.api.key");
    }

}
