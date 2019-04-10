Feature: Transaction
  As a business customer, I want to create a transactionBankRequest when a cardholder buys something,
  So that cardholders can make transactions and Steve Goliath can buy a yacht with his profits.

  Scenario: A successful transactionBankRequest is created
    Given an active card and account
    When a purchase transactionBankRequest request is made,
    Then success response is returned

  Scenario: A successful V2 transactionBankRequest is created
    Given an active card and account
    When a v2 purchase transactionBankRequest request is made,
    Then success response is returned
