package com.pillar;

import com.pillar.transaction.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.logging.Logger;

@Service
public class BankService {
    Logger log = Logger.getLogger(BankService.class.getName());
    @Value("${bankservice.url}")
    String serviceUrl;

    public HttpStatus postTransaction(Transaction transaction) {
        log.info("Bank Service URL: " + serviceUrl);
        WebClient webClient = WebClient.create(serviceUrl);
        final ClientResponse response = webClient
                .post()
                .uri("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transaction))
                .exchange()
                .block();
        return response.statusCode();
    }
}
