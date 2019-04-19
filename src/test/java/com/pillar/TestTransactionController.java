package com.pillar;

import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import com.pillar.cardholder.Cardholder;
import com.pillar.transaction.TransactionBankRequest;
import com.pillar.transaction.TransactionRecordRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestTransactionController {

    private static final int CREDIT_LIMIT = 10000;
    private static final String CARD_NUMBER = "1234";
    private static final boolean APPROVED = true;
    private TransactionController controller;
    private BankService bankService;

    private TransactionBankRequest validTransactionBankRequest;
    private TransactionBankRequest invalidTransactionBankRequest;

    @Before
    public void setUp() {
        AccountRepository accountRepository = mock(AccountRepository.class);
        TransactionRecordRepository transactionRecordRepository = mock(TransactionRecordRepository.class);
        bankService = mock(BankService.class);

        controller = new TransactionController(accountRepository, transactionRecordRepository, bankService);

        Account testAccount = new Account(1, CREDIT_LIMIT, CARD_NUMBER, APPROVED, new Cardholder());

        when(accountRepository.findByCardNumber(CARD_NUMBER)).thenReturn(testAccount);

        validTransactionBankRequest = new TransactionBankRequest(CARD_NUMBER, 4.00, new Date(), 9, null);
        invalidTransactionBankRequest = new TransactionBankRequest(CARD_NUMBER, 10999.00, new Date(), 9, null);
    }

    @Test
    public void transactionControllerExists() {
        assertNotNull(controller);
    }

    @Test
    public void transactionControllerReturnsOkStatusCodeForPostRequest() {
        postValidTransaction();
        ResponseEntity<?> transactionResponse = controller.createTransaction(validTransactionBankRequest);
        assertEquals(transactionResponse.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void transactionControllerReturnsA403ResponseCodeForInvalidTransaction() {
        postInvalidTransaction();
        ResponseEntity<?> transactionResponse = controller.createTransaction(invalidTransactionBankRequest);
        assertEquals(transactionResponse.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    private void postValidTransaction() {
        when(bankService.postTransaction(validTransactionBankRequest)).thenReturn(HttpStatus.CREATED);
    }

    private void postInvalidTransaction() {
        when(bankService.postTransaction(invalidTransactionBankRequest)).thenReturn(HttpStatus.FORBIDDEN);
    }
}
