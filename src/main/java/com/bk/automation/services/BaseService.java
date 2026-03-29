package com.bk.automation.services;

import com.bk.automation.core.config.ConfigReader;
import com.bk.automation.core.driver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Base class for all Service Layer classes.
 * Provides common functionality, driver access, and configuration.
 *
 * <p>The Service Layer acts as a bridge between Step Definitions and Page Objects,
 * encapsulating business logic and multi-page workflows.
 *
 * <p>Flow: Feature → Step Definition → Service → Page → Driver
 */
public abstract class BaseService {

    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected final ConfigReader config;

    protected BaseService() {
        this.config = ConfigReader.getInstance();
    }

    /**
     * Gets the current WebDriver instance.
     */
    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    /**
     * Navigates to the application base URL.
     */
    protected void navigateToBaseUrl() {
        String baseUrl = config.getBaseUrl();
        logger.info("Navigating to base URL: {}", baseUrl);
        getDriver().get(baseUrl);
    }
}
