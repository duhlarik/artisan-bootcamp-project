package com.pillar.cucumber;

import com.pillar.AccountApiController;
import com.pillar.TransactionController;
import com.pillar.account.Account;
import com.pillar.transaction.TransactionBankRequest;
import com.pillar.transaction.TransactionRecord;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TransactionStepdefs {
    private final WebClient client;

    private Account account;
    private ClientResponse response;

    public TransactionStepdefs() {
        String endpoint = System.getProperty("integration-endpoint", "http://localhost:8080");
        client = WebClient.create(endpoint);
    }

    @Given("an active card and account")
    public void anActiveCardAndAccount() {
        final HashMap<String, String> payload = new HashMap<>();
        payload.put(AccountApiController.CARDHOLDER_NAME, "Steve Goliath");
        payload.put(AccountApiController.CARDHOLDER_SSN, "123-45-6788");
        payload.put(AccountApiController.BUSINESS_NAME, "Target");

        final ClientResponse response = client
                .post()
                .uri(AccountApiController.ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(payload))
                .exchange()
                .block();

        account = response.bodyToMono(Account.class).block();
    }

    @Given("a charge balance of $500")
    public void aChargeBalanceOf500(){
        double amount = 500;
        TransactionController.TransactionRequest transaction = new TransactionController.TransactionRequest(account.getCreditCardNumber(), amount, Instant.now(), "Electronics XYZ");
        response = client
                .post()
                .uri("/transaction/createv2")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transaction))
                .exchange()
                .block();
    }

    @When("a payment transaction request is made of $100")
    public void aPaymentTransactionRequestIsMadeOf100(){
        double amount = 100;
        TransactionController.TransactionRequest transaction = new TransactionController.TransactionRequest(account.getCreditCardNumber(), amount, Instant.now(), "Electronics XYZ", false);
        response = client
                .post()
                .uri("/transaction/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transaction))
                .exchange()
                .block();
        assertEquals(HttpStatus.CREATED, response.statusCode());
    }

    @When("a purchase transaction request is made,")
    public void aPurchaseTransactionRequestIsMade() {
        TransactionBankRequest transactionBankRequest = new TransactionBankRequest(account.getCreditCardNumber(), 2.00, new Date(), 1, account.getCreditLimit());
        response = client
                .post()
                .uri("/transaction/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transactionBankRequest))
                .exchange()
                .block();
    }

    @When("a v2 purchase transaction request is made,")
    public void aV2PurchaseTransactionRequestIsMade() {
        TransactionController.TransactionRequest transaction = new TransactionController.TransactionRequest(account.getCreditCardNumber(), 2.00, Instant.now(), "Electronics XYZ");
        response = client
                .post()
                .uri("/transaction/createv2")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transaction))
                .exchange()
                .block();
    }

    @Then("success response is returned")
    public void successResponseIsReturned() {
        assertEquals(HttpStatus.CREATED, response.statusCode());
    }

    @Then("The charge Balance is $400")
    public void theChargeBalanceIs400(){
        final ClientResponse response = client
                .get()
                .uri("/api/account/" + account.getCreditCardNumber())
                .exchange()
                .block();

        account = response.bodyToMono(Account.class).block();

        double chargeBalance = account.getChargeBalance();
        assertEquals(400, chargeBalance, TransactionRecord.DELTA);
    }
}
