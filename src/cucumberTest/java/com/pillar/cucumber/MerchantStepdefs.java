package com.pillar.cucumber;

import com.mysql.cj.jdbc.MysqlDataSource;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MerchantStepdefs {
    private String endpoint;
    private String dbUrl;
    private Integer id;
    private Map response;

    public MerchantStepdefs() {
        endpoint = System.getProperty("integration-endpoint", "http://localhost:8080");
        dbUrl = System.getProperty("integration-mysql", "jdbc:mysql://localhost:3306/cc_processing");
    }

    @Given("a merchant")
    public void aMerchant() {
        // Insert into db
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(dbUrl);
        dataSource.setUser("root");
        dataSource.setPassword("password");
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update("INSERT INTO merchant SET id=?, name=?", 1, "Test Merchant");
    }

    @When("I request it's information")
    public void iRequestItsInformation() {
        // Query URL for merchant with id from previously inserted
        WebClient client = WebClient.create(endpoint);
        response = client
                .get()
                .uri("/api/merchant/" + id)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    @Then("the name is {string}")
    public void theNameIs(String name) {
        // Response object name matches
        assertEquals(response.get("name"), name);
    }
}
