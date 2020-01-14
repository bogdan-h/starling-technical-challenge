Feature: Roundup

  Scenario: Should return OK HTTP status
    When I invoke the roundup feature
    Then The HTTP response status will be 200