package com.pillar;

import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import com.pillar.cardholder.Cardholder;
import com.pillar.cardholder.CardholderRepository;
import com.pillar.customer.Customer;
import com.pillar.customer.CustomerRepository;
import com.pillar.transaction.TransactionRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountApiController {
    public static final String ENDPOINT = "/api/account";

    public static final String CARDHOLDER_NAME = "cardholderName";
    public static final String CARDHOLDER_SSN = "cardholderSsn";
    public static final String BUSINESS_NAME = "businessName";

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
        final String name = params.get(CARDHOLDER_NAME);
        final String ssn = params.get(CARDHOLDER_SSN);
        final String businessName = params.get(BUSINESS_NAME);

        final Cardholder cardholder = cardholderRepository.findOneBySsn(ssn)
                .orElseGet(() -> cardholderRepository.save(new Cardholder(ssn, name)));

        final Customer customer = customerRepository.findOneByName(businessName)
                .orElseGet(() -> customerRepository.save(new Customer(businessName)));

        if (!accountRepository.existsByCustomerIdAndCardholderId(customer.getId(), cardholder.getId())) {
            final Account account = accountRepository.save(new Account(cardholder, customer));
            return new ResponseEntity<>(account, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(path = "/cancel/{cardNumber}")
    public ResponseEntity<?> cancelAccount(@PathVariable String cardNumber) {
        final Optional<Account> found = accountRepository.findOneByCreditCardNumber(cardNumber);
        if (found.isPresent()) {
            Account account = found.get();
            account.deactivate();
            accountRepository.save(account);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{cardNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String cardNumber) {
        final Optional<Account> found = accountRepository.findOneByCreditCardNumber(cardNumber);
        if(found.isPresent()){
            Account account = found.get();
            account.setTransactionBalance(5.0);
            return new ResponseEntity<>(account, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
