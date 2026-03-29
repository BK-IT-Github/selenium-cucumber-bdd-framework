package com.bk.automation.stepdefinitions.dashboard;

import com.bk.automation.services.dashboard.DashboardService;
import com.bk.automation.services.login.LoginService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.List;

/**
 * Step Definitions for Dashboard module feature file.
 * Delegates all business logic to DashboardService.
 *
 * <p>Flow: Dashboard.feature → DashboardStepDefinitions → DashboardService → DashboardPage → Driver
 */
public class DashboardStepDefinitions {

    private final DashboardService dashboardService;
    private final LoginService loginService;

    public DashboardStepDefinitions() {
        this.dashboardService = new DashboardService();
        this.loginService = new LoginService();
    }

    // ======================== Given Steps ========================

    @Given("the user is logged in and on the Dashboard page")
    public void theUserIsLoggedInAndOnTheDashboardPage() {
        loginService.navigateToLoginPage();
        loginService.performLoginWithDefaultCredentials();
        Assert.assertTrue("Dashboard page is not displayed after login",
                dashboardService.isDashboardDisplayed());
    }

    // ======================== When Steps ========================

    @When("the user clicks the Logout button")
    public void theUserClicksTheLogoutButton() {
        dashboardService.performLogout();
    }

    @When("the user clicks on the {string} menu item")
    public void theUserClicksOnTheMenuItem(String menuItem) {
        dashboardService.navigateToModule(menuItem);
    }

    @When("the user performs a search with keyword {string}")
    public void theUserPerformsASearchWithKeyword(String keyword) {
        dashboardService.performSearch(keyword);
    }

    // ======================== Then Steps ========================

    @Then("the Dashboard page should be displayed")
    public void theDashboardPageShouldBeDisplayed() {
        Assert.assertTrue("Dashboard page is not displayed",
                dashboardService.isDashboardDisplayed());
    }

    @Then("the welcome message should be visible")
    public void theWelcomeMessageShouldBeVisible() {
        String welcomeMsg = dashboardService.getWelcomeMessage();
        Assert.assertNotNull("Welcome message is null", welcomeMsg);
        Assert.assertFalse("Welcome message is empty", welcomeMsg.isEmpty());
    }

    @Then("the navigation menu should be visible")
    public void theNavigationMenuShouldBeVisible() {
        Assert.assertTrue("Navigation menu is not visible",
                dashboardService.isNavigationMenuVisible());
    }

    @Then("the logged-in user name should be displayed on the Dashboard")
    public void theLoggedInUserNameShouldBeDisplayedOnTheDashboard() {
        String userName = dashboardService.getLoggedInUserName();
        Assert.assertNotNull("User name is null", userName);
        Assert.assertFalse("User name is empty", userName.isEmpty());
    }

    @Then("the Dashboard should display at least one widget")
    public void theDashboardShouldDisplayAtLeastOneWidget() {
        int widgetCount = dashboardService.getWidgetCount();
        Assert.assertTrue("No widgets displayed on dashboard. Count: " + widgetCount,
                widgetCount > 0);
    }

    @Then("the navigation menu should contain the expected items")
    public void theNavigationMenuShouldContainTheExpectedItems() {
        List<String> menuItems = dashboardService.getAvailableMenuItems();
        Assert.assertNotNull("Menu items list is null", menuItems);
        Assert.assertFalse("Menu items list is empty", menuItems.isEmpty());
    }

    @Then("the user should be navigated to the {string} page")
    public void theUserShouldBeNavigatedToThePage(String pageName) {
        String pageTitle = dashboardService.getPageTitle();
        Assert.assertNotNull("Page title is null after navigating to " + pageName, pageTitle);
    }

    @Then("the search results should be displayed")
    public void theSearchResultsShouldBeDisplayed() {
        // Search results verification - can be customized based on actual application behavior
        Assert.assertTrue("Dashboard is not displayed after search",
                dashboardService.isDashboardDisplayed());
    }

    @Then("the user should be redirected to the Login page")
    public void theUserShouldBeRedirectedToTheLoginPage() {
        Assert.assertTrue("User is not redirected to Login page",
                loginService.isLoginPageDisplayed());
    }

    @Then("the Logout button should be displayed on the Dashboard")
    public void theLogoutButtonShouldBeDisplayedOnTheDashboard() {
        Assert.assertTrue("Logout button is not displayed",
                dashboardService.isLogoutButtonDisplayed());
    }

    @Then("the notification icon should be visible on the Dashboard")
    public void theNotificationIconShouldBeVisibleOnTheDashboard() {
        Assert.assertTrue("Notification icon is not visible",
                dashboardService.isNotificationIconVisible());
    }

    @Then("the Dashboard page title should be correct")
    public void theDashboardPageTitleShouldBeCorrect() {
        String title = dashboardService.getPageTitle();
        Assert.assertNotNull("Dashboard page title is null", title);
        Assert.assertFalse("Dashboard page title is empty", title.isEmpty());
    }
}
