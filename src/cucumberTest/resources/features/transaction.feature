Feature: Transaction
  As a business customer, I want to create a transactionBankRequest when a cardholder buys something,
  So that cardholders can make transactions and Steve Goliath can buy a yacht with his profits.

  Scenario: A successful V2 transaction is created
    Given an active card and account
    When a v2 purchase transaction request is made,
    Then success response is returned
