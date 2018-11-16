package com.pillar.fakeBankService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeBankServiceController {

    @PostMapping("/transaction")
    public ResponseEntity<?> postTransaction(@RequestBody TransactionRequest transaction) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
