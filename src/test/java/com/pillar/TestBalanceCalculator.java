package com.pillar;

import com.pillar.account.Account;
import com.pillar.transaction.TransactionRecord;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import static com.pillar.TestTransactionController.APPROVED;
import static org.junit.Assert.assertEquals;

public class TestBalanceCalculator {
    private static final Instant NOW = Instant.now();

    @Test
    public void transactionBalanceReturns5GivenASingleTransactionRecordOf5() {
        TransactionRecord tran = new TransactionRecord(5.0, NOW, APPROVED, new Account());

        double expected = BalanceCalculator.transactionBalance(list(tran));

        assertEquals(5, expected, 0.001);
    }

    @Test
    public void transactionBalanceReturns0GivenNoTransactionRecords() {
        double expected = BalanceCalculator.transactionBalance(list());

        assertEquals(0, expected, 0.001);
    }

    @Test
    public void transactionBalanceReturns5GivenASingleAuthorizationTransactionRecordOf5() {
        TransactionRecord authTransaction = new AuthorizationTransactionRecord(5.0);
        double expected = BalanceCalculator.transactionBalance(list(authTransaction));
        assertEquals(5.0, expected, 0.001);
    }

    @Test
    public void chargeBalanceReturns0GivenSingleAuthorizationTransactionOf5() {
        TransactionRecord authTransaction = new AuthorizationTransactionRecord(5.0);

        double expected = BalanceCalculator.chargeBalance(list(authTransaction));

        assertEquals(0, expected, 0.001);
    }

    @Test
    public void chargeBalanceReturnsChargeAmountGivenChargeAndAuthorizationTransactionRecords() {
        double chargeAmount = 5.0;
        ChargeTransactionRecord chargeTransaction = new ChargeTransactionRecord(chargeAmount);
        AuthorizationTransactionRecord authTransaction = new AuthorizationTransactionRecord(1.0);

        double expected = BalanceCalculator.chargeBalance(list(chargeTransaction, authTransaction));

        assertEquals(chargeAmount, expected, 0.001);
    }

    private ArrayList<TransactionRecord> list(TransactionRecord... transactions) {
        ArrayList<TransactionRecord> returnValue = new ArrayList<>();
        Collections.addAll(returnValue, transactions);
        return returnValue;
    }
}
