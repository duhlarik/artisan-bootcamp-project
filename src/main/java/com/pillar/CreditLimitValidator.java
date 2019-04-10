package com.pillar;

public class CreditLimitValidator {
    public boolean validate(Double amount, Double creditLimit) {
        return amount <= creditLimit;
    }
}
