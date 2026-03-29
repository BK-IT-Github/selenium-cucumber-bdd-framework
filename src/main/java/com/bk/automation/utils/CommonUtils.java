package com.bk.automation.utils;

import com.bk.automation.core.driver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Common utility methods for frequently used Selenium operations.
 * Provides a clean, reusable API for element interactions.
 */
public final class CommonUtils {

    private static final Logger logger = LogManager.getLogger(CommonUtils.class);

    private CommonUtils() {
        // Utility class
    }

    // ======================== Navigation ========================

    /**
     * Navigates to the specified URL.
     */
    public static void navigateTo(String url) {
        logger.info("Navigating to URL: {}", url);
        DriverFactory.getDriver().get(url);
        WaitUtil.waitForPageLoad();
    }

    /**
     * Refreshes the current page.
     */
    public static void refreshPage() {
        logger.info("Refreshing current page");
        DriverFactory.getDriver().navigate().refresh();
        WaitUtil.waitForPageLoad();
    }

    /**
     * Navigates back in browser history.
     */
    public static void navigateBack() {
        logger.info("Navigating back");
        DriverFactory.getDriver().navigate().back();
    }

    /**
     * Returns the current page title.
     */
    public static String getPageTitle() {
        return DriverFactory.getDriver().getTitle();
    }

    /**
     * Returns the current URL.
     */
    public static String getCurrentUrl() {
        return DriverFactory.getDriver().getCurrentUrl();
    }

    // ======================== Element Interactions ========================

    /**
     * Clicks an element after waiting for it to be clickable.
     */
    public static void click(By locator) {
        WebElement element = WaitUtil.waitForClickable(locator);
        logger.debug("Clicking element: {}", locator);
        element.click();
    }

    /**
     * Clicks using JavaScript (useful for hidden or overlapped elements).
     */
    public static void jsClick(By locator) {
        WebElement element = WaitUtil.waitForPresence(locator);
        logger.debug("JS clicking element: {}", locator);
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Types text into an element after clearing it.
     */
    public static void type(By locator, String text) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        logger.debug("Typing '{}' into element: {}", text, locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Clears the text field.
     */
    public static void clear(By locator) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        element.clear();
    }

    /**
     * Gets text from an element.
     */
    public static String getText(By locator) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        return element.getText().trim();
    }

    /**
     * Gets the value of a specific attribute.
     */
    public static String getAttribute(By locator, String attribute) {
        WebElement element = WaitUtil.waitForPresence(locator);
        return element.getAttribute(attribute);
    }

