package com.pillar.integration;

import com.pillar.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@SpringBootTest()
@Rollback
@RunWith(SpringRunner.class)
public class TestTransaction {

    WebClient client;

    @Before
    public void setUp() {
        String defaultEndpoint = "http://localhost:5000";
        client = WebClient.create(System.getProperty("fake-bank-service", defaultEndpoint));
    }

    @Test
    public void testExternalApiEndpointIsReturnsExpectedResult() {
        Transaction transaction = new Transaction("1234", 5.00, new Date(), 1, 10000);
        final ClientResponse response = client
                                        .post()
                                        .uri("/api/transaction")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromObject(transaction))
                                        .exchange()
                                        .block();
        assertEquals(HttpStatus.CREATED, response.statusCode());
    }

}
