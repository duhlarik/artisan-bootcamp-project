package com.pillar.transaction;

import io.micrometer.core.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

public class Transaction {

    private String creditCardNumber;
    private Double amount;
    private Date dateOfTransaction;
    private Integer customerId;
    private Integer creditLimit;

    public Transaction() {}

    public Transaction(String creditCardNumber, Double amount, Date dateOfTransaction, Integer customerId, Integer creditLimit) {
        this.creditCardNumber = creditCardNumber;
        this.amount = amount;
        this.dateOfTransaction = dateOfTransaction;
        this.customerId = customerId;
        this.creditLimit = creditLimit;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String cardNumber) {
        this.creditCardNumber = cardNumber;
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

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
    }
}
