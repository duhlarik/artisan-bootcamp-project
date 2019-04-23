package com.pillar;

import com.pillar.account.Account;
import com.pillar.cardholder.Cardholder;
import com.pillar.transaction.TransactionRecord;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestTransactionRecord {
    private static final Instant NOW = Instant.now();
    private static final boolean APPROVED = true;
    private static final int ACCOUNT_ID = 1;
    private static final String CC_NUMBER = "12345";
    private static final boolean ACTIVE = true;
    private static final Cardholder CARDHOLDER = new Cardholder();

    @Test
    public void isValidReturnsTrueWhenAmountIs2CreditLimitIs2() {
        Account account = createAccount(2);
        TransactionRecord txRecord = createTxRecord(account, 2.0);

        assertTrue(txRecord.isValid(account, new ArrayList<>()));
    }

    @Test
    public void isValidReturnsFalseWhenAmountIs2CreditLimitIs1() {
        Account account = createAccount(1);
        TransactionRecord txRecord = createTxRecord(account, 2.0);

        assertFalse(txRecord.isValid(account, new ArrayList<>()));
    }

    @Test
    public void isValidReturnsTrueWhenAmountIs1CreditLimitIs2() {
        Account account = createAccount(2);
        TransactionRecord txRecord = createTxRecord(account, 1.0);

        assertTrue(txRecord.isValid(account, new ArrayList<>()));
    }

    @Test
    public void isValidReturnsFalseWhenAmountIs1TxBalanceIs1AndCreditLimitIs1() {
        Account    account = createAccount(1);
        TransactionRecord txRecord = createTxRecord(account, 1.0);
        ArrayList<TransactionRecord> txList = new ArrayList<>();
        txList.add(createTxRecord(account, 1.0));

        boolean recordValid = txRecord.isValid(account, txList);

        assertFalse(recordValid);
    }

    private TransactionRecord createTxRecord(Account account, double amount) {
        return new TransactionRecord(amount, NOW, APPROVED, account);
    }

    private Account createAccount(int creditLimit) {
        return new Account(ACCOUNT_ID, creditLimit, CC_NUMBER, ACTIVE, CARDHOLDER);
    }
}