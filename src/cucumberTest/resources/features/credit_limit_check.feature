Feature: Denying transactions that exceed the credit limit
    
Scenario: A transaction above the limit is denied
    Given a credit card with balance below the credit limit
    When a transaction request is made that would make the balance exceed the credit limit
    Then the transaction is denied


Scenario: A transaction below the limit is allowed
    Given a credit card with balance below the credit limit
    When a transaction request is made that keeps the balance below the credit limit
    Then the transaction is allowed