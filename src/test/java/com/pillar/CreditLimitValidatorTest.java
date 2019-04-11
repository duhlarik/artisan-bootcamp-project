package com.pillar;

import com.pillar.account.Account;
import com.pillar.transaction.TransactionRecord;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class CreditLimitValidatorTest {
    @Test
    public void validateReturnsFalseIfAmountGreaterThanCreditLimit() {
        Double amount = 10.0;
        Double creditLimit = 5.0;

        boolean approved = new CreditLimitValidator().validate(amount, creditLimit);

        assertEquals(false, approved);
    }

    @Test
    public void validateReturnsTrueIfAmountLessThanCreditLimit() {
        Double amount = 10.0;
        Double creditLimit = 15.0;

        boolean approved = new CreditLimitValidator().validate(amount, creditLimit);

        assertEquals(true, approved);
    }

    @Test
    public void validateReturnsTrueIfAmountEqualToCreditLimit() {
        Double amount = 10.0;
        Double creditLimit = amount;

        boolean approved = new CreditLimitValidator().validate(amount, creditLimit);

        assertEquals(true, approved);
    }

    @Test
    public void balanceReturns0GivenEmptyTransactionList() {
        ArrayList<TransactionRecord> transactionRecordList = new ArrayList<>();
        double actual = CreditLimitValidator.calculateBalance(transactionRecordList);
        assertEquals(0.0, actual, 0.001);
    }

    @Test
    public void balanceReturnsAmountOfTransactionGivenTransactionRecordListWithOneRecord() {
        double amount = 10.0;
        TransactionRecord transactionRecord = new TransactionRecord(amount, Instant.now(), true, new Account());
        ArrayList<TransactionRecord> transactionRecordList = new ArrayList<>();
        transactionRecordList.add(transactionRecord);

        double actual = CreditLimitValidator.calculateBalance(transactionRecordList);

        assertEquals(amount, actual, 0.001);
    }
}