    /**
     * Checks if an element is displayed on the page.
     */
    public static boolean isDisplayed(By locator) {
        try {
            return DriverFactory.getDriver().findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Checks if an element is enabled.
     */
    public static boolean isEnabled(By locator) {
        try {
            return DriverFactory.getDriver().findElement(locator).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks if a checkbox or radio button is selected.
     */
    public static boolean isSelected(By locator) {
        try {
            return DriverFactory.getDriver().findElement(locator).isSelected();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // ======================== Dropdown Handling ========================

    /**
     * Selects a dropdown option by visible text.
     */
    public static void selectByVisibleText(By locator, String text) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByVisibleText(text);
        logger.debug("Selected dropdown option by visible text: '{}'", text);
    }

    /**
     * Selects a dropdown option by value attribute.
     */
    public static void selectByValue(By locator, String value) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByValue(value);
        logger.debug("Selected dropdown option by value: '{}'", value);
    }

    /**
     * Selects a dropdown option by index.
     */
    public static void selectByIndex(By locator, int index) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByIndex(index);
        logger.debug("Selected dropdown option by index: {}", index);
    }

    /**
     * Gets the currently selected dropdown text.
     */
    public static String getSelectedText(By locator) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText();
    }

    // ======================== Actions ========================

    /**
     * Hovers over an element.
     */
    public static void hoverOver(By locator) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        Actions actions = new Actions(DriverFactory.getDriver());
        actions.moveToElement(element).perform();
        logger.debug("Hovered over element: {}", locator);
    }

    /**
     * Double-clicks on an element.
     */
    public static void doubleClick(By locator) {
        WebElement element = WaitUtil.waitForClickable(locator);
        Actions actions = new Actions(DriverFactory.getDriver());
        actions.doubleClick(element).perform();
        logger.debug("Double-clicked element: {}", locator);
    }

    /**
     * Right-clicks (context click) on an element.
     */
    public static void rightClick(By locator) {
        WebElement element = WaitUtil.waitForClickable(locator);
        Actions actions = new Actions(DriverFactory.getDriver());
        actions.contextClick(element).perform();
        logger.debug("Right-clicked element: {}", locator);
    }

    /**
     * Drags an element and drops it on the target.
     */
    public static void dragAndDrop(By source, By target) {
        WebElement srcElement = WaitUtil.waitForVisibility(source);
        WebElement tgtElement = WaitUtil.waitForVisibility(target);
        Actions actions = new Actions(DriverFactory.getDriver());
        actions.dragAndDrop(srcElement, tgtElement).perform();
        logger.debug("Drag and drop from {} to {}", source, target);
    }

    // ======================== Window / Tab Handling ========================

    /**
     * Switches to a new browser window or tab.
     */
    public static void switchToNewWindow() {
        WebDriver driver = DriverFactory.getDriver();
        String currentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(currentWindow)) {
                driver.switchTo().window(window);
                logger.info("Switched to new window: {}", window);
                break;
            }
        }
    }

    /**
     * Switches to the parent/original window.
     */
    public static void switchToParentWindow() {
        WebDriver driver = DriverFactory.getDriver();
        List<String> windows = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windows.get(0));
        logger.info("Switched to parent window");
    }

    /**
     * Closes the current window and switches to the parent.
     */
    public static void closeCurrentWindowAndSwitch() {
        WebDriver driver = DriverFactory.getDriver();
        driver.close();
        switchToParentWindow();
    }

    // ======================== iFrame Handling ========================

    /**
     * Switches to an iFrame by locator.
     */
    public static void switchToFrame(By locator) {
        WebElement frame = WaitUtil.waitForVisibility(locator);
        DriverFactory.getDriver().switchTo().frame(frame);
        logger.debug("Switched to frame: {}", locator);
    }

    /**
     * Switches back to the default content.
     */
    public static void switchToDefaultContent() {
        DriverFactory.getDriver().switchTo().defaultContent();
        logger.debug("Switched to default content");
    }

    // ======================== Alert Handling ========================

    /**
     * Accepts the currently displayed alert.
     */
    public static void acceptAlert() {
        Alert alert = WaitUtil.waitForAlert();
        logger.info("Accepting alert: {}", alert.getText());
        alert.accept();
    }

    /**
     * Dismisses the currently displayed alert.
     */
    public static void dismissAlert() {
        Alert alert = WaitUtil.waitForAlert();
        logger.info("Dismissing alert: {}", alert.getText());
        alert.dismiss();
    }

    /**
     * Gets the text of the currently displayed alert.
     */
    public static String getAlertText() {
        Alert alert = WaitUtil.waitForAlert();
        return alert.getText();
    }

    /**
     * Types text into the prompt alert and accepts it.
     */
    public static void typeInAlert(String text) {
        Alert alert = WaitUtil.waitForAlert();
        alert.sendKeys(text);
        alert.accept();
        logger.debug("Typed '{}' into alert and accepted", text);
    }

    // ======================== JavaScript ========================

    /**
     * Executes arbitrary JavaScript.
     */
    public static Object executeScript(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
        return js.executeScript(script, args);
    }

    /**
     * Scrolls to an element.
     */
    public static void scrollToElement(By locator) {
        WebElement element = WaitUtil.waitForPresence(locator);
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        logger.debug("Scrolled to element: {}", locator);
    }

    /**
     * Scrolls to the bottom of the page.
     */
    public static void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Scrolls to the top of the page.
     */
    public static void scrollToTop() {
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
        js.executeScript("window.scrollTo(0, 0);");
    }

    /**
     * Highlights an element (useful for debugging/demo).
     */
    public static void highlightElement(By locator) {
        WebElement element = DriverFactory.getDriver().findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }
}
