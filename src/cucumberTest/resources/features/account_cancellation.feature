Feature: Account Cancellation
  As a FinTech customer, I want a process to cancel a credit card So that a card can be de-activated.

  Scenario: Cancelling a card
    Given an active credit card account
    When a request is made to cancel the card
    Then the card should become inactive
