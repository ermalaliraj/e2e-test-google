@BaseLineScenario
Feature: BaseLine Scenarios

  @HomepageTesting
  Scenario: E2e Home Testing
    Given navigate to "Google" application
    Then Home page is displayed
    When Search student by name
    Then Empty search result

#  @NewStudentTesting
#  Scenario: E2e Test Add new student
#    Given Empty search result
##    Then  Close the browser