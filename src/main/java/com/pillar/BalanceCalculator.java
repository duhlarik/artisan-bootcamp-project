package com.pillar;

import com.pillar.transaction.TransactionRecord;

import java.util.ArrayList;

public class BalanceCalculator {
    public static double transactionBalance(ArrayList<TransactionRecord> list) {
        return CreditLimit.calculateBalance(list);
    }
}
