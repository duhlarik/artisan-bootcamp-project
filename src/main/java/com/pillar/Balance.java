package com.pillar;

import com.pillar.transaction.TransactionRecord;

import java.util.ArrayList;

public class Balance {
    public static double calculateTransactionBalance(ArrayList<TransactionRecord> list) {
        return list.stream().
                mapToDouble(TransactionRecord::getAmount).
                sum();
    }

    public static double calculateChargeBalance(ArrayList<TransactionRecord> list) {
        return list.stream()
                .filter(TransactionRecord::isCharge)
                .mapToDouble(TransactionRecord::getAmount)
                .sum();
    }
}
