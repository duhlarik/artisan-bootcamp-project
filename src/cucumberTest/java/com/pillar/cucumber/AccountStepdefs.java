package com.pillar.cucumber;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.pillar.AccountApiController;
import com.pillar.account.Account;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccountStepdefs {
    private final WebClient client;

    private String cardholderName;
    private String ssn;
    private String businessName;

    private HttpStatus status;
    private Map body;

    public AccountStepdefs() {
        final String endpoint = System.getProperty("integration-endpoint", "http://localhost:8080");
        client = WebClient.create(endpoint);
    }

    @Given("a cardholder Name: {string}, SSN: {string}, Business Name: {string}")
    public void aCardHolder(String cardholderName, String ssn, String businessName) {
        this.cardholderName = cardholderName;
        this.ssn = ssn;
        this.businessName = businessName;
    }

    @When("a request is made to create an account for this cardholder")
    public void aRequestIsMadeToCreateAnAccount() {
        requestCreateAccount();
    }

    @And("a second request is made to create an account for this cardholder at the same business customer")
    public void aSecondRequestIsMadeToCreateAnAccount() {
        requestCreateAccount();
    }

    @Then("a new account is created and a new card number is issued to that account and returned")
    public void aNewAccountIsCreated() {
        assertEquals(HttpStatus.CREATED, status);
        assertTrue(body.containsKey(Account.CREDIT_CARD_NUMBER));
        assertNotNull(body.get(Account.CREDIT_CARD_NUMBER));
    }

    @And("a credit limit of 10,000 is assigned")
    public void aCreditLimitIsAssigned() {
        assertTrue(body.containsKey(Account.CREDIT_LIMIT));
        assertEquals(10000, body.get(Account.CREDIT_LIMIT));
    }

    @Then("the request should fail and return an Error")
    public void requestShouldFail() {
        assertEquals(HttpStatus.FORBIDDEN, status);
    }

    private void requestCreateAccount(){
        final HashMap<String, String> payload = new HashMap<>();
        payload.put(AccountApiController.CARDHOLDER_NAME, cardholderName);
        payload.put(AccountApiController.CARDHOLDER_SSN, ssn);
        payload.put(AccountApiController.BUSINESS_NAME, businessName);

        final ClientResponse response = client
                .post()
                .uri(AccountApiController.ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(payload))
                .exchange()
                .block();

        status = response.statusCode();
        body = response.bodyToMono(Map.class).block();
    }

    @After
    public void tearDown() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3316/cc_processing");
        dataSource.setUser("root");
        dataSource.setPassword("password");

        final JdbcTemplate template = new JdbcTemplate(dataSource);
        template.execute("DELETE FROM account");
        template.execute("DELETE FROM customer");
        template.execute("DELETE FROM cardholder");
    }
}
