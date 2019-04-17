package com.pillar;

import com.pillar.transaction.TransactionRecord;

import java.util.ArrayList;

public class BalanceCalculator {
    public static double transactionBalance(ArrayList<TransactionRecord> list) {
        return list.stream().
                mapToDouble(TransactionRecord::getAmount).
                sum();
    }

    public static double chargeBalance(ArrayList<TransactionRecord> list) {
        return list.stream()
                .filter(TransactionRecord::isCharge)
                .mapToDouble(TransactionRecord::getAmount)
                .sum();
    }
}
