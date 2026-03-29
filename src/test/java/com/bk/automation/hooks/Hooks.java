package com.bk.automation.hooks;

import com.bk.automation.core.config.ConfigReader;
import com.bk.automation.core.driver.DriverFactory;
import com.bk.automation.utils.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cucumber Hooks for managing test lifecycle.
 * Handles WebDriver initialization, screenshot capture, and cleanup.
 *
 * <p>Execution Order:
 * <ul>
 *   <li>@Before - Initialize driver before each scenario</li>
 *   <li>@AfterStep - Capture screenshot after each step (on failure)</li>
 *   <li>@After - Cleanup and capture final screenshot after each scenario</li>
 * </ul>
 */
public class Hooks {

    private static final Logger logger = LogManager.getLogger(Hooks.class);
    private static final ConfigReader config = ConfigReader.getInstance();

    // ======================== Before Hooks ========================

    /**
     * Runs before EVERY scenario. Initializes the WebDriver.
     * Order = 0 ensures this runs first among Before hooks.
     */
    @Before(order = 0)
    public void setUp(Scenario scenario) {
        logger.info("========================================================");
        logger.info("STARTING SCENARIO: {}", scenario.getName());
        logger.info("Tags: {}", scenario.getSourceTagNames());
        logger.info("========================================================");

        // Initialize WebDriver
        DriverFactory.initDriver();
    }

    /**
     * Runs before scenarios tagged with @Login.
     * Navigates to the application base URL.
     */
    @Before(value = "@Login", order = 1)
    public void setUpLoginModule(Scenario scenario) {
        logger.info("Setting up Login module for scenario: {}", scenario.getName());
    }

    /**
     * Runs before scenarios tagged with @Dashboard.
     * Any module-specific setup goes here.
     */
    @Before(value = "@Dashboard", order = 1)
    public void setUpDashboardModule(Scenario scenario) {
        logger.info("Setting up Dashboard module for scenario: {}", scenario.getName());
    }

    // ======================== After Step Hook ========================

    /**
     * Runs after each step. Captures screenshot on step failure for debugging.
     */
    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed() && DriverFactory.isDriverInitialized()) {
            logger.warn("Step FAILED in scenario: {}", scenario.getName());
            attachScreenshot(scenario, "Step_Failure");
        }
    }

    // ======================== After Hooks ========================

    /**
     * Runs after EVERY scenario. Handles screenshot capture and driver cleanup.
     * Order = 0 ensures this runs last among After hooks (lowest order runs last).
     */
    @After(order = 0)
    public void tearDown(Scenario scenario) {
        try {
            if (DriverFactory.isDriverInitialized()) {
                // Capture screenshot based on scenario outcome
                if (scenario.isFailed()) {
                    logger.error("SCENARIO FAILED: {}", scenario.getName());
                    attachScreenshot(scenario, "Failure");
                    // Also save screenshot to file for external report reference
                    ScreenshotUtil.captureScreenshot(scenario.getName());
                } else if (config.getBooleanProperty("screenshot.on.pass", false)) {
                    logger.info("SCENARIO PASSED: {}", scenario.getName());
                    attachScreenshot(scenario, "Pass");
                }
            }
        } catch (Exception e) {
            logger.error("Error during teardown for scenario: {}", scenario.getName(), e);
        } finally {
            // Always quit driver
            DriverFactory.quitDriver();
            logger.info("========================================================");
            logger.info("COMPLETED SCENARIO: {} | Status: {}", scenario.getName(), scenario.getStatus());
            logger.info("========================================================\n");
        }
    }

    /**
     * Runs after scenarios tagged with @Smoke (higher order runs first).
     */
    @After(value = "@Smoke", order = 1)
    public void afterSmokeScenario(Scenario scenario) {
        logger.info("Completed Smoke scenario: {} | Status: {}", scenario.getName(), scenario.getStatus());
    }

    // ======================== Private Helpers ========================

    /**
     * Attaches a screenshot to the Cucumber report.
     *
     * @param scenario the current scenario
     * @param label    label for the screenshot (e.g., "Failure", "Pass")
     */
    private void attachScreenshot(Scenario scenario, String label) {
        try {
            byte[] screenshot = ScreenshotUtil.captureScreenshotAsBytes();
            if (screenshot != null && screenshot.length > 0) {
                scenario.attach(screenshot, "image/png",
                        label + "_" + scenario.getName());
                logger.debug("Screenshot attached to report: {}_{}", label, scenario.getName());
            }
        } catch (Exception e) {
            logger.error("Failed to attach screenshot for scenario: {}", scenario.getName(), e);
        }
    }
}
