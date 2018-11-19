package com.pillar;

import com.pillar.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestTransaction {

    private Transaction transaction;

    @Before
    public void setUp() { transaction = new Transaction("1234", 2.00, new Date(), 2,   10000.00);}

    @Test
    public void testIfTheTransactionIsEqualToNotNull() {
        assertNotNull(transaction);
    }

    @Test
    public void testTransactionHasGivenProperties() {
        assertEquals(transaction.getCardNumber(), "1234");
        assertEquals(transaction.getAmount().doubleValue(), 2, .001);
        assertEquals(transaction.getDateOfTransaction(), new Date());
        assertEquals(transaction.getCustomerId().intValue(), 2);
    }
}