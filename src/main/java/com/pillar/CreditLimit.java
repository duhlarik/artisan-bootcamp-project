package com.pillar;

import com.pillar.transaction.TransactionRecord;

import java.util.ArrayList;

public class CreditLimit {
    public static boolean validate(Double amount, Double creditLimit) {
        return amount <= creditLimit;
    }

    public static double calculateBalance(ArrayList<TransactionRecord> transactionRecordList) {
        return transactionRecordList.stream().
                mapToDouble(tr -> tr.getAmount()).
                sum();
    }

    public static boolean validate(ArrayList<TransactionRecord> transactionRecordList, double amount, double creditLimit) {
        return validate(amount + calculateBalance(transactionRecordList), creditLimit);
    }

}
