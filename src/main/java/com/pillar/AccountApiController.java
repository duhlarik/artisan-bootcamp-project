package com.pillar;

import com.pillar.account.Account;
import com.pillar.cardholder.Cardholder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/account")
public class AccountApiController {
    @RequestMapping(method = {RequestMethod.POST})
    public ResponseEntity<Account> create(@RequestBody Map<String, String> params) {
        final String name = params.get("cardHolderName");
        final String ssn = params.get("ssn");
        final UUID uuid = UUID.randomUUID();

        final Cardholder cardholder = new Cardholder(1, ssn, name);
        final Account account = new Account(1, 10000, uuid.toString(), true, cardholder);

        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }
}
