package com.bk.automation.services.login;

import com.bk.automation.pages.login.LoginPage;
import com.bk.automation.services.BaseService;
import com.bk.automation.utils.WaitUtil;

/**
 * Service class encapsulating Login module business logic.
 * Orchestrates LoginPage interactions for complex login workflows.
 *
 * <p>Flow: LoginStepDefinitions → LoginService → LoginPage → Driver
 */
public class LoginService extends BaseService {

    private LoginPage loginPage;

    public LoginService() {
        super();
    }

    /**
     * Lazily initializes the LoginPage (created after driver is ready).
     */
    private LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        return loginPage;
    }

    /**
     * Navigates to the login page of the application.
     */
    public void navigateToLoginPage() {
        navigateToBaseUrl();
        WaitUtil.waitForPageLoad();
        logger.info("Navigated to Login page");
    }

    /**
     * Performs a complete login operation.
     *
     * @param username the username
     * @param password the password
     */
    public void performLogin(String username, String password) {
        logger.info("Performing login with username: {}", username);
        getLoginPage().enterUsername(username);
        getLoginPage().enterPassword(password);
        getLoginPage().clickLoginButton();
        WaitUtil.waitForPageLoad();
    }

    /**
     * Performs login with credentials from configuration.
     */
    public void performLoginWithDefaultCredentials() {
        String username = config.getTestUsername();
        String password = config.getTestPassword();
        performLogin(username, password);
    }

    /**
     * Performs login and verifies that the error message is displayed (negative test).
     *
     * @param username the invalid username
     * @param password the invalid password
     * @return the error message text
     */
    public String performInvalidLogin(String username, String password) {
        logger.info("Performing invalid login with username: {}", username);
        getLoginPage().enterUsername(username);
        getLoginPage().enterPassword(password);
        getLoginPage().clickLoginButton();
        return getLoginPage().getErrorMessage();
    }

    /**
     * Verifies that the login page is fully loaded.
     *
     * @return true if the login page elements are displayed
     */
    public boolean isLoginPageDisplayed() {
        return getLoginPage().isPageLoaded();
    }

    /**
     * Gets the error message from the login page.
     *
     * @return error message text
     */
    public String getLoginErrorMessage() {
        return getLoginPage().getErrorMessage();
    }

    /**
     * Checks if an error message is displayed on the login page.
     */
    public boolean isErrorMessageDisplayed() {
        return getLoginPage().isErrorMessageDisplayed();
    }

    /**
     * Checks if the username field is displayed.
     */
    public boolean isUsernameFieldDisplayed() {
        return getLoginPage().isUsernameFieldDisplayed();
    }

    /**
     * Checks if the password field is displayed.
     */
    public boolean isPasswordFieldDisplayed() {
        return getLoginPage().isPasswordFieldDisplayed();
    }

    /**
     * Checks if the login button is enabled.
     */
    public boolean isLoginButtonEnabled() {
        return getLoginPage().isLoginButtonEnabled();
    }

    /**
     * Clicks the Forgot Password link.
     */
    public void clickForgotPassword() {
        getLoginPage().clickForgotPassword();
    }

    /**
     * Gets the current page title.
     */
    public String getPageTitle() {
        return getLoginPage().getPageTitle();
    }
}
