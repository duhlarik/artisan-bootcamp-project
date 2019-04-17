package com.pillar.transaction;

public class Transaction {
    private Double amount;
    private Double balance;
    private Double creditLimit;

    public Transaction(Double amount, Double balance, Double creditLimit) {
        this.amount = amount;
        this.balance = balance;
        this.creditLimit = creditLimit;
    }

    public boolean isValid() {
        return amount + balance <= creditLimit;
    }
}
