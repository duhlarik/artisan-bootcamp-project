package com.pillar.cucumber;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.pillar.account.Account;
import com.pillar.cardholder.Cardholder;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.Assert.assertFalse;

public class AccountCancellationStepdefs {
    private String endpoint;
    private String dbUrl;
    private final WebClient client;
    private final String cardNumber = "111111222222333333444444555555666666";
    private Account account;

    public AccountCancellationStepdefs() {
        endpoint = System.getProperty("integration-endpoint", "http://localhost:8080");
        dbUrl = System.getProperty("integration-mysql", "jdbc:mysql://localhost:3316/cc_processing");
        client = WebClient.create(endpoint);
    }

    @After
    public void teardown(){
        JdbcTemplate template = getJdbcTemplate();
        template.update("DELETE FROM account");
        template.update("DELETE FROM customer");
        template.update("DELETE FROM cardholder");
    }

    @Given("an active credit card account")
    public void anActiveCardAccount() {
        Cardholder cardholder = new Cardholder(1, "123-45-6788", "Steve Goliath");
        account = new Account(1, 10000, this.cardNumber, true, cardholder);
        JdbcTemplate template = getJdbcTemplate();
        template.update("INSERT INTO customer SET id=?, name=?", 1, "Target");
        template.update("INSERT INTO cardholder SET id=?, ssn=?, name=?",
                1, cardholder.getSsn(), cardholder.getName());
        template.update("INSERT INTO account SET id=?, cardholder_id=?, customer_id=?, credit_card_number=?, credit_limit=?, active=?",
                1, 1, 1, account.getCardNumber(), account.getCreditLimit(), account.isActive());
    }

    @When("a request is made to cancel the card")
    public void requestToCancelCard() {
        ClientResponse response = client
                .put()
                .uri("/api/account/cancel/" + cardNumber)
                .exchange()
                .block();
    }

    @Then("the card should become inactive")
    public void cardBecomesInactive() {
        assertFalse(account.isActive());
    }

    private JdbcTemplate getJdbcTemplate() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(dbUrl);
        dataSource.setUser("root");
        dataSource.setPassword("password");
        return new JdbcTemplate(dataSource);
    }
}