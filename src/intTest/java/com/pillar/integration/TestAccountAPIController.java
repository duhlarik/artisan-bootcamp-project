package com.pillar.integration;

import com.pillar.AccountApiController;
import com.pillar.rewardsProgramme.RewardsProgramme;
import com.pillar.rewardsProgramme.RewardsProgrammeRepository;
import com.pillar.TransactionController;
import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import com.pillar.cardholder.CardholderRepository;
import com.pillar.customer.CustomerRepository;
import com.pillar.transaction.TransactionRecord;
import org.junit.After;
import org.junit.Before;
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

import static com.pillar.TransactionController.TransactionRequest;
import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestAccountAPIController {
    private static final String TEST_CARDHOLDER_NAME = "Steve Goliath";
    private static final String TEST_CARDHOLDER_SSN = "123-45-6789";
    private static final String TEST_BUSINESS = "Target";
    private static final String RETAILER = "";
    private static final Instant NOW = Instant.now();

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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RewardsProgrammeRepository rewardsProgrammeRepository;


    @Before
    public void setUp() throws Exception {
        createAccount();
    }

    @Test
    public void createsNewCardholder() {
        assertEquals(1, cardholderRepository.findBySsn(TEST_CARDHOLDER_SSN).size());
    }

    @Test
    public void doesNotDuplicateCardholder() {
        createAccount();

        assertEquals(1, cardholderRepository.findBySsn(TEST_CARDHOLDER_SSN).size());
    }

    @Test
    public void createsNewCustomer() {
        assertEquals(1, customerRepository.findByName(TEST_BUSINESS).size());
    }

    @Test
    public void doesNotDuplicateCustomer() {
        createAccount();

        assertEquals(1, customerRepository.findByName(TEST_BUSINESS).size());
    }

    @Test
    public void createsNewAccount() {
        assertEquals(1, accountRepository.findAll().size());
    }

    @Test
    public void findsAccountByCardNumber() {
        assertNotNull(accountRepository.findOneByCreditCardNumber(account.getCreditCardNumber()));
    }

    @Test
    public void cancelsAccountByCardNumber() {
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
        double chargeAmount = 5.0;
        String creditCardNumber = account.getCreditCardNumber();
        transactionController.createDbTransaction(new TransactionRequest(creditCardNumber, chargeAmount, NOW, RETAILER ));

        Account responseAccount = controller.getAccount(creditCardNumber).getBody();

        assertEquals(chargeAmount, responseAccount.getTransactionBalance(), TransactionRecord.DELTA);
    }

    @Test
    public void getAccountReturnsAccountWithTransactionBalanceEqualToSumOfChargesGiven2Transactions() {
        double charge1 = 1.0;
        double charge2 = 2.0;
        String creditCardNumber = account.getCreditCardNumber();
        createChargeTransaction(creditCardNumber, charge1);
        createChargeTransaction(creditCardNumber, charge2);

        ResponseEntity<Account> entity = controller.getAccount(creditCardNumber);

        assertEquals(charge1+charge2, entity.getBody().getTransactionBalance(), TransactionRecord.DELTA);
    }

    @Test
    public void getAccountReturnsAccountWithChargeBalanceEqualToZeroWhenNoTransactionsGiven() {
        String creditCardNumber = account.getCreditCardNumber();

        ResponseEntity<Account> entity = controller.getAccount(creditCardNumber);

        assertEquals(0, entity.getBody().getChargeBalance(), TransactionRecord.DELTA);
    }

    @Test
    public void getAccountReturnsAccountWithChargeBalanceEqualToAmountOfChargeTransactionGiven() {
        String creditCardNumber = account.getCreditCardNumber();
        double chargeAmount = 2.0;
        createChargeTransaction(creditCardNumber, chargeAmount);

        ResponseEntity<Account> entity = controller.getAccount(creditCardNumber);

        assertEquals(chargeAmount, entity.getBody().getChargeBalance(), TransactionRecord.DELTA);
    }

    @Test
    public void getRewardsBalanceReturns1GivenTxBalanceOf100_AndRewardPercentageOf1Percent() {
        rewardsProgrammeRepository.save(new RewardsProgramme(RETAILER, 1));

        ResponseEntity<Double> entity = controller.getRewardsBalance(account.getCreditCardNumber(), RETAILER);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(1, entity.getBody(), 0.001);
    }

    @Test
    public void getRewardsBalanceReturns404NotFoundGivenRetailerWithoutRewardsProgramme() {
        ResponseEntity<Double> entity = controller.getRewardsBalance(account.getCreditCardNumber(), "NONEXISTENT_RETAILER");

        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
    }

    private void createChargeTransaction(String creditCardNumber, double amount) {
        transactionController.createDbTransaction(new TransactionRequest(creditCardNumber, amount, Instant.now(), RETAILER));
    }

    @After
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM transaction_record");
        jdbcTemplate.execute("DELETE FROM account");
        jdbcTemplate.execute("DELETE FROM cardholder");
        jdbcTemplate.execute("DELETE FROM customer");
        jdbcTemplate.execute("DELETE FROM rewards_programme");
    }
}
