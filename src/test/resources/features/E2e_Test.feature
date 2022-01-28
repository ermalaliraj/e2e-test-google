@BaseLineScenario
Feature: BaseLine Scenarios

  @HomepageTesting
  Scenario: E2e Home Testing
    Given navigate to "Google" application
    Then Home page is displayed
    When Search "European Commission laws 2022"
    Then Validate search result
    Then Close the browser


#  @NewStudentTesting
#  Scenario: E2e Test Add new student
#    Given Empty search result
##    Then  Close the browser