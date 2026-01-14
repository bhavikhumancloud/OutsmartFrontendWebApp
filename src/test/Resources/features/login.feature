Feature: Login

  Scenario: Successful login with valid credentials
    Given I open the login page
    When I enter username "admin@outsmart-tech.com" and password "Password@123"
    And I submit the login form
    Then I should be logged in and navigated to dashboard

  Scenario: Login fails when username is missing
    Given I open the login page
    When I enter username "" and password "Password@123"
    And I submit the login form
    Then I should see error message "Email is required"

  Scenario: Login fails when password is missing
    Given I open the login page
    When I enter username "admin@outsmart-tech.com" and password ""
    And I submit the login form
    Then I should see error message "Password is required"

  Scenario: Username and password are mandatory fields
    Given I open the login page
    When I submit the login form
    Then I should see error message "Email is required"
