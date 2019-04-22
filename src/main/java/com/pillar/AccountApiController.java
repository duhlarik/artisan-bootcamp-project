package com.pillar;

import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import com.pillar.cardholder.Cardholder;
import com.pillar.cardholder.CardholderRepository;
import com.pillar.customer.Customer;
import com.pillar.customer.CustomerRepository;
import com.pillar.rewardsProgramme.RewardsProgrammeRepository;
import com.pillar.transaction.TransactionRecord;
import com.pillar.transaction.TransactionRecordRepository;
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
    private final TransactionRecordRepository transactionRecordRepository;
    private final RewardsProgrammeRepository rewardsProgrammeRepository;

    public AccountApiController(AccountRepository accountRepository, CardholderRepository cardholderRepository, CustomerRepository customerRepository, TransactionRecordRepository transactionRepository, RewardsProgrammeRepository rewardsProgrammeRepository) {
        this.accountRepository = accountRepository;
        this.cardholderRepository = cardholderRepository;
        this.customerRepository = customerRepository;
        this.transactionRecordRepository = transactionRepository;
        this.rewardsProgrammeRepository = rewardsProgrammeRepository;
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
            ArrayList<TransactionRecord> transactionRecordList = this.transactionRecordRepository.findAllByAccount(account);
            account.setTransactionBalance(TransactionRecordGenerator.calculateTransactionBalance(transactionRecordList));
            account.setChargeBalance(TransactionRecordGenerator.calculateChargeBalance(transactionRecordList));
            return new ResponseEntity<>(account, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{cardNumber}/rewards/{retailer}")
    public ResponseEntity<Double> getRewardsBalance(String cardNumber, String retailer) {
        if(rewardsProgrammeRepository.existsByRetailer(retailer)){
            Account account = accountRepository.findOneByCreditCardNumber(cardNumber).get();
            ArrayList<TransactionRecord> transactionRecordList = this.transactionRecordRepository.findAllByAccount(account);
            double chargeBalance = TransactionRecordGenerator.calculateChargeBalance(transactionRecordList);
            double percentage = rewardsProgrammeRepository.findOneByRetailer(retailer).get().getPercentage();
            return new ResponseEntity<>(new RewardsBalance(chargeBalance, percentage).calculate(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(Double.MIN_VALUE, HttpStatus.NOT_FOUND);
        }
    }
}
