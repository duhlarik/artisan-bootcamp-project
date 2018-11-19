package com.pillar;

import com.pillar.transaction.Transaction;
import com.pillar.transaction.TransactionController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestTransactionController {
    private static final String TRANSACTION_CREATE = "/create";
    private static final String TRANSACTION_REQUEST_MAPPING = "/transaction";

    private TransactionController controller;
    private MockMvc mockMvc;

    private Transaction validTransaction;
    private Transaction invalidTransaction;

    @Before
    public void setUp() {
        controller = new TransactionController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        validTransaction = new Transaction("1234", 4.00, new Date(), 9, 10000.00);
        invalidTransaction = new Transaction("1234", 3.00, new Date(), 9, 2.00);
    }

    @Test
    public void transactionControllerExists() {
        assertNotNull(controller);
    }

    @Test
    public void transactionControllerReturnsOkStatusCodeForPostRequest() throws Exception {
        HttpStatus transactionResponse = controller.createTransaction(validTransaction);
        assertEquals(transactionResponse, HttpStatus.OK);
    }

    @Test
    public void transactionControllerReturnsA403ResponseCodeForInvalidTransaction() {
        HttpStatus transactionResponse = controller.createTransaction(invalidTransaction);
        assertEquals(transactionResponse, HttpStatus.FORBIDDEN);
    }

    private ResultActions queryApi(String s) throws Exception {
        return mockMvc.perform(post(s));
    }
}
