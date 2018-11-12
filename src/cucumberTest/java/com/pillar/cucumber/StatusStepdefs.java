package com.pillar.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;


public class StatusStepdefs {
    private Logger log = LoggerFactory.getLogger(StatusStepdefs.class);
    private String endpoint;
    private String statusUrl = "/actuator/health";
    private Mono<ClientResponse> response;

    @Given("the health endpoint")
    public void theHealthEndpoint() {
        endpoint = System.getProperty("integration-endpoint", "http://localhost:8080");
    }

    @When("I request the status")
    public void iRequestTheStatus() {
        WebClient client = WebClient.create(endpoint);
        response = client.get().uri(statusUrl).exchange();
    }

    @Then("I receive an {string}")
    public void iReceiveAn(String status) {
        if (status.equals("OK")) {
            HttpStatus httpStatus = response.block().statusCode();
            assertEquals(httpStatus, HttpStatus.OK);
        } else {
            log.error("Unknown status to query for: " + status);
            throw new cucumber.api.PendingException();
        }
    }
}
