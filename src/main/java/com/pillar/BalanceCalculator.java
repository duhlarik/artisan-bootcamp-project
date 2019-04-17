package com.pillar;

import com.pillar.transaction.TransactionRecord;

import java.util.ArrayList;

public class BalanceCalculator {
    public static double transactionBalance(ArrayList<TransactionRecord> list) {
        return list.stream().
                mapToDouble(tr -> tr.getAmount()).
                sum();
    }

    public static double chargeBalance(ArrayList<TransactionRecord> list) {
        return list.stream()
                .filter(record -> record instanceof ChargeTransactionRecord)
                .mapToDouble(record -> record.getAmount())
                .sum();
    }
}
