package com.bk.automation.utils;

import com.bk.automation.core.config.ConfigReader;
import com.bk.automation.core.driver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Utility class providing explicit, fluent, and custom wait strategies.
 * Eliminates the need for Thread.sleep() across the framework.
 */
public final class WaitUtil {

    private static final Logger logger = LogManager.getLogger(WaitUtil.class);
    private static final ConfigReader config = ConfigReader.getInstance();

    private WaitUtil() {
        // Utility class
    }

    // ======================== Explicit Waits ========================

    /**
     * Waits until the element is visible on the page.
     */
    public static WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, config.getExplicitWait());
    }

    public static WebElement waitForVisibility(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for visibility of element: {} (timeout: {}s)", locator, timeoutInSeconds);
        return getWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(WebElement element) {
        return waitForVisibility(element, config.getExplicitWait());
    }

    public static WebElement waitForVisibility(WebElement element, int timeoutInSeconds) {
        logger.debug("Waiting for visibility of WebElement (timeout: {}s)", timeoutInSeconds);
        return getWait(timeoutInSeconds).until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits until the element is clickable.
     */
    public static WebElement waitForClickable(By locator) {
        return waitForClickable(locator, config.getExplicitWait());
    }

    public static WebElement waitForClickable(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for element to be clickable: {} (timeout: {}s)", locator, timeoutInSeconds);
        return getWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(WebElement element) {
        return waitForClickable(element, config.getExplicitWait());
    }

    public static WebElement waitForClickable(WebElement element, int timeoutInSeconds) {
        logger.debug("Waiting for WebElement to be clickable (timeout: {}s)", timeoutInSeconds);
        return getWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until the element is present in the DOM.
     */
    public static WebElement waitForPresence(By locator) {
        return waitForPresence(locator, config.getExplicitWait());
    }

    public static WebElement waitForPresence(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for presence of element: {} (timeout: {}s)", locator, timeoutInSeconds);
        return getWait(timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits until all elements matching the locator are visible.
     */
    public static List<WebElement> waitForAllVisible(By locator) {
        return waitForAllVisible(locator, config.getExplicitWait());
    }

    public static List<WebElement> waitForAllVisible(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for all elements to be visible: {} (timeout: {}s)", locator, timeoutInSeconds);
        return getWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Waits until the element becomes invisible / disappears.
     */
    public static boolean waitForInvisibility(By locator) {
        return waitForInvisibility(locator, config.getExplicitWait());
    }

    public static boolean waitForInvisibility(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for invisibility of element: {} (timeout: {}s)", locator, timeoutInSeconds);
        return getWait(timeoutInSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for the element's text to match.
     */
    public static boolean waitForTextToBe(By locator, String text) {
        return waitForTextToBe(locator, text, config.getExplicitWait());
    }

    public static boolean waitForTextToBe(By locator, String text, int timeoutInSeconds) {
        logger.debug("Waiting for text '{}' in element: {} (timeout: {}s)", text, locator, timeoutInSeconds);
        return getWait(timeoutInSeconds).until(ExpectedConditions.textToBe(locator, text));
    }

    /**
     * Waits for an alert to be present.
     */
    public static Alert waitForAlert() {
        return waitForAlert(config.getExplicitWait());
    }

    public static Alert waitForAlert(int timeoutInSeconds) {
        logger.debug("Waiting for alert to be present (timeout: {}s)", timeoutInSeconds);
        return getWait(timeoutInSeconds).until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Waits for the page title to contain a specific text.
     */
    public static boolean waitForTitleContains(String titlePart) {
        return waitForTitleContains(titlePart, config.getExplicitWait());
    }

    public static boolean waitForTitleContains(String titlePart, int timeoutInSeconds) {
        logger.debug("Waiting for title to contain '{}' (timeout: {}s)", titlePart, timeoutInSeconds);
        return getWait(timeoutInSeconds).until(ExpectedConditions.titleContains(titlePart));
    }

    /**
     * Waits for a frame to be available and switches to it.
     */
    public static WebDriver waitForFrameAndSwitch(By locator) {
        return waitForFrameAndSwitch(locator, config.getExplicitWait());
    }

    public static WebDriver waitForFrameAndSwitch(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for frame and switching: {} (timeout: {}s)", locator, timeoutInSeconds);
        return getWait(timeoutInSeconds).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    // ======================== Fluent Wait ========================

    /**
     * Fluent wait with custom polling interval.
     */
    public static <T> T fluentWait(Function<WebDriver, T> condition, int timeoutInSeconds, int pollingInMillis) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(DriverFactory.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingInMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class);

        return fluentWait.until(condition);
    }

    /**
     * Waits for a custom JavaScript condition to evaluate to true.
     */
    public static boolean waitForPageLoad() {
        return waitForPageLoad(config.getPageLoadTimeout());
    }

    public static boolean waitForPageLoad(int timeoutInSeconds) {
        logger.debug("Waiting for page to fully load (timeout: {}s)", timeoutInSeconds);
        return getWait(timeoutInSeconds).until(driver ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState")
                        .equals("complete"));
    }

    /**
     * Waits for jQuery/AJAX calls to complete.
     */
    public static boolean waitForAjaxComplete() {
        return waitForAjaxComplete(config.getExplicitWait());
    }

    public static boolean waitForAjaxComplete(int timeoutInSeconds) {
        logger.debug("Waiting for AJAX calls to complete (timeout: {}s)", timeoutInSeconds);
        return getWait(timeoutInSeconds).until(driver -> {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return (Boolean) js.executeScript(
                    "return (typeof jQuery === 'undefined') || (jQuery.active === 0)");
        });
    }

    // ======================== Private Helpers ========================

    private static WebDriverWait getWait(int timeoutInSeconds) {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }
}
