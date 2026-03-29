package com.bk.automation.pages.login;

import com.bk.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object representing the Login page of the Healthcare Facility Management application.
 * Contains all element locators and low-level page interactions.
 *
 * <p>Naming Convention: All locators use meaningful names prefixed by element type.
 * <p>Locator Strategy: XPath-based locators for maximum flexibility.
 */
public class LoginPage extends BasePage {

    // ======================== Locators (XPath) ========================

    // --- Input Fields ---
    private static final By INPUT_USERNAME = By.xpath("//input[@id='username' or @name='username' or @placeholder='Username']");
    private static final By INPUT_PASSWORD = By.xpath("//input[@id='password' or @name='password' or @type='password']");

    // --- Buttons ---
    private static final By BTN_LOGIN = By.xpath("//button[@type='submit' or contains(text(),'Login') or contains(text(),'Sign In')]");
    private static final By BTN_FORGOT_PASSWORD = By.xpath("//a[contains(text(),'Forgot') or contains(@href,'forgot')]");

    // --- Messages ---
    private static final By MSG_ERROR = By.xpath("//div[contains(@class,'error') or contains(@class,'alert-danger') or contains(@role,'alert')]");
    private static final By MSG_VALIDATION = By.xpath("//span[contains(@class,'validation') or contains(@class,'error-message')]");

    // --- Logo / Branding ---
    private static final By IMG_LOGO = By.xpath("//img[contains(@class,'logo') or contains(@alt,'logo')]");

    // --- Remember Me ---
    private static final By CHK_REMEMBER_ME = By.xpath("//input[@type='checkbox' and (contains(@id,'remember') or contains(@name,'remember'))]");

    // ======================== PageFactory Elements ========================

    @FindBy(xpath = "//input[@id='username' or @name='username' or @placeholder='Username']")
    private WebElement usernameField;

    @FindBy(xpath = "//input[@id='password' or @name='password' or @type='password']")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit' or contains(text(),'Login') or contains(text(),'Sign In')]")
    private WebElement loginButton;

    // ======================== Page Actions ========================

    /**
     * Enters the username into the username field.
     *
     * @param username the username to enter
     */
    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        typeText(INPUT_USERNAME, username);
    }

    /**
     * Enters the password into the password field.
     *
     * @param password the password to enter
     */
    public void enterPassword(String password) {
        logger.info("Entering password: ****");
        typeText(INPUT_PASSWORD, password);
    }

    /**
     * Clicks the Login button.
     */
    public void clickLoginButton() {
        logger.info("Clicking Login button");
        clickElement(BTN_LOGIN);
    }

    /**
     * Clicks the Forgot Password link.
     */
    public void clickForgotPassword() {
        logger.info("Clicking Forgot Password link");
        clickElement(BTN_FORGOT_PASSWORD);
    }

    /**
     * Checks the "Remember Me" checkbox if present and not already checked.
     */
    public void checkRememberMe() {
        if (isElementDisplayed(CHK_REMEMBER_ME) && !driver.findElement(CHK_REMEMBER_ME).isSelected()) {
            clickElement(CHK_REMEMBER_ME);
            logger.info("Checked 'Remember Me' checkbox");
        }
    }

    /**
     * Gets the error message displayed on the login page.
     *
     * @return error message text, or empty string if not displayed
     */
    public String getErrorMessage() {
        if (isElementDisplayed(MSG_ERROR)) {
            String errorText = getElementText(MSG_ERROR);
            logger.info("Error message displayed: {}", errorText);
            return errorText;
        }
        return "";
    }

    /**
     * Gets the validation message for input fields.
     *
     * @return validation message text
     */
    public String getValidationMessage() {
        if (isElementDisplayed(MSG_VALIDATION)) {
            return getElementText(MSG_VALIDATION);
        }
        return "";
    }

    /**
     * Checks if the login page logo is displayed.
     */
    public boolean isLogoDisplayed() {
        return isElementDisplayed(IMG_LOGO);
    }

    /**
     * Checks if the username field is displayed.
     */
    public boolean isUsernameFieldDisplayed() {
        return isElementDisplayed(INPUT_USERNAME);
    }

    /**
     * Checks if the password field is displayed.
     */
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(INPUT_PASSWORD);
    }

    /**
     * Checks if the login button is enabled.
     */
    public boolean isLoginButtonEnabled() {
        return isElementEnabled(BTN_LOGIN);
    }

    /**
     * Checks if the error message is displayed.
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(MSG_ERROR);
    }

    @Override
    public boolean isPageLoaded() {
        logger.info("Verifying Login page is loaded");
        return isUsernameFieldDisplayed() && isPasswordFieldDisplayed();
    }
}
