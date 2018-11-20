package com.pillar.fakeBankService;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class TransactionRequest {

    public TransactionRequest() {}

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String cardNumber) {
        this.creditCardNumber = cardNumber;
    }

    @NotNull
    private String creditCardNumber;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @NotNull
    private Double amount;

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
    }

    @NotNull
    private Integer creditLimit;

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    @NotNull
    private Date dateOfTransaction;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @NotNull
    private Integer customerId;
}
