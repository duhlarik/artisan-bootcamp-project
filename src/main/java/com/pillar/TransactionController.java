package com.pillar;

import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import com.pillar.transaction.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private AccountRepository accountRepository;

    private BankService bankService;

    public TransactionController(AccountRepository repository, BankService bankService){
        this.accountRepository = repository;
        this.bankService = bankService;
    }

    @RequestMapping(path = "/create", method = {RequestMethod.POST})
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        Account account = accountRepository.findByCardNumber(transaction.getCreditCardNumber());
        transaction.setCreditLimit(account.getCreditLimit());
        return new ResponseEntity<>(this.bankService.postTransaction(transaction));
    }

}
