package com.bk.automation.pages;

import com.bk.automation.core.driver.DriverFactory;
import com.bk.automation.utils.CommonUtils;
import com.bk.automation.utils.WaitUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * Base class for all Page Objects in the framework.
 * Provides common functionality and WebDriver access.
 *
 * <p>All page classes MUST extend this class and initialize
 * their elements via {@link PageFactory} in the constructor.
 */
public abstract class BasePage {

    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected WebDriver driver;

    protected BasePage() {
        this.driver = DriverFactory.getDriver();
        PageFactory.initElements(driver, this);
    }

    // ======================== Common Page Actions ========================

    /**
     * Returns the page title.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Returns the current URL.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Waits for an element to be visible and then clicks it.
     */
    protected void clickElement(By locator) {
        WaitUtil.waitForClickable(locator).click();
        logger.debug("Clicked element: {}", locator);
    }

    /**
     * Waits for an element, clears it, and then types the specified text.
     */
    protected void typeText(By locator, String text) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
        logger.debug("Typed '{}' into element: {}", text, locator);
    }

    /**
     * Waits for an element to be visible and returns its text.
     */
    protected String getElementText(By locator) {
        return WaitUtil.waitForVisibility(locator).getText().trim();
    }

    /**
     * Checks whether an element is displayed.
     */
    protected boolean isElementDisplayed(By locator) {
        return CommonUtils.isDisplayed(locator);
    }

    /**
     * Checks whether an element is enabled.
     */
    protected boolean isElementEnabled(By locator) {
        return CommonUtils.isEnabled(locator);
    }

    /**
     * Scrolls to an element and clicks it.
     */
    protected void scrollAndClick(By locator) {
        CommonUtils.scrollToElement(locator);
        clickElement(locator);
    }

    /**
     * Waits for the page to fully load.
     */
    protected void waitForPageLoad() {
        WaitUtil.waitForPageLoad();
    }

    /**
     * Abstract method – each page must validate that it loaded correctly.
     *
     * @return true if the page loaded correctly
     */
    public abstract boolean isPageLoaded();
}
