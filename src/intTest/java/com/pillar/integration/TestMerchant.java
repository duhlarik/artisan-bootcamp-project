package com.pillar.integration;

import com.pillar.merchant.Merchant;
import com.pillar.merchant.MerchantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Transactional
@Rollback
@RunWith(SpringRunner.class)
public class TestMerchant {

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void testEmptyMerchantTableHasNoRecords() {
        List<Merchant> merchants = merchantRepository.findAll();

        assertEquals(0, merchants.size());
    }

    @Test
    public void testTableWithOneNamedMerchantReturnsFromRepository() {
        Merchant input = new Merchant(1, "Test Merchant");
        merchantRepository.save(input);

        Merchant merchant = merchantRepository.getOne(1);

        assertEquals("Test Merchant", merchant.getName());
    }
}
