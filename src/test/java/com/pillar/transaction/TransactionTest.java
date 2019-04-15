package com.pillar.transaction;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransactionTest {
    @Test
    public void isValidReturnsTrueGivenBalance5_Amount5_CreditLimit_10() {
        Double amount = 5.0;
        Double balance = 5.0;
        Double creditLimit = 10.0;
        Transaction transaction = new Transaction(amount,  balance, creditLimit);

        boolean expected = transaction.isValid();

        assertEquals(true, expected);
    }

    @Test
    public void isValidReturnsFalseGivenAmount6_Balance5_CreditLimit_10() {
        Transaction transaction = new Transaction(6.0, 5.0, 10.0);

        boolean expected = transaction.isValid();

        assertEquals(false, expected);
    }

    @Test
    public void saveCallsTransactionRepositoryWithATransactionRecordMatchingTheTransaction() {
        
    }
}