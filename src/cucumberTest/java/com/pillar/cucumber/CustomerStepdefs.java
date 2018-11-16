package com.pillar.cucumber;

import com.mysql.cj.jdbc.MysqlDataSource;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.client.WebClient;

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
        dbUrl = System.getProperty("integration-mysql", "jdbc:mysql://localhost:3306/cc_processing");
        client = WebClient.create(endpoint);
    }

    @Given("a customer")
    public void aCustomer() {
        JdbcTemplate template = getJdbcTemplate();
        template.update("INSERT INTO customer SET id=?, name=?", 1, "Test Customer");
    }

    @When("I request it's information")
    public void iRequestItsInformation() {
        response = getObjectForUrl("/api/customer/1", Map.class);
    }

    @Then("the name is {string}")
    public void theNameIs(String name) {
        assertEquals(response.get("name"), name);
    }

    private Map getObjectForUrl(String uri, Class<Map> bodyType) {
        return client
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(bodyType)
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