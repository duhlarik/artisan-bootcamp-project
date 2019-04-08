package com.pillar;

import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import com.pillar.transaction.Transaction;
import com.pillar.transaction.TransactionRecord;
import com.pillar.transaction.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    private BankService bankService;

    public TransactionController(AccountRepository repository, TransactionRepository transactionRepository, BankService bankService){
        this.accountRepository = repository;
        this.transactionRepository = transactionRepository;
        this.bankService = bankService;
    }

    @RequestMapping(path = "/create", method = {RequestMethod.POST})
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        Account account = accountRepository.findByCardNumber(transaction.getCreditCardNumber());
        transaction.setCreditLimit(account.getCreditLimit());
        return new ResponseEntity<>(this.bankService.postTransaction(transaction));
    }

    public TransactionResponse createDbTransaction(TransactionRequest request) {
        Account account = accountRepository.findByCardNumber(request.getCreditCardNumber());
        TransactionRecord transaction = new TransactionRecord(request.getAmount(), request.dateOfTransaction, true, account);
        transaction = transactionRepository.save(transaction);
        return new TransactionResponse(transaction.getId(), transaction.isApproved());
    }

    public static class TransactionResponse {
        private Integer transactionId;
        private boolean approved;

        TransactionResponse(Integer transactionId, boolean approved) {
            this.transactionId = transactionId;
            this.approved = approved;
        }

        public Integer getTransactionId() {
            return transactionId;
        }

        public boolean isApproved() {
            return approved;
        }
    }

    public static class TransactionRequest {
        private String creditCardNumber;
        private Double amount;
        private Instant dateOfTransaction;
        private String retailer;

        public TransactionRequest(String creditCardNumber, Double amount, Instant dateOfTransaction, String retailer) {
            this.creditCardNumber = creditCardNumber;
            this.amount = amount;
            this.dateOfTransaction = dateOfTransaction;
            this.retailer = retailer;
        }

        public String getCreditCardNumber() {
            return creditCardNumber;
        }

        public Double getAmount() {
            return amount;
        }

        public Instant getDateOfTransaction() {
            return dateOfTransaction;
        }

        public String getRetailer() {
            return retailer;
        }
    }
}
