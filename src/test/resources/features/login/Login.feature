@Login @Module-Login
Feature: Login Functionality
  As a user of the Healthcare Facility Management application
  I want to be able to log in with valid credentials
  So that I can access the system dashboard and manage healthcare facilities

  Background:
    Given the user is on the Login page

  # ==================== Positive Scenarios ====================

  @Test @Positive @TC-LOGIN-001
  Scenario: Successful login with valid credentials
    When the user enters valid username and password
    And the user clicks the Login button
    Then the user should be redirected to the Dashboard page
    And the Dashboard page should be displayed

  @Regression @Positive @TC-LOGIN-002
  Scenario Outline: Login with multiple valid credentials
    When the user enters username "<username>" and password "<password>"
    And the user clicks the Login button
    Then the user should be redirected to the Dashboard page

    Examples:
      | username              | password         |
      | admin@healthcare.com  | SecurePassword123|
      | user1@healthcare.com  | UserPass123      |

  # ==================== Negative Scenarios ====================

  @Smoke @Negative @TC-LOGIN-003
  Scenario: Login with invalid username
    When the user enters username "invalid_user" and password "SecurePassword123"
    And the user clicks the Login button
    Then an error message should be displayed
    And the user should remain on the Login page

  @Regression @Negative @TC-LOGIN-004
  Scenario: Login with invalid password
    When the user enters username "admin@healthcare.com" and password "WrongPassword"
    And the user clicks the Login button
    Then an error message should be displayed

  @Regression @Negative @TC-LOGIN-005
  Scenario: Login with empty username and password
    When the user enters username "" and password ""
    And the user clicks the Login button
    Then an error message should be displayed

  @Regression @Negative @TC-LOGIN-006
  Scenario: Login with empty username
    When the user enters username "" and password "SecurePassword123"
    And the user clicks the Login button
    Then an error message should be displayed

  @Regression @Negative @TC-LOGIN-007
  Scenario: Login with empty password
    When the user enters username "admin@healthcare.com" and password ""
    And the user clicks the Login button
    Then an error message should be displayed

  # ==================== UI Validation ====================

  @Sanity @UI @TC-LOGIN-008
  Scenario: Verify Login page UI elements
    Then the username field should be displayed
    And the password field should be displayed
    And the Login button should be enabled

  @Regression @UI @TC-LOGIN-009
  Scenario: Verify Login page title
    Then the Login page title should be correct
