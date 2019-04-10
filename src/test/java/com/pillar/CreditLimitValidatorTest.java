package com.pillar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class CreditLimitValidatorTest {
    @Test
    public void validateReturnsFalseIfAmountGreaterThanCreditLimit() {
        Double amount = 10.0;
        Double creditLimit = 5.0;

        boolean approved = new CreditLimitValidator().validate(amount, creditLimit);

        assertEquals(false, approved);
    }

    @Test
    public void validateReturnsTrueIfAmountLessThanCreditLimit() {
        Double amount = 10.0;
        Double creditLimit = 15.0;

        boolean approved = new CreditLimitValidator().validate(amount, creditLimit);

        assertEquals(true, approved);
    }

    @Test
    public void validateReturnsTrueIfAmountEqualToCreditLimit() {
        Double amount = 10.0;
        Double creditLimit = amount;

        boolean approved = new CreditLimitValidator().validate(amount, creditLimit);

        assertEquals(true, approved);
    }

}
