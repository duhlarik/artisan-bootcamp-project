package com.pillar;

import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import com.pillar.cardholder.Cardholder;
import com.pillar.cardholder.CardholderRepository;
import com.pillar.customer.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountApiController {
    private final AccountRepository accountRepository;
    private final CardholderRepository cardholderRepository;
    private final CustomerRepository customerRepository;

    public AccountApiController(AccountRepository accountRepository, CardholderRepository cardholderRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.cardholderRepository = cardholderRepository;
        this.customerRepository = customerRepository;
    }

    @RequestMapping(method = {RequestMethod.POST})
    public ResponseEntity<Account> create(@RequestBody Map<String, String> params) {
        final String name = params.get("cardHolderName");
        final String ssn = params.get("ssn");

        final Cardholder cardholder = new Cardholder(null, ssn, name);

        if (!cardholderRepository.existsBySsn(ssn)) {
            cardholderRepository.save(cardholder);
        }

        final Account account = new Account(cardholder);

        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }
}
