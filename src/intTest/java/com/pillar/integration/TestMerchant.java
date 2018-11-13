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
        jdbcTemplate.update("INSERT INTO merchant SET id=?, name=?", 1, "Test Merchant");

        Merchant merchant = merchantRepository.getOne(1);

        assertEquals("Test Merchant", merchant.getName());
    }

    @Test
    public void testApiReturnsTestMerchantWhenQueriedForId1() {
        jdbcTemplate.update("INSERT INTO merchant SET id=?, name=?", 1, "Test Merchant");

        Merchant test_merchant = new Merchant(1, "Test Merchant");

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
}
