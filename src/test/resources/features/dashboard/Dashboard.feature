@Dashboard @Module-Dashboard
Feature: Dashboard Functionality
  As a logged-in user of the Healthcare Facility Management application
  I want to interact with the Dashboard
  So that I can view facility management data and navigate to different modules

  Background:
    Given the user is logged in and on the Dashboard page

  # ==================== Dashboard Verification ====================

  @Smoke @Positive @TC-DASH-001
  Scenario: Verify Dashboard page is displayed after login
    Then the Dashboard page should be displayed
    And the welcome message should be visible
    And the navigation menu should be visible

  @Regression @Positive @TC-DASH-002
  Scenario: Verify logged-in user name is displayed
    Then the logged-in user name should be displayed on the Dashboard

  @Regression @Positive @TC-DASH-003
  Scenario: Verify dashboard widgets are displayed
    Then the Dashboard should display at least one widget

  # ==================== Navigation ====================

  @Regression @Navigation @TC-DASH-004
  Scenario: Verify navigation menu items
    Then the navigation menu should contain the expected items

  @Regression @Navigation @TC-DASH-005
  Scenario Outline: Navigate to different modules
    When the user clicks on the "<module>" menu item
    Then the user should be navigated to the "<module>" page

    Examples:
      | module     |
      | Dashboard  |

  # ==================== Search ====================

  @Regression @Search @TC-DASH-006
  Scenario: Perform search on Dashboard
    When the user performs a search with keyword "facility"
    Then the search results should be displayed

  # ==================== Logout ====================

  @Smoke @Positive @TC-DASH-007
  Scenario: Successful logout from Dashboard
    When the user clicks the Logout button
    Then the user should be redirected to the Login page

  @Regression @Positive @TC-DASH-008
  Scenario: Verify logout button is available
    Then the Logout button should be displayed on the Dashboard

  # ==================== Notifications ====================

  @Sanity @UI @TC-DASH-009
  Scenario: Verify notification icon is displayed
    Then the notification icon should be visible on the Dashboard

  # ==================== Dashboard Title ====================

  @Sanity @UI @TC-DASH-010
  Scenario: Verify Dashboard page title
    Then the Dashboard page title should be correct
