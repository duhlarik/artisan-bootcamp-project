Feature: API Status
  As an operations team, I want my API to have a health status so that I can know if the system is functioning correctly

  Scenario: Health check is ok
    Given the health endpoint
    When I request the status
    Then I receive an "OK"