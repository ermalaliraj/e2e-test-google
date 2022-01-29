Feature: Google Search Feature
  Scenario: Search using button
    Given Navigate to "Google" application
    When Accept cookie browser
    Then Home page is displayed
    When Insert "European Commission laws 2022" in the search input
    And Click search button
    Then Search result will be generated with 5 rows
    Then Close the browser

#  Scenario: Search using Enter key
#    When Empty search input
#    Then Press Enter key
#    Then Search result will be empty
#    When Insert search keywords "European Commission laws 2022" in the search input
#    Then Press Enter
#    Then Search result will be generated with "12" rows
#    Then Close the browser