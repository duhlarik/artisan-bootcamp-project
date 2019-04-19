package com.pillar;

import com.pillar.account.Account;
import com.pillar.transaction.TransactionRecord;

import java.time.Instant;
import java.util.ArrayList;

public class TransactionRecordGenerator {
    private final boolean isCharge;
    private Instant dateOfTransaction;
    private double amount;
    private ArrayList<TransactionRecord> transactions;
    private Account account;

    public TransactionRecordGenerator(double amount, ArrayList<TransactionRecord> existingTransaction, Instant dateOfTransaction, boolean isCharge, Account account) {
        this.dateOfTransaction = dateOfTransaction;
        this.amount = amount;
        this.isCharge = isCharge;
        this.transactions = existingTransaction;
        this.account = account;
    }

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

    public TransactionRecord generate() {
        return new TransactionRecord(this.amount, dateOfTransaction, isTransactionValid(), this.account, isCharge);
    }

    public boolean isTransactionValid() {
        return amount + calculateTransactionBalance(this.transactions) <= account.getCreditLimit();
    }
}
