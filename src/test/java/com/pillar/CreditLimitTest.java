package com.pillar;

import com.pillar.account.Account;
import com.pillar.transaction.TransactionRecord;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class CreditLimitTest {
    @Test
    public void validateReturnsFalseIfAmountGreaterThanCreditLimit() {
        Double amount = 10.0;
        Double creditLimit = 5.0;

        boolean approved = new CreditLimit().validate(amount, creditLimit);

        assertEquals(false, approved);
    }

    @Test
    public void validateReturnsTrueIfAmountLessThanCreditLimit() {
        Double amount = 10.0;
        Double creditLimit = 15.0;

        boolean approved = new CreditLimit().validate(amount, creditLimit);

        assertEquals(true, approved);
    }

    @Test
    public void validateReturnsTrueIfAmountEqualToCreditLimit() {
        Double amount = 10.0;
        Double creditLimit = amount;

        boolean approved = new CreditLimit().validate(amount, creditLimit);

        assertEquals(true, approved);
    }

    @Test
    public void balanceReturns0GivenEmptyTransactionList() {
        ArrayList<TransactionRecord> transactionRecordList = new ArrayList<>();
        double actual = CreditLimit.calculateBalance(transactionRecordList);
        assertEquals(0.0, actual, 0.001);
    }

    @Test
    public void validateReturnsFalseIfSumOfBalanceAndAmountExceedCreditLimit() {
        double balance = 10.0;
        int transactionAmount = 20;
        int creditLimit = 20;
        ArrayList<TransactionRecord> transactionRecordList = new ArrayList<>();
        transactionRecordList.add(new TransactionRecord(balance, Instant.now(), true, new Account()));

        boolean actual = CreditLimit.validate(transactionRecordList, transactionAmount, creditLimit);

        assertEquals(false, actual);
    }

    @Test
    public void validateReturnsTrueIfSumOfBalanceAndAmountEqualCreditLimit() {
        double balance = 10.0;
        int transactionAmount = 10;
        int creditLimit = 20;
        ArrayList<TransactionRecord> transactionRecordList = new ArrayList<>();
        transactionRecordList.add(new TransactionRecord(balance, Instant.now(), true, new Account()));

        boolean actual = CreditLimit.validate(transactionRecordList, transactionAmount, creditLimit);

        assertEquals(true, actual);
    }

    @Test
    public void balanceReturnsAmountOfTransactionGivenTransactionRecordListWithOneRecord() {
        double amount = 10.0;
        TransactionRecord transactionRecord = new TransactionRecord(amount, Instant.now(), true, new Account());
        ArrayList<TransactionRecord> transactionRecordList = new ArrayList<>();
        transactionRecordList.add(transactionRecord);

        double actual = CreditLimit.calculateBalance(transactionRecordList);

        assertEquals(amount, actual, 0.001);
    }
}
