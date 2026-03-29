package com.bk.automation.utils;

import com.bk.automation.core.config.ConfigReader;
import com.bk.automation.core.driver.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * Utility class for capturing and managing screenshots.
 * Supports file-based and Base64 encoded screenshot capture.
 */
public final class ScreenshotUtil {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);
    private static final ConfigReader config = ConfigReader.getInstance();

    private ScreenshotUtil() {
        // Utility class
    }

    /**
     * Captures a screenshot and saves it to the configured screenshot directory.
     *
     * @param scenarioName the name of the scenario (used in filename)
     * @return the absolute path of the saved screenshot
     */
    public static String captureScreenshot(String scenarioName) {
        WebDriver driver = DriverFactory.getDriver();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String sanitizedName = sanitizeFileName(scenarioName);
        String fileName = sanitizedName + "_" + timestamp + ".png";
        String screenshotDir = config.getScreenshotPath();
        String filePath = screenshotDir + fileName;

        try {
            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destinationFile = new File(filePath);
            FileUtils.copyFile(sourceFile, destinationFile);
            logger.info("Screenshot captured: {}", destinationFile.getAbsolutePath());
            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Failed to capture screenshot for scenario: {}", scenarioName, e);
            return null;
        }
    }

    /**
     * Captures a screenshot as a Base64 encoded string.
     * Useful for embedding in HTML reports (Extent Reports / Cucumber).
     *
     * @return Base64 encoded screenshot string
     */
    public static String captureScreenshotAsBase64() {
        try {
            WebDriver driver = DriverFactory.getDriver();
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            logger.error("Failed to capture Base64 screenshot", e);
            return null;
        }
    }

    /**
     * Captures a screenshot as a byte array.
     * Useful for Cucumber scenario attachment.
     *
     * @return byte array of the screenshot
     */
    public static byte[] captureScreenshotAsBytes() {
        try {
            WebDriver driver = DriverFactory.getDriver();
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot as bytes", e);
            return new byte[0];
        }
    }

    /**
     * Sanitizes the file name by removing/replacing invalid characters.
     */
    private static String sanitizeFileName(String name) {
        if (name == null || name.isEmpty()) {
            return "screenshot";
        }
        return name.replaceAll("[^a-zA-Z0-9_\\-]", "_")
                .replaceAll("_{2,}", "_")
                .substring(0, Math.min(name.length(), 100));
    }
}
