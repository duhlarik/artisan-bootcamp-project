package com.pillar;

import com.pillar.account.Account;
import com.pillar.transaction.TransactionRecord;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import static com.pillar.TestTransactionController.APPROVED;
import static org.junit.Assert.assertEquals;

public class TestBalance {
    private static final Instant NOW = Instant.now();
    private static final Account ACCOUNT = new Account();

    @Test
    public void transactionBalanceReturns5GivenASingleTransactionRecordOf5() {
        TransactionRecord tran = new TransactionRecord(5.0, NOW, APPROVED, ACCOUNT);

        double expected = Balance.calculateTransactionBalance(list(tran));

        assertEquals(5, expected, 0.001);
    }

    @Test
    public void transactionBalanceReturns0GivenNoTransactionRecords() {
        double expected = Balance.calculateTransactionBalance(list());

        assertEquals(0, expected, 0.001);
    }

    @Test
    public void transactionBalanceReturns5GivenASingleAuthorizationTransactionRecordOf5() {
        TransactionRecord authTransaction = new TransactionRecord(5.0, NOW, APPROVED, ACCOUNT, false);

        double expected = Balance.calculateTransactionBalance(list(authTransaction));

        assertEquals(5.0, expected, 0.001);
    }

    @Test
    public void chargeBalanceReturns0GivenSingleAuthorizationTransactionOf5() {
        TransactionRecord authTransaction = new TransactionRecord(5.0, NOW, APPROVED, ACCOUNT, false);

        double expected = Balance.calculateChargeBalance(list(authTransaction));

        assertEquals(0, expected, 0.001);
    }

    @Test
    public void chargeBalanceReturnsChargeAmountGivenChargeAndAuthorizationTransactionRecords() {
        double chargeAmount = 5.0;
        TransactionRecord chargeTransaction = new TransactionRecord(chargeAmount, NOW, APPROVED, ACCOUNT, true);
        TransactionRecord authTransaction = new TransactionRecord(1.0, NOW, APPROVED, ACCOUNT, false);

        double expected = Balance.calculateChargeBalance(list(chargeTransaction, authTransaction));

        assertEquals(chargeAmount, expected, 0.001);
    }

    private ArrayList<TransactionRecord> list(TransactionRecord... transactions) {
        ArrayList<TransactionRecord> returnValue = new ArrayList<>();
        Collections.addAll(returnValue, transactions);
        return returnValue;
    }
}
