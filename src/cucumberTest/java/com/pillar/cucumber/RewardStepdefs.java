package com.pillar.cucumber;

import com.pillar.AccountApiController;
import com.pillar.TransactionController;
import com.pillar.account.Account;
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
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class RewardStepdefs {

    private static final String SOME_RETAILER = "GRIFTERS";
    private Double rewardsBalance;
    private WebClient client;
    private HttpStatus creationResponseCode;
    private Account account;

    public RewardStepdefs() {
        final String endpoint = System.getProperty("integration-endpoint", "http://localhost:8080");
        client = WebClient.create(endpoint);
        account = createAccount();
    }


    @Given("a retailer offering a reward programme")
    public void aRetailerOfferingARewardProgramme() {
        creationResponseCode = createRewardsProgramme(SOME_RETAILER);
    }

    @Given("the retailer TARGET offering 1% rewards")
    public void theRetailerTARGETOffering1PercentRewards() {
        createRewardsProgramme("TARGET");
    }

    @Given("the retailer BESTBUY offering 1% rewards")
    public void theRetailerBESTBUYOffering1PercentRewards(){
        createRewardsProgramme("BESTBUY");
    }

    @Given("an account with a charge balance of 10 at TARGET")
    public void anAccountWithChargeBalanceOf10AtTARGET() {
        TransactionController.TransactionRequest transaction = new TransactionController.TransactionRequest(account.getCreditCardNumber(), 10.0, Instant.now(), "TARGET", true);

        postApi(transaction, "/transaction/createv2");
    }

    @When("someone requests the TARGET rewards balance")
    public void someoneRequestsTheTARGETRewardsBalance() {
        ClientResponse response = client
                .get()
                .uri("/api/account/" + account.getCreditCardNumber() + "/rewards/TARGET")
                .exchange()
                .block();

        assertEquals(HttpStatus.OK, response.statusCode());
        rewardsBalance = response.bodyToMono(Double.class).block();
    }

    @Then("the reward programme is created")
    public void theRewardProgrammeIsCreated() {
        assertEquals(HttpStatus.CREATED, creationResponseCode);
    }


    @Then("they see a balance of {double}")
    public void theySeeABalanceOf(Double double1) {
        assertEquals(double1, rewardsBalance, TransactionRecord.DELTA);
    }

    private HttpStatus createRewardsProgramme(String retailer) {
        return postApi(1.0, "/api/rewards/" + retailer).statusCode();
    }

    private ClientResponse postApi(Object payload, String uri) {
        return client
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(payload))
                .exchange()
                .block();
    }

    private Account createAccount() {
        final HashMap<String, String> payload = new HashMap<>();
        payload.put(AccountApiController.CARDHOLDER_NAME, "Yahoo");
        payload.put(AccountApiController.CARDHOLDER_SSN, "123");
        payload.put(AccountApiController.BUSINESS_NAME, "Flintrock");

        ClientResponse response = client.post()
                .uri(AccountApiController.ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(payload))
                .exchange()
                .block();
        assertEquals(HttpStatus.CREATED, response.statusCode());
        return response.bodyToMono(Account.class)
                .block();
    }

}
