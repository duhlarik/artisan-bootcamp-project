Feature: Account Creation
  As a FinTech customer, I want a process to create new accounts, so that I can issue and assign new credit cards.

  Scenario: Create a cardholder account
    Given a cardholder Name: "Steve Goliath", SSN: "123-45-6788", Business Name: "Target"
    When a request is made to create an account for this cardholder
    Then a new account is created and a new card number is issued to that account and returned
    And a credit limit of 10,000 is assigned

  @wip
  Scenario: Deny issuing a duplicate account
    Given a cardholder Name: "Steve Goliath", SSN: "123-45-6788", Business Name: "Target"
    When a request is made to create an account for this cardholder
    And a second request is made to create an account for this cardholder at the same business customer
    Then the request should fail and return an Error
