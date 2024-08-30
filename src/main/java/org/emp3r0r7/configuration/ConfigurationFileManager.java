package org.emp3r0r7.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.emp3r0r7.configuration.ConfigProperties.APPLICATION_NAME;

@Component
public class ConfigurationFileManager {

    private static final String applicationName = ConfigReader.getApplicationName();

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationFileManager.class);

    public static final String CONFIG_DIR = System.getProperty("user.home") + File.separator + "." + applicationName.toLowerCase();

    private static final String CONFIG_FILE = CONFIG_DIR + File.separator + "config.properties";

    private void createConfigurationFile() {
        try {

            Path configDirPath = Paths.get(CONFIG_DIR);
            if (!Files.exists(configDirPath)) {
                Files.createDirectories(configDirPath);
                logger.info("Created configuration directory at {}", CONFIG_DIR);
            }

            File configFile = new File(CONFIG_FILE);
            if (!configFile.exists()) {

                if(configFile.createNewFile())
                    logger.info("Created configuration file at {}", CONFIG_FILE);

                // default props
                try (FileWriter writer = new FileWriter(configFile)) {
                    Properties properties = new Properties();
                    properties.setProperty(APPLICATION_NAME.getProp(), applicationName);
                    properties.store(writer, "Configuration for " + applicationName);
                    logger.info("Configuration file populated with default settings.");
                }
            } else {
                logger.info("Configuration file already exists at {}", CONFIG_FILE);
            }
        } catch (IOException e) {
            logger.error("Failed to create configuration file", e);
        }
    }

    public Properties loadConfiguration() {
        Properties properties = new Properties();
        try {
            Path configFilePath = Paths.get(CONFIG_FILE);
            if (Files.exists(configFilePath)) {
                properties.load(Files.newInputStream(configFilePath));
                logger.info("Configuration loaded from {}", CONFIG_FILE);
            } else {
                logger.warn("Configuration file does not exist, creating a new one with default settings.");
                createConfigurationFile();
                properties.load(Files.newInputStream(configFilePath));
            }
        } catch (IOException e) {
            logger.error("Failed to load configuration file", e);
        }
        return properties;
    }
}
