package com.bk.automation.pages.dashboard;

import com.bk.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object representing the Dashboard page of the Healthcare Facility Management application.
 * Contains all element locators and low-level page interactions for the Dashboard.
 */
public class DashboardPage extends BasePage {

    // ======================== Locators (XPath) ========================

    // --- Header / Navigation ---
    private static final By HEADER_WELCOME = By.xpath("//h1[contains(@class,'welcome') or contains(@class,'dashboard-title')] | //span[contains(@class,'welcome-text')]");
    private static final By NAV_MENU = By.xpath("//nav | //div[contains(@class,'sidebar') or contains(@class,'navigation')]");
    private static final By LBL_LOGGED_USER = By.xpath("//span[contains(@class,'user-name') or contains(@class,'logged-user')] | //div[contains(@class,'user-info')]//span");
    private static final By BTN_LOGOUT = By.xpath("//a[contains(text(),'Logout') or contains(text(),'Sign Out')] | //button[contains(text(),'Logout')]");
    private static final By BTN_USER_PROFILE = By.xpath("//a[contains(@href,'profile') or contains(@class,'profile')] | //div[contains(@class,'user-avatar')]");

    // --- Dashboard Widgets ---
    private static final By DASHBOARD_WIDGETS = By.xpath("//div[contains(@class,'widget') or contains(@class,'card') or contains(@class,'dashboard-item')]");
    private static final By DASHBOARD_STATS = By.xpath("//div[contains(@class,'stat') or contains(@class,'metric') or contains(@class,'kpi')]");

    // --- Quick Actions ---
    private static final By BTN_QUICK_ACTIONS = By.xpath("//button[contains(@class,'quick-action')] | //a[contains(@class,'quick-action')]");

    // --- Notifications ---
    private static final By ICON_NOTIFICATION = By.xpath("//i[contains(@class,'notification') or contains(@class,'bell')] | //span[contains(@class,'notification-icon')]");
    private static final By BADGE_NOTIFICATION_COUNT = By.xpath("//span[contains(@class,'badge') or contains(@class,'notification-count')]");

    // --- Search ---
    private static final By INPUT_SEARCH = By.xpath("//input[contains(@placeholder,'Search') or contains(@class,'search')]");

    // --- Menu Items ---
    private static final By MENU_ITEMS = By.xpath("//nav//a | //div[contains(@class,'sidebar')]//a | //ul[contains(@class,'menu')]//li//a");

    // ======================== PageFactory Elements ========================

    @FindBy(xpath = "//h1[contains(@class,'welcome') or contains(@class,'dashboard-title')] | //span[contains(@class,'welcome-text')]")
    private WebElement welcomeHeader;

    @FindBy(xpath = "//a[contains(text(),'Logout') or contains(text(),'Sign Out')] | //button[contains(text(),'Logout')]")
    private WebElement logoutButton;

    // ======================== Page Actions ========================

    /**
     * Gets the welcome message/header text on the dashboard.
     *
     * @return welcome message text
     */
    public String getWelcomeMessage() {
        String message = getElementText(HEADER_WELCOME);
        logger.info("Dashboard welcome message: {}", message);
        return message;
    }

    /**
     * Gets the logged-in user's display name.
     *
     * @return the displayed username
     */
    public String getLoggedInUserName() {
        String userName = getElementText(LBL_LOGGED_USER);
        logger.info("Logged in user: {}", userName);
        return userName;
    }

    /**
     * Clicks the Logout button.
     */
    public void clickLogout() {
        logger.info("Clicking Logout button");
        clickElement(BTN_LOGOUT);
    }

    /**
     * Clicks on the user profile icon/link.
     */
    public void clickUserProfile() {
        logger.info("Clicking User Profile");
        clickElement(BTN_USER_PROFILE);
    }

    /**
     * Returns the number of dashboard widgets displayed.
     *
     * @return count of widgets
     */
    public int getDashboardWidgetCount() {
        List<WebElement> widgets = driver.findElements(DASHBOARD_WIDGETS);
        logger.info("Dashboard widget count: {}", widgets.size());
        return widgets.size();
    }

    /**
     * Returns the text of all dashboard statistic values.
     *
     * @return list of stat texts
     */
    public List<String> getDashboardStats() {
        List<WebElement> stats = driver.findElements(DASHBOARD_STATS);
        return stats.stream()
                .map(el -> el.getText().trim())
                .collect(Collectors.toList());
    }

    /**
     * Checks if the navigation menu is displayed.
     */
    public boolean isNavigationMenuDisplayed() {
        return isElementDisplayed(NAV_MENU);
    }

    /**
     * Checks if the notification icon is displayed.
     */
    public boolean isNotificationIconDisplayed() {
        return isElementDisplayed(ICON_NOTIFICATION);
    }

    /**
     * Gets the notification count from the badge.
     *
     * @return notification count as string
     */
    public String getNotificationCount() {
        if (isElementDisplayed(BADGE_NOTIFICATION_COUNT)) {
            return getElementText(BADGE_NOTIFICATION_COUNT);
        }
        return "0";
    }

    /**
     * Performs a search on the dashboard.
     *
     * @param searchText the text to search for
     */
    public void performSearch(String searchText) {
        logger.info("Searching for: {}", searchText);
        typeText(INPUT_SEARCH, searchText);
    }

    /**
     * Gets all menu item names from the navigation.
     *
     * @return list of menu item text
     */
    public List<String> getMenuItems() {
        List<WebElement> menuItems = driver.findElements(MENU_ITEMS);
        return menuItems.stream()
                .map(el -> el.getText().trim())
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Clicks a specific menu item by its name.
     *
     * @param menuName the name of the menu item to click
     */
    public void clickMenuItem(String menuName) {
        By menuLocator = By.xpath(String.format(
                "//nav//a[contains(text(),'%s')] | //div[contains(@class,'sidebar')]//a[contains(text(),'%s')]",
                menuName, menuName));
        logger.info("Clicking menu item: {}", menuName);
        clickElement(menuLocator);
    }

    /**
     * Checks if the logout button is displayed.
     */
    public boolean isLogoutButtonDisplayed() {
        return isElementDisplayed(BTN_LOGOUT);
    }

    @Override
    public boolean isPageLoaded() {
        logger.info("Verifying Dashboard page is loaded");
        return isElementDisplayed(HEADER_WELCOME) || isElementDisplayed(NAV_MENU);
    }
}
