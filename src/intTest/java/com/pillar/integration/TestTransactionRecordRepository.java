package com.pillar.integration;

import com.pillar.AccountApiController;
import com.pillar.account.Account;
import com.pillar.transaction.TransactionRecord;
import com.pillar.transaction.TransactionRecordRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestTransactionRecordRepository {
    private static final String TEST_CARDHOLDER_NAME = "Steve Goliath";
    private static final String TEST_CARDHOLDER_SSN = "123-45-6789";
    private static final String TEST_BUSINESS = "Target";
    private static final Double AMOUNT = 5.0;
    private static final Instant NOW = Instant.now();
    private static final boolean APPROVED = true;
    private static final boolean IS_CHARGE = false;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Account account;

    @Autowired
    private AccountApiController controller;

    @Test
    public void transactionFromRepositoryHasIsChargeSetToWhatWasSaved() {
        createAccount();
        boolean isCharge = false;

        TransactionRecord inputTxRecord = new TransactionRecord(5.0, Instant.now(), true, account, isCharge);
        TransactionRecord outputTxRecord = transactionRecordRepository.save(inputTxRecord);

        assertEquals(isCharge, outputTxRecord.isCharge());
    }

    @Test
    public void transactionFromRepositoryHasIsChargeSetToTrueWhenTransactionSavedHadIsChargeTrue() {
        createAccount();
        boolean isCharge = true;

        TransactionRecord inputTxRecord = new TransactionRecord(5.0, Instant.now(), true, account, isCharge);
        TransactionRecord outputTxRecord = transactionRecordRepository.save(inputTxRecord);

        assertEquals(isCharge, outputTxRecord.isCharge());
    }

    private ArrayList<TransactionRecord> list(TransactionRecord...records){
        ArrayList<TransactionRecord> returnValue = new ArrayList<>();
        Collections.addAll(returnValue, records);
        return returnValue;
    }

    private void createAccount() {
        final Map<String, String> params = new HashMap<>();
        params.put(AccountApiController.CARDHOLDER_NAME, TEST_CARDHOLDER_NAME);
        params.put(AccountApiController.CARDHOLDER_SSN, TEST_CARDHOLDER_SSN);
        params.put(AccountApiController.BUSINESS_NAME, TEST_BUSINESS);

        account = controller.create(params).getBody();
    }

    @After
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM transaction_record");
        jdbcTemplate.execute("DELETE FROM account");
        jdbcTemplate.execute("DELETE FROM cardholder");
        jdbcTemplate.execute("DELETE FROM customer");
    }
}