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

    private ArrayList<TransactionRecord> list(TransactionRecord... transactions) {
        ArrayList<TransactionRecord> returnValue = new ArrayList<>();
        Collections.addAll(returnValue, transactions);
        return returnValue;
    }
}
