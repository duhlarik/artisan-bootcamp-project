package com.pillar;

public class CreditLimitValidator {
    public static boolean validate(Double amount, Double creditLimit) {
        return amount <= creditLimit;
    }
}
