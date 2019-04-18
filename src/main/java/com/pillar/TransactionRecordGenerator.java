package com.pillar;

import com.pillar.account.Account;
import com.pillar.transaction.Transaction;
import com.pillar.transaction.TransactionRecord;

import java.time.Instant;
import java.util.ArrayList;

public class TransactionRecordGenerator {
    private final boolean isCharge;
    private Instant dateOfTransaction;
    private double amount;
    private double creditLimit;
    private ArrayList<TransactionRecord> transactions;
    private Account account;

    public TransactionRecordGenerator(double amount, ArrayList<TransactionRecord> existingTransaction, double creditLimit, Instant dateOfTransaction, boolean isCharge, Account account) {
        this.dateOfTransaction = dateOfTransaction;
        this.amount = amount;
        this.creditLimit = creditLimit;
        this.isCharge = isCharge;
        this.transactions = existingTransaction;
        this.account = account;
    }

    public TransactionRecord generate() {
        boolean approved = new Transaction(this.amount, Balance.calculateTransactionBalance(this.transactions), this.creditLimit).isValid();
        return new TransactionRecord(this.amount, dateOfTransaction, approved, this.account, isCharge);
    }
}
