package com.pillar.transaction;

import com.pillar.TransactionController;

public class Transaction {
    Double amount;
    Double balance;
    Double creditLimit;

    public Transaction(Double amount, Double balance, Double creditLimit) {
        this.amount = amount;
        this.balance = balance;
        this.creditLimit = creditLimit;
    }

    public boolean isValid() {
        return amount + balance <= creditLimit;
    }
}
