Feature: Merchant Management
  As a credit card processor, I want to be able to keep track of my merchants so that I can provide a high quality of service

  Scenario: Merchant has a name
    Given a merchant
    When I request it's information
    Then the name is "Test Merchant"