package com.pillar.cucumber;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.pillar.TransactionController;
import com.pillar.account.Account;
import com.pillar.cardholder.Cardholder;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class CreditLimitCheckStepdefs {
    public static final int CREDIT_LIMIT = 10000;
    private final String dbUrl;
    private ClientResponse response;
    private WebClient client;
    private Account account;


    public CreditLimitCheckStepdefs() {
        String endpoint = System.getProperty("integration-endpoint", "http://localhost:8080");
        client = WebClient.create(endpoint);

        dbUrl = System.getProperty("integration-mysql", "jdbc:mysql://localhost:3316/cc_processing?useSSL=false");

    }

    @Given("a credit card with balance below the credit limit")
    public void aCreditCardWithBalanceBelowTheCreditLimit() {
        final String cardNumber = "111111222222333333444444555555666666";
        Cardholder cardholder = new Cardholder(1, "123-45-6788", "Steve Goliath");
        account = new Account(1, CREDIT_LIMIT, cardNumber, true, cardholder);
        JdbcTemplate template = getJdbcTemplate();
        template.update("INSERT INTO customer SET id=?, name=?", 1, "Target");
        template.update("INSERT INTO cardholder SET id=?, ssn=?, name=?",
                1, cardholder.getSsn(), cardholder.getName());
        template.update("INSERT INTO account SET id=?, cardholder_id=?, customer_id=?, credit_card_number=?, credit_limit=?, active=?",
                1, 1, 1, account.getCreditCardNumber(), account.getCreditLimit(), account.isActive());
    }


    @When("a transaction request is made that would make the balance exceed the credit limit")
    public void aTransactionRequestIsMadeThatWouldMakeTheBalanceExceedTheCreditLimit() {
        createTransactionAndSetResponse(CREDIT_LIMIT + 1);
    }

    @Then("the transaction is denied")
    public void theTransactionIsDenied() {
        assertEquals(HttpStatus.FORBIDDEN, response.statusCode());
    }

    @When("a transaction request is made that keeps the balance below the credit limit")
    public void aTransactionRequestIsMadeThatKeepsTheBalanceBelowTheCreditLimit() {
        createTransactionAndSetResponse(CREDIT_LIMIT - 1);
    }

    private void createTransactionAndSetResponse(double v) {
        TransactionController.TransactionRequest transaction = new TransactionController.TransactionRequest(account.getCreditCardNumber(), v, Instant.now(), "Electronics XYZ");
        response = client
                .post()
                .uri("/transaction/createv2")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transaction))
                .exchange()
                .block();
    }

    @Then("the transaction is allowed")
    public void theTransactionIsAllowed() {
        assertEquals(HttpStatus.CREATED, response.statusCode());
    }

    private JdbcTemplate getJdbcTemplate() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(dbUrl);
        dataSource.setUser("root");
        dataSource.setPassword("password");
        return new JdbcTemplate(dataSource);
    }
}
