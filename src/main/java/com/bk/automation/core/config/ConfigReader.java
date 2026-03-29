package com.bk.automation.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Singleton ConfigReader that loads and manages configuration properties.
 * Supports environment-specific overrides and system property / env variable fallbacks.
 *
 * <p>Loading priority (highest to lowest):
 * <ol>
 *   <li>System properties (-Dproperty=value)</li>
 *   <li>Environment variables</li>
 *   <li>Environment-specific config file (config-{env}.properties)</li>
 *   <li>Default config file (config.properties)</li>
 * </ol>
 */
public final class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final String DEFAULT_CONFIG_PATH = "src/main/resources/config/config.properties";
    private static final String ENV_CONFIG_PATH_TEMPLATE = "src/main/resources/config/config-%s.properties";

    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        properties = new Properties();
        loadDefaultConfig();
        loadEnvironmentConfig();
        logger.info("Configuration loaded successfully for environment: {}", getEnvironment());
    }

    /**
     * Returns the singleton instance of ConfigReader.
     * Thread-safe via synchronized block.
     */
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    /**
     * Reloads the configuration (useful for test setup).
     */
    public static synchronized void reload() {
        instance = null;
        getInstance();
    }

    // ======================== Property Accessors ========================

    public String getProperty(String key) {
        // Priority: System property > Env variable > Config file
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.isEmpty()) {
            return systemProp;
        }

        String envVar = System.getenv(key.replace(".", "_").toUpperCase());
        if (envVar != null && !envVar.isEmpty()) {
            return envVar;
        }

        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return (value != null) ? Integer.parseInt(value.trim()) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key '{}': '{}'. Using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return (value != null) ? Boolean.parseBoolean(value.trim()) : defaultValue;
    }

    // ======================== Convenience Methods ========================

    public String getBaseUrl() {
        return getProperty("app.url");
    }

    public String getBrowser() {
        return getProperty("browser", "chrome");
    }

    public boolean isHeadless() {
        return getBooleanProperty("browser.headless", false);
    }

    public boolean isMaximized() {
        return getBooleanProperty("browser.maximize", true);
    }

    public int getImplicitWait() {
        return getIntProperty("timeout.implicit", 10);
    }

    public int getExplicitWait() {
        return getIntProperty("timeout.explicit", 20);
    }

    public int getPageLoadTimeout() {
        return getIntProperty("timeout.pageload", 30);
    }

    public String getEnvironment() {
        return getProperty("environment", "qa");
    }

    public String getTestUsername() {
        return getProperty("test.username");
    }

    public String getTestPassword() {
        return getProperty("test.password");
    }

    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure", true);
    }

    public String getScreenshotPath() {
        return getProperty("screenshot.path", "target/screenshots/");
    }

    public String getReportPath() {
        return getProperty("report.path", "target/reports/");
    }

    public int getRetryCount() {
        return getIntProperty("retry.count", 1);
    }

    // ======================== Private Loaders ========================

    private void loadDefaultConfig() {
        try (FileInputStream fis = new FileInputStream(DEFAULT_CONFIG_PATH)) {
            properties.load(fis);
            logger.debug("Loaded default configuration from: {}", DEFAULT_CONFIG_PATH);
        } catch (IOException e) {
            logger.error("Failed to load default config file: {}", DEFAULT_CONFIG_PATH, e);
            throw new RuntimeException("Cannot load default configuration file: " + DEFAULT_CONFIG_PATH, e);
        }
    }

    private void loadEnvironmentConfig() {
        String env = System.getProperty("environment",
                properties.getProperty("environment", "qa"));
        String envConfigPath = String.format(ENV_CONFIG_PATH_TEMPLATE, env.toLowerCase());

        try (FileInputStream fis = new FileInputStream(envConfigPath)) {
            Properties envProperties = new Properties();
            envProperties.load(fis);
            // Environment-specific properties override defaults
            properties.putAll(envProperties);
            logger.debug("Loaded environment configuration from: {}", envConfigPath);
        } catch (IOException e) {
            logger.warn("Environment config file not found: {}. Using defaults.", envConfigPath);
        }
    }
}
