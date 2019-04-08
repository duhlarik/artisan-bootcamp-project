package com.pillar.cucumber;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.pillar.customer.Customer;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CustomerStepdefs {
    private String endpoint;
    private String dbUrl;
    private Integer id;
    private Map response;
    private final WebClient client;

    public CustomerStepdefs() {
        endpoint = System.getProperty("integration-endpoint", "http://localhost:8080");
        dbUrl = System.getProperty("integration-mysql", "jdbc:mysql://localhost:3316/cc_processing?useSSL=false");
        client = WebClient.create(endpoint);
    }

    @Given("a customer")
    public void aCustomer() {
        JdbcTemplate template = getJdbcTemplate();
        template.update("INSERT INTO customer SET name=?", "Test Customer");

        final List<Integer> customerIds = template.query("SELECT id FROM customer WHERE name=? LIMIT 1", new String[] { "Test Customer" },
                (ResultSet rs, int rowNum) -> rs.getInt("ID"));

        id = customerIds.get(0);
    }

    @When("I request it's information")
    public void iRequestItsInformation() {
        response = getObjectForUrl("/api/customer/" + id);
    }

    @Then("the name is {string}")
    public void theNameIs(String name) {
        assertEquals(response.get(Customer.NAME), name);
    }

    private Map getObjectForUrl(String uri) {
        return client
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    private JdbcTemplate getJdbcTemplate() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(dbUrl);
        dataSource.setUser("root");
        dataSource.setPassword("password");
        return new JdbcTemplate(dataSource);
    }
}
