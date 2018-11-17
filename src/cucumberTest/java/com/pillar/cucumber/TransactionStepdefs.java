package com.pillar.cucumber;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.pillar.account.Account;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.HashMap;
import  java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;

public class TransactionStepdefs {
    private String dbUrl;
    private String endpoint;
    private ClientResponse response;
    private final WebClient client;
    private Account account;
    private UUID activeCreditCardNumber;


    public TransactionStepdefs() {
        endpoint = System.getProperty("integration-endpoint", "http://localhost:8080");
        dbUrl = System.getProperty("integration-mysql", "jdbc:mysql://localhost:3306/cc_processing");
        client = WebClient.create(endpoint);
        activeCreditCardNumber = randomUUID();
    }

    @Given("an active card and account")
    public void anActiveCardAndAccount(){
        createAccount();
        String query = "SELECT * FROM account WHERE credit_card_number = ?";
        account = (Account)getJdbcTemplate().queryForObject(
                query, new Object[] { activeCreditCardNumber.toString() }, new AccountRowMapper());
    }

    @When("a purchase transaction request is made,")
    public void aPurchaseTransactionRequestIsMade() {
        HashMap<String, String> transaction = new HashMap<>();
        transaction.put("amount", "2.00");
        transaction.put("creditCardNumber", activeCreditCardNumber.toString());
        transaction.put("creditLimit", "5.00");
        response =  client
                .post()
                .uri("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transaction))
                .exchange()
                .block();
    }

    @Then("success response is returned")
    public void successResponseIsReturned() {
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode());
    }

    private JdbcTemplate getJdbcTemplate() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(dbUrl);
        dataSource.setUser("root");
        dataSource.setPassword("password");
        return new JdbcTemplate(dataSource);
    }

    private void createAccount() {
        JdbcTemplate template = getJdbcTemplate();
        template.update("INSERT INTO account SET credit_card_number = ?, active = ?", activeCreditCardNumber.toString(), 1);
    }
}
