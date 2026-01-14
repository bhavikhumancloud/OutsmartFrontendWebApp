Feature: Navigation after login

  Background:
    Given I open the login page

  Scenario: Navigate to Community
    When I enter username "admin@outsmart-tech.com" and password "Password@123"
    And I submit the login form
    When I click the "community" tab
    Then I should see the "community" page

  Scenario: Navigate to Groups
    When I enter username "admin@outsmart-tech.com" and password "Password@123"
    And I submit the login form
    When I click the "group" tab
    Then I should see the "group" page

  Scenario: Navigate to Users
    When I enter username "admin@outsmart-tech.com" and password "Password@123"
    And I submit the login form
    When I click the "user" tab
    Then I should see the "user" page

  Scenario: Navigate to Emergency
    When I enter username "admin@outsmart-tech.com" and password "Password@123"
    And I submit the login form
    When I click the "emergency" tab
    Then I should see the "emergency" page

