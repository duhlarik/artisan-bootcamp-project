package com.pillar.integration;

import com.pillar.AccountApiController;
import com.pillar.TransactionController;
import com.pillar.account.Account;
import com.pillar.transaction.TransactionBankRequest;
import com.pillar.transaction.TransactionRecord;
import com.pillar.transaction.TransactionRecordRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.pillar.TransactionController.TransactionRequest;
import static com.pillar.TransactionController.TransactionResponse;
import static com.pillar.transaction.TransactionRecord.DELTA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestTransactionController {
    private static final String TEST_CARDHOLDER_NAME = "Steve Goliath";
    private static final String TEST_CARDHOLDER_SSN = "123-45-6789";
    private static final String TEST_BUSINESS = "Target";
    private static final Instant NOW = Instant.now();

    private Account account;

    private WebClient client;

    @Autowired
    private AccountApiController accountApiController;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionController transactionController;

    @Before
    public void setUp() {
        String defaultEndpoint = "http://localhost:5000";
        client = WebClient.create(System.getProperty("fake-bank-service", defaultEndpoint));
    }

    @Test
    public void testExternalApiEndpointIsReturnsExpectedResult() {
        TransactionBankRequest transactionBankRequest = new TransactionBankRequest("1234", 5.00, new Date(), 1, 10000);
        final ClientResponse response = client
                .post()
                .uri("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transactionBankRequest))
                .exchange()
                .block();
        assertEquals(HttpStatus.CREATED, response.statusCode());
    }

    @Test
    public void returns201CreatedIfSumOfBalanceAndAmountEqualToCreditLimit() {
        createAccount();
        Double amount = 10.0;
        Double balance = account.getCreditLimit() - amount;
        transactionRecordRepository.save(new TransactionRecord(balance, Instant.now(), true, account));
        Instant date = Instant.now();
        TransactionRequest request = new TransactionRequest(account.getCreditCardNumber(), amount, date, "RETAILER");

        ResponseEntity<TransactionResponse> response = transactionController.createDbTransaction(request);

        HttpStatus actual = response.getStatusCode();
        assertEquals(HttpStatus.CREATED, actual);
    }

    @Test
    public void testTransactionOf100IsRecordedAgainstTheTestCustomerAccount() {
        createAccount();
        Instant dateOfTransaction = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        TransactionRequest request = new TransactionRequest(account.getCreditCardNumber(), 100.0, dateOfTransaction, "Electronics XYZ");
        ResponseEntity<TransactionResponse> response = transactionController.createDbTransaction(request);

        TransactionResponse responseBody = response.getBody();

        TransactionRecord dbTransaction = transactionRecordRepository.findById(responseBody.getTransactionId()).get();
        assertEquals(request.getAmount(), dbTransaction.getAmount());
        assertEquals(request.getDateOfTransaction(), dbTransaction.getDateOfTransaction());
        assertEquals(account, dbTransaction.getAccount());
        assertTrue(responseBody.isApproved());
        assertTrue(dbTransaction.isApproved());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void givenAPaymentTransactionOf0ChargeBalanceRemainsTheSame() {
        createAccount();
        double chargeBalanceBefore = account.getChargeBalance();
        double amount = 0;

        TransactionRequest request = new TransactionRequest(account.getCreditCardNumber(), amount, NOW, TEST_BUSINESS, false);
        ResponseEntity<TransactionResponse> response = transactionController.createPaymentTransaction(request);
        ResponseEntity<Account> accountResponseEntity = accountApiController.getAccount(account.getCreditCardNumber());
        double chargeBalanceAfter = accountResponseEntity.getBody().getChargeBalance();

        assertEquals(chargeBalanceBefore, chargeBalanceAfter, DELTA);
    }

    @Test
    public void givenAPaymentTransactionChargeBalanceIsReducedByPaymentAmount() {
        createAccount();
        double chargeBalanceBefore = account.getChargeBalance();
        double paymentAmount = 5.0;
        TransactionRequest request = new TransactionRequest(account.getCreditCardNumber(), paymentAmount, NOW, TEST_BUSINESS, false);

        TransactionResponse txResponse = transactionController.createPaymentTransaction(request).getBody();

        ResponseEntity<Account> accountResponseEntity = accountApiController.getAccount(account.getCreditCardNumber());
        double actual = accountResponseEntity.getBody().getChargeBalance();
        assertEquals(chargeBalanceBefore - paymentAmount, actual, DELTA);
        assertTrue(txResponse.isApproved());
        assertTrue(txResponse.getTransactionId() > 0);
    }

    private void createAccount() {
        final Map<String, String> params = new HashMap<>();
        params.put(AccountApiController.CARDHOLDER_NAME, TEST_CARDHOLDER_NAME);
        params.put(AccountApiController.CARDHOLDER_SSN, TEST_CARDHOLDER_SSN);
        params.put(AccountApiController.BUSINESS_NAME, TEST_BUSINESS);

        account = accountApiController.create(params).getBody();
    }

    @After
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM transaction_record");
        jdbcTemplate.execute("DELETE FROM account");
        jdbcTemplate.execute("DELETE FROM cardholder");
        jdbcTemplate.execute("DELETE FROM customer");
    }
}
