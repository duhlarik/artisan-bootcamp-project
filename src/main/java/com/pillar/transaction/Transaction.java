package com.pillar.transaction;

import io.micrometer.core.lang.Nullable;

import java.util.Date;

public class Transaction {

    private String cardNumber;
    private Double amount;
    private Date dateOfTransaction;
    private Integer customerId;
    private int creditLimit;

    public Transaction() {}

    public Transaction(String cardNumber, Double amount, Date dateOfTransaction, Integer customerId, @Nullable int creditLimit) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.dateOfTransaction = dateOfTransaction;
        this.customerId = customerId;
        this.creditLimit = creditLimit;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }
}
