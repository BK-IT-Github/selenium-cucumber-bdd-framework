package com.bk.automation.services.dashboard;

import com.bk.automation.pages.dashboard.DashboardPage;
import com.bk.automation.services.BaseService;
import com.bk.automation.utils.WaitUtil;

import java.util.List;

/**
 * Service class encapsulating Dashboard module business logic.
 * Orchestrates DashboardPage interactions for complex dashboard workflows.
 *
 * <p>Flow: DashboardStepDefinitions → DashboardService → DashboardPage → Driver
 */
public class DashboardService extends BaseService {

    private DashboardPage dashboardPage;

    public DashboardService() {
        super();
    }

    /**
     * Lazily initializes the DashboardPage.
     */
    private DashboardPage getDashboardPage() {
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage();
        }
        return dashboardPage;
    }

    /**
     * Verifies that the dashboard page is loaded correctly.
     *
     * @return true if the dashboard is loaded
     */
    public boolean isDashboardDisplayed() {
        WaitUtil.waitForPageLoad();
        return getDashboardPage().isPageLoaded();
    }

    /**
     * Gets the welcome message from the dashboard.
     *
     * @return welcome message text
     */
    public String getWelcomeMessage() {
        return getDashboardPage().getWelcomeMessage();
    }

    /**
     * Gets the display name of the logged-in user.
     *
     * @return logged-in user display name
     */
    public String getLoggedInUserName() {
        return getDashboardPage().getLoggedInUserName();
    }

    /**
     * Performs a logout operation from the dashboard.
     */
    public void performLogout() {
        logger.info("Performing logout from dashboard");
        getDashboardPage().clickLogout();
        WaitUtil.waitForPageLoad();
    }

    /**
     * Navigates to a specific module via the sidebar/menu.
     *
     * @param moduleName the module name to navigate to
     */
    public void navigateToModule(String moduleName) {
        logger.info("Navigating to module: {}", moduleName);
        getDashboardPage().clickMenuItem(moduleName);
        WaitUtil.waitForPageLoad();
    }

    /**
     * Gets the count of dashboard widgets.
     *
     * @return widget count
     */
    public int getWidgetCount() {
        return getDashboardPage().getDashboardWidgetCount();
    }

    /**
     * Gets all visible dashboard statistics.
     *
     * @return list of stat values
     */
    public List<String> getDashboardStats() {
        return getDashboardPage().getDashboardStats();
    }

    /**
     * Gets all navigation menu items.
     *
     * @return list of menu item names
     */
    public List<String> getAvailableMenuItems() {
        return getDashboardPage().getMenuItems();
    }

    /**
     * Checks if the navigation menu is visible.
     */
    public boolean isNavigationMenuVisible() {
        return getDashboardPage().isNavigationMenuDisplayed();
    }

    /**
     * Checks if the notification icon is visible.
     */
    public boolean isNotificationIconVisible() {
        return getDashboardPage().isNotificationIconDisplayed();
    }

    /**
     * Gets the notification count.
     */
    public String getNotificationCount() {
        return getDashboardPage().getNotificationCount();
    }

    /**
     * Performs a dashboard search.
     *
     * @param searchQuery the search text
     */
    public void performSearch(String searchQuery) {
        getDashboardPage().performSearch(searchQuery);
    }

    /**
     * Gets the page title.
     */
    public String getPageTitle() {
        return getDashboardPage().getPageTitle();
    }

    /**
     * Checks if the logout button is available.
     */
    public boolean isLogoutButtonDisplayed() {
        return getDashboardPage().isLogoutButtonDisplayed();
    }
}
