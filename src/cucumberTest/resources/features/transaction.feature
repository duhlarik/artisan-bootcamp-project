Feature: Transaction
  As a business customer, I want to create a transaction when a cardholder buys something,
  So that cardholders can make transactions and Steve Goliath can buy a yacht with his profits.

  Scenario: A successful transaction is created
    Given an active card and account
    When a purchase transaction request is made,
    Then success response is returned

  Scenario: A successful V2 transaction is created
    Given an active card and account
    When a v2 purchase transaction request is made,
    Then success response is returned

  Scenario: A transaction is denied
    Given an active card and account
    When two v2 transaction requests are made that each equal the credit limit
    Then forbidden response is returned