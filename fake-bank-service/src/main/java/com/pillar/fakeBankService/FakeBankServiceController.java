package com.pillar.fakeBankService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class FakeBankServiceController {
    private Double currentBalance = 7000.0;

    @RequestMapping(method = {RequestMethod.POST})
    public ResponseEntity<?> postTransaction(@RequestBody TransactionRequest transaction) {
        if (transaction.getAmount() + currentBalance <= transaction.getCreditLimit()) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
