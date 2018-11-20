package com.pillar.integration;

import com.pillar.AccountApiController;
import com.pillar.account.AccountRepository;
import com.pillar.cardholder.CardholderRepository;
import com.pillar.customer.CustomerRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestAccount {
    private static final String TEST_CARDHOLDER_NAME = "Steve Goliath";
    private static final String TEST_CARDHOLDER_SSN = "123-45-6789";
    private static final String TEST_BUSINESS = "Target";

    @Autowired
    private AccountApiController controller;

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

    private void createAccount() {
        final Map<String, String> params = new HashMap<>();
        params.put(AccountApiController.CARDHOLDER_NAME, TEST_CARDHOLDER_NAME);
        params.put(AccountApiController.CARDHOLDER_SSN, TEST_CARDHOLDER_SSN);
        params.put(AccountApiController.BUSINESS_NAME, TEST_BUSINESS);

        controller.create(params);
    }

    @After
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM account");
        jdbcTemplate.execute("DELETE FROM cardholder");
        jdbcTemplate.execute("DELETE FROM customer");
    }
}
