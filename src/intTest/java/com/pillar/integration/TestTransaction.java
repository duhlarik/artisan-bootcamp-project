package com.pillar.integration;

import com.pillar.BankService;
import com.pillar.account.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestTransaction {

    @Autowired
    AccountRepository accountRepository;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    BankService bankService;

    @Test
    @Transactional
    public void testEmptyAccountTableHasNoRecords() {

    }
}
