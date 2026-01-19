Feature: Community management

  Background:
    Given I open the login page
    When I enter username "admin@outsmart-tech.com" and password "Password@123"
    And I submit the login form
    Then I should be logged in and navigated to dashboard
    When I click the "community" tab

  Scenario: Create a community successfully
#    When I create a community with name "Community A" and description "First community"
    When I create communities from csv "testdata/community.csv"
    Then I should see community in the community list

  Scenario: Fail to create community when name is missing
    When I create a community with name "" and description "No name"
    Then I should see community error message "Name is required"

  Scenario: Fail to create duplicate community
    Given I create a community with name "Community B" and description "Second"
    When I create a community with name "Community B" and description "Duplicate"
    Then I should see community error message "Community already exists"

  Scenario: Edit a community successfully
    Given I create a community with name "Community C" and description "Third"
    When I edit community "Community C" to name "Community C Updated" and description "Updated description"
    Then I should see community "Community C Updated" in the community list

  Scenario: Fail to edit community to a name that already exists
    Given I create a community with name "Community D" and description "Fourth"
    And I create a community with name "Community E" and description "Fifth"
    When I edit community "Community D" to name "Community E" and description "Conflict"
    Then I should see community error message "Community already exists"

