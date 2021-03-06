Feature: Transaction
  As a business customer, I want to create a transaction when a cardholder buys something,
  So that cardholders can make transactions and Steve Goliath can buy a yacht with his profits.

  Scenario: A successful transaction is created
    Given an active card and account
    When a purchase transaction request is made,
    Then success response is returned