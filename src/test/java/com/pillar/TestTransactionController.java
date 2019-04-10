package com.pillar;

import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import com.pillar.cardholder.Cardholder;
import com.pillar.transaction.Transaction;
import com.pillar.transaction.TransactionRecord;
import com.pillar.transaction.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sun.security.timestamp.TSResponse;

import javax.xml.ws.http.HTTPException;
import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestTransactionController {

    public static final int CREDIT_LIMIT = 10000;
    private TransactionController controller;
    private MockMvc mockMvc;
    private BankService bankService;
    private AccountRepository accountRepository;
    private Account testAccount;
    private TransactionRepository transactionRepository;

    private Transaction validTransaction;
    private Transaction invalidTransaction;

    @Before
    public void setUp() {
        accountRepository = mock(AccountRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        bankService = mock(BankService.class);

        controller = new TransactionController(accountRepository, transactionRepository, bankService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        testAccount = new Account(1, CREDIT_LIMIT, "1234", true, new Cardholder());

        String cardNumber = "1234";
        when(accountRepository.findByCardNumber(cardNumber)).thenReturn(testAccount);

        validTransaction = new Transaction("1234", 4.00, new Date(), 9, null);
        invalidTransaction = new Transaction("1234", 10999.00, new Date(), 9, null);
    }

    @Test
    public void transactionControllerExists() {
        assertNotNull(controller);
    }

    @Test
    public void transactionControllerReturnsOkStatusCodeForPostRequest() {
        postValidTransaction();
        ResponseEntity<?> transactionResponse = controller.createTransaction(validTransaction);
        assertEquals(transactionResponse.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void transactionControllerReturnsA403ResponseCodeForInvalidTransaction() {
        postInvalidTransaction();
        ResponseEntity<?> transactionResponse = controller.createTransaction(invalidTransaction);
        assertEquals(transactionResponse.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    private void postValidTransaction() {
        when(bankService.postTransaction(validTransaction)).thenReturn(HttpStatus.CREATED);
    }

    private void postInvalidTransaction() {
        when(bankService.postTransaction(invalidTransaction)).thenReturn(HttpStatus.FORBIDDEN);
    }

    @Test
    public void returns201CreatedWhenAmountLessThanCreditLimit() {
        Double amountLessThanLimit = CREDIT_LIMIT - 1.0;
        TransactionController.TransactionRequest request = new TransactionController.TransactionRequest("DUMMY", amountLessThanLimit, Instant.now(), "JUNK RETAILER");

        when(transactionRepository.save(any())).thenReturn(new TransactionRecord());
        ResponseEntity<?> transactionResponse = controller.createDbTransaction(request);

        assertEquals(HttpStatus.CREATED, transactionResponse.getStatusCode());
    }
}
