package com.pillar.cucumber;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    private String cardHolderName;
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
        this.cardHolderName = cardholderName;
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
        assertTrue(body.containsKey("cardNumber"));
        assertNotNull(body.get("cardNumber"));
    }

    @And("a credit limit of 10,000 is assigned")
    public void aCreditLimitIsAssigned() {
        assertTrue(body.containsKey("creditLimit"));
        assertEquals(10000, body.get("creditLimit"));
    }

    @Then("the request should fail and return an Error")
    public void requestShouldFail() {
        throw new PendingException();
//      assertEquals(HttpStatus.FORBIDDEN, status);
    }

    private void requestCreateAccount(){
        final HashMap<String, String> payload = new HashMap<>();
        payload.put("card_holder_name", cardHolderName);
        payload.put("ssn", ssn);
        payload.put("business_name", businessName);

        final ClientResponse response = client
                .post()
                .uri("/api/account")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(payload))
                .exchange()
                .block();

        status = response.statusCode();
        body = response.bodyToMono(Map.class).block();
    }
}
