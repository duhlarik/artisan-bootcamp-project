package com.pillar;

import com.pillar.account.Account;
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
    public ResponseEntity<Account> create(@RequestBody Map params) {
        final UUID uuid = UUID.randomUUID();
        return new ResponseEntity<>(new Account(1, 10000, uuid.toString(), true), HttpStatus.CREATED);
    }
}
