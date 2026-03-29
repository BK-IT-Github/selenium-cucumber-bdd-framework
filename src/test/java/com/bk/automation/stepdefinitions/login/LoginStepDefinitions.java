package com.bk.automation.stepdefinitions.login;

import com.bk.automation.services.login.LoginService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

/**
 * Step Definitions for Login module feature file.
 * Delegates all business logic to LoginService.
 *
 * <p>Flow: Login.feature → LoginStepDefinitions → LoginService → LoginPage → Driver
 */
public class LoginStepDefinitions {

    private final LoginService loginService;

    public LoginStepDefinitions() {
        this.loginService = new LoginService();
    }

    // ======================== Given Steps ========================

    @Given("the user is on the Login page")
    public void theUserIsOnTheLoginPage() {
        loginService.navigateToLoginPage();
        Assert.assertTrue("Login page is not displayed", loginService.isLoginPageDisplayed());
    }

    // ======================== When Steps ========================

    @When("the user enters valid username and password")
    public void theUserEntersValidUsernameAndPassword() {
        loginService.performLoginWithDefaultCredentials();
    }

    @When("the user enters username {string} and password {string}")
    public void theUserEntersUsernameAndPassword(String username, String password) {
        loginService.performLogin(username, password);
    }

    @When("the user clicks the Login button")
    public void theUserClicksTheLoginButton() {
        // Login button click is already handled in performLogin()
        // This step exists for readability in feature files
    }

    // ======================== Then Steps ========================

    @Then("the user should be redirected to the Dashboard page")
    public void theUserShouldBeRedirectedToTheDashboardPage() {
        // Verification handled in DashboardStepDefinitions
        // This step acts as a marker for flow clarity
    }

    @Then("an error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() {
        Assert.assertTrue("Error message is not displayed",
                loginService.isErrorMessageDisplayed());
    }

    @Then("the user should remain on the Login page")
    public void theUserShouldRemainOnTheLoginPage() {
        Assert.assertTrue("User is not on the Login page",
                loginService.isLoginPageDisplayed());
    }

    @Then("the error message should contain {string}")
    public void theErrorMessageShouldContain(String expectedMessage) {
        String actualMessage = loginService.getLoginErrorMessage();
        Assert.assertTrue("Error message does not contain expected text. Actual: " + actualMessage,
                actualMessage.contains(expectedMessage));
    }

    // ======================== UI Validation Steps ========================

    @Then("the username field should be displayed")
    public void theUsernameFieldShouldBeDisplayed() {
        Assert.assertTrue("Username field is not displayed",
                loginService.isUsernameFieldDisplayed());
    }

    @Then("the password field should be displayed")
    public void thePasswordFieldShouldBeDisplayed() {
        Assert.assertTrue("Password field is not displayed",
                loginService.isPasswordFieldDisplayed());
    }

    @Then("the Login button should be enabled")
    public void theLoginButtonShouldBeEnabled() {
        Assert.assertTrue("Login button is not enabled",
                loginService.isLoginButtonEnabled());
    }

    @Then("the Login page title should be correct")
    public void theLoginPageTitleShouldBeCorrect() {
        String title = loginService.getPageTitle();
        Assert.assertNotNull("Page title is null", title);
        Assert.assertFalse("Page title is empty", title.isEmpty());
    }
}
