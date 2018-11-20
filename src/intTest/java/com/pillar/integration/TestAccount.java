package com.pillar.integration;

import com.pillar.AccountApiController;
import com.pillar.account.AccountRepository;
import com.pillar.cardholder.CardholderRepository;
import com.pillar.customer.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        params.put("cardHolderName", TEST_CARDHOLDER_NAME);
        params.put("ssn", TEST_CARDHOLDER_SSN);
        params.put("businessName", TEST_BUSINESS);

        controller.create(params).getBody();
    }
}
