package com.pillar.cucumber;

import com.pillar.account.Account;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Date;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.Assert.assertEquals;

public class TransactionStepdefs {
    private final WebClient client;
    private final WebClient fakeServiceClient;

    private Account account;
    private ClientResponse response;

    public TransactionStepdefs() {
        String endpoint = System.getProperty("integration-endpoint", "http://localhost:8080");
        String fakeServiceUrl = System.getProperty("fake-service-base-url", "http://localhost:5000");
        client = WebClient.create(endpoint);
        fakeServiceClient = WebClient.create(fakeServiceUrl);
    }

    @Given("an active card and account")
    public void anActiveCardAndAccount(){
        final HashMap<String, String> payload = new HashMap<>();
        payload.put("cardholderName", "Steve Goliath");
        payload.put("ssn", "123-45-6788");
        payload.put("businessName", "Target");

        final ClientResponse response = client
                .post()
                .uri("/api/account")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(payload))
                .exchange()
                .block();

        account = response.bodyToMono(Account.class).block();
    }

    @When("a purchase transaction request is made,")
    public void aPurchaseTransactionRequestIsMade() {
        HashMap<String, Object> transaction = new HashMap<>();
        transaction.put("cardNumber", account.getCreditCardNumber());
        transaction.put("amount", 2.00);
        transaction.put("creditLimit", account.getCreditLimit());
        transaction.put("dateOfTransaction", new Date());
        transaction.put("customerId", account.getCustomer().getId());

        response = fakeServiceClient
                .post()
                .uri("/api/makeTransaction")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transaction))
                .exchange()
                .block();
    }

    @Then("success response is returned")
    public void successResponseIsReturned() {
        assertEquals(HttpStatus.CREATED, response.statusCode());
    }
}
