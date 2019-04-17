package com.pillar.integration;

import com.pillar.AccountApiController;
import com.pillar.TransactionController;
import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import com.pillar.cardholder.CardholderRepository;
import com.pillar.customer.CustomerRepository;
import com.pillar.transaction.TransactionRecordRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.pillar.TransactionController.*;
import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestAccountAPIController {
    private static final String TEST_CARDHOLDER_NAME = "Steve Goliath";
    private static final String TEST_CARDHOLDER_SSN = "123-45-6789";
    private static final String TEST_BUSINESS = "Target";

    private Account account;

    @Autowired
    private AccountApiController controller;

    @Autowired
    private TransactionController transactionController;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CardholderRepository cardholderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void createsNewCardholder() {
        createAccount();

        assertEquals(1, cardholderRepository.findBySsn(TEST_CARDHOLDER_SSN).size());
    }

    @Test
    public void doesNotDuplicateCardholder() {
        createAccount();
        createAccount();

        assertEquals(1, cardholderRepository.findBySsn(TEST_CARDHOLDER_SSN).size());
    }

    @Test
    public void createsNewCustomer() {
        createAccount();

        assertEquals(1, customerRepository.findByName(TEST_BUSINESS).size());
    }

    @Test
    public void doesNotDuplicateCustomer() {
        createAccount();
        createAccount();

        assertEquals(1, customerRepository.findByName(TEST_BUSINESS).size());
    }

    @Test
    public void createsNewAccount() {
        createAccount();

        assertEquals(1, accountRepository.findAll().size());
    }

    @Test
    public void findsAccountByCardNumber() {
        createAccount();
        assertNotNull(accountRepository.findOneByCreditCardNumber(account.getCreditCardNumber()));
    }

    @Test
    public void cancelsAccountByCardNumber() {
        createAccount();
        controller.cancelAccount(account.getCreditCardNumber());
        Optional<Account> maybeChangedAccount = accountRepository.findOneByCreditCardNumber(account.getCreditCardNumber());
        if (maybeChangedAccount.isPresent()) {
            Account changedAccount = maybeChangedAccount.get();
            assertFalse(changedAccount.isActive());
        } else {
            fail();
        }
    }

    @Test
    public void getAccountReturnsOKResponseGivenValidCreditCardNumber() {
        createAccount();
        String creditCardNumber = account.getCreditCardNumber();

        ResponseEntity<?> responseEntity = controller.getAccount(creditCardNumber);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    private void createAccount() {
        final Map<String, String> params = new HashMap<>();
        params.put(AccountApiController.CARDHOLDER_NAME, TEST_CARDHOLDER_NAME);
        params.put(AccountApiController.CARDHOLDER_SSN, TEST_CARDHOLDER_SSN);
        params.put(AccountApiController.BUSINESS_NAME, TEST_BUSINESS);

        account = controller.create(params).getBody();
    }

    @Test
    public void getAccountReturnsAccountWithTransactionBalanceEqualToChargeAmountGivenChargeTransactionOfChargeAmount() {
        createAccount();
        double chargeAmount = 5.0;
        String cardNumber = account.getCreditCardNumber();
        transactionController.createDbTransaction(new TransactionRequest(cardNumber, chargeAmount, Instant.now(), "" ));

        Account responseAccount = controller.getAccount(cardNumber).getBody();

        double expected = responseAccount.getTransactionBalance();
        assertEquals(chargeAmount, expected, 0.001);
    }

    @After
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM transaction_record");
        jdbcTemplate.execute("DELETE FROM account");
        jdbcTemplate.execute("DELETE FROM cardholder");
        jdbcTemplate.execute("DELETE FROM customer");
    }
}
