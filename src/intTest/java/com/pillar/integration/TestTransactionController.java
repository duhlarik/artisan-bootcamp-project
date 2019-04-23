package com.pillar.integration;

import com.pillar.AccountApiController;
import com.pillar.TransactionController;
import com.pillar.account.Account;
import com.pillar.transaction.Transaction;
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

import static com.pillar.TransactionController.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestTransactionController {
    private static final String TEST_CARDHOLDER_NAME = "Steve Goliath";
    private static final String TEST_CARDHOLDER_SSN = "123-45-6789";
    private static final String TEST_BUSINESS = "Target";
    public static final Instant NOW = Instant.now();
    public static final String RETAILER = "TARGET";

    private Account account;

    private WebClient client;

    @Autowired
    private AccountApiController accountApiController;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TransactionController transactionController;

    @Before
    public void setUp() {
        String defaultEndpoint = "http://localhost:5000";
        client = WebClient.create(System.getProperty("fake-bank-service", defaultEndpoint));
    }

    @Test
    public void testExternalApiEndpointIsReturnsExpectedResult() {
        Transaction transaction = new Transaction("1234", 5.00, new Date(), 1, 10000);
        final ClientResponse response = client
                .post()
                .uri("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transaction))
                .exchange()
                .block();
        assertEquals(HttpStatus.CREATED, response.statusCode());
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
    }

    private void createAccount() {
        final Map<String, String> params = new HashMap<>();
        params.put(AccountApiController.CARDHOLDER_NAME, TEST_CARDHOLDER_NAME);
        params.put(AccountApiController.CARDHOLDER_SSN, TEST_CARDHOLDER_SSN);
        params.put(AccountApiController.BUSINESS_NAME, TEST_BUSINESS);

        account = accountApiController.create(params).getBody();
    }

    @Test
    public void createDBTransactionReturnsSavedTransactionGivenTransactionBelowCreditLimit() {
        createAccount();
        TransactionRequest request = new TransactionRequest(account.getCreditCardNumber(), account.getCreditLimit() - 1.0, NOW, RETAILER);

        ResponseEntity<TransactionResponse> entity = transactionController.createDbTransaction(request);

        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
    }

    @Test
    public void createDBTransactionReturns403ForbiddenGivenTransactionAboveCreditLimit() {
        createAccount();
        TransactionRequest request = new TransactionRequest(account.getCreditCardNumber(), account.getCreditLimit() + 1.0, NOW, RETAILER);

        ResponseEntity<TransactionResponse> entity = transactionController.createDbTransaction(request);

        assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
    }

    @Test
    public void createDBTransactionReturns403ForbiddenWhenGivenTwoTransactionEachWithAmountOfCreditLimit() {
        createAccount();
        TransactionRequest request = new TransactionRequest(account.getCreditCardNumber(), new Double(account.getCreditLimit()), NOW, RETAILER);
        transactionController.createDbTransaction(request);

        ResponseEntity<TransactionResponse> entity = transactionController.createDbTransaction(request);

        assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
    }

    @After
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM transaction_record");
        jdbcTemplate.execute("DELETE FROM account");
        jdbcTemplate.execute("DELETE FROM cardholder");
        jdbcTemplate.execute("DELETE FROM customer");
    }
}
