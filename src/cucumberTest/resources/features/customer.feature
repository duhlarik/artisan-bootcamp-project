Feature: Customer Management
  As a credit card processor, I want to be able to keep track of my customer so that I can provide a high quality of service

  Scenario: Customer has a name
    Given a customer
    When I request it's information
    Then the name is "Test Customer"
