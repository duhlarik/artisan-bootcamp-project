package com.pillar;

import com.pillar.transaction.TransactionBankRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestTransactionBankRequest {

    private TransactionBankRequest transactionBankRequest;

    @Before
    public void setUp() { transactionBankRequest = new TransactionBankRequest("1234", 2.00, new Date(), 2,   10000);}

    @Test
    public void testIfTheTransactionIsEqualToNotNull() {
        assertNotNull(transactionBankRequest);
    }

    @Test
    public void testTransactionHasGivenProperties() {
        assertEquals(transactionBankRequest.getCreditCardNumber(), "1234");
        assertEquals(transactionBankRequest.getAmount().doubleValue(), 2, .001);
        assertEquals(transactionBankRequest.getDateOfTransaction(), new Date());
        assertEquals(transactionBankRequest.getCustomerId().intValue(), 2);
    }
}