package com.pillar.integration;

import com.pillar.merchant.Merchant;
import com.pillar.merchant.MerchantRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestMerchant {

    private static final String TEST_MERCHANT_NAME = "Test Merchant";
    private static final int TEST_MERCHANT_ID = 1;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void testEmptyMerchantTableHasNoRecords() {
        List<Merchant> merchants = merchantRepository.findAll();

        assertEquals(0, merchants.size());
    }

    @Test
    @Transactional
    public void testTableWithOneNamedMerchantReturnsFromRepository() {
        insertTestMerchant();

        Merchant merchant = merchantRepository.getOne(TEST_MERCHANT_ID);

        assertEquals(TEST_MERCHANT_NAME, merchant.getName());
    }

    @Test
    public void testApiReturnsTestMerchantWhenQueriedForId1() {
        insertTestMerchant();

        Merchant test_merchant = new Merchant(TEST_MERCHANT_ID, TEST_MERCHANT_NAME);

        WebClient client = WebClient.create("http://localhost:" + randomServerPort);
        Merchant response = client
                .get()
                .uri("/api/merchant/1")
                .retrieve()
                .bodyToMono(Merchant.class)
                .block();

        assertEquals(test_merchant, response);
    }

    @After
    public void tearDown() {
        jdbcTemplate.update("TRUNCATE merchant");
    }

    private int insertTestMerchant() {
        return jdbcTemplate.update("INSERT INTO merchant SET id=?, name=?", TEST_MERCHANT_ID, TEST_MERCHANT_NAME);
    }
}
