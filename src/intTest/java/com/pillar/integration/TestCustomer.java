package com.pillar.integration;

import com.pillar.merchant.Customer;
import com.pillar.merchant.CustomerRepository;
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
public class TestCustomer {

    private static final String TEST_CUSTOMER_NAME = "Test Customer";
    private static final int TEST_CUSTOMER_ID = 1;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void testEmptyMerchantTableHasNoRecords() {
        List<Customer> customers = customerRepository.findAll();

        assertEquals(0, customers.size());
    }

    @Test
    @Transactional
    public void testTableWithOneNamedMerchantReturnsFromRepository() {
        insertTestMerchant();

        Customer customer = customerRepository.getOne(TEST_CUSTOMER_ID);

        assertEquals(TEST_CUSTOMER_NAME, customer.getName());
    }

    @Test
    public void testApiReturnsTestMerchantWhenQueriedForId1() {
        insertTestMerchant();

        Customer test_customer = new Customer(TEST_CUSTOMER_ID, TEST_CUSTOMER_NAME);

        WebClient client = WebClient.create("http://localhost:" + randomServerPort);
        Customer response = client
                .get()
                .uri("/api/merchant/1")
                .retrieve()
                .bodyToMono(Customer.class)
                .block();

        assertEquals(test_customer, response);
    }

    @After
    public void tearDown() {
        jdbcTemplate.update("TRUNCATE customer");
    }

    private int insertTestMerchant() {
        return jdbcTemplate.update("INSERT INTO customer SET id=?, name=?", TEST_CUSTOMER_ID, TEST_CUSTOMER_NAME);
    }
}
