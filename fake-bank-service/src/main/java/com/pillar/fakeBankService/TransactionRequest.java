package com.pillar.fakeBankService;

import java.util.Date;

public class TransactionRequest {

    public TransactionRequest() {}

    private String cardNumber;
    private Double amount;
    private Double creditLimit;
    private Date dateOfTransaction;
    private String customer;

    public Double getAmount() {
        return amount;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }
}
