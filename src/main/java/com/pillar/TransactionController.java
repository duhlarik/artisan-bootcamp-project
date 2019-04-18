package com.pillar;

import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import com.pillar.transaction.Transaction;
import com.pillar.transaction.TransactionBankRequest;
import com.pillar.transaction.TransactionRecord;
import com.pillar.transaction.TransactionRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;

import static com.pillar.Balance.calculateTransactionBalance;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private static final boolean APPROVED = true;
    private static final Integer FAILED_TRANSACTION_ID = 0;
    private AccountRepository accountRepository;
    private TransactionRecordRepository transactionRecordRepository;

    private BankService bankService;

    public TransactionController(AccountRepository repository, TransactionRecordRepository transactionRecordRepository, BankService bankService){
        this.accountRepository = repository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.bankService = bankService;
    }

    @RequestMapping(path = "/create", method = {RequestMethod.POST})
    public ResponseEntity<?> createTransaction(@RequestBody TransactionBankRequest transactionBankRequest) {
        Account account = accountRepository.findByCardNumber(transactionBankRequest.getCreditCardNumber());
        transactionBankRequest.setCreditLimit(account.getCreditLimit());
        return new ResponseEntity<>(this.bankService.postTransaction(transactionBankRequest));
    }

    @RequestMapping(path = "/createv2", method = {RequestMethod.POST})
    public ResponseEntity<TransactionResponse> createDbTransaction(@RequestBody TransactionRequest request) {
        if(createTransaction(request).isValid()){
            TransactionRecord transactionRecord = saveTransaction(request);
            return new ResponseEntity<>(new TransactionResponse(transactionRecord.getId(), transactionRecord.isApproved()),
                    HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(new TransactionResponse(FAILED_TRANSACTION_ID, APPROVED),
                    HttpStatus.FORBIDDEN);
        }
    }

    private TransactionRecord saveTransaction(@RequestBody TransactionRequest request) {
        Account account = accountRepository.findByCardNumber(request.creditCardNumber);
        TransactionRecord transactionRecord = new TransactionRecord(request.getAmount(), request.dateOfTransaction, APPROVED, account, request.isCharge);
        return transactionRecordRepository.save(transactionRecord);
    }

    private Transaction createTransaction(TransactionRequest request){
        Double amount = request.getAmount();
        Account account = accountRepository.findByCardNumber(request.getCreditCardNumber());
        double creditLimit = account.getCreditLimit();
        ArrayList<TransactionRecord> transactionRecordList = transactionRecordRepository.findAllByAccount(account);

        return new Transaction(amount, calculateTransactionBalance(transactionRecordList), creditLimit);
    }

    @RequestMapping(path = "/payment", method={RequestMethod.POST})
    public ResponseEntity<TransactionResponse> createPaymentTransaction(@RequestBody TransactionRequest request) {
        return new ResponseEntity<>(new TransactionResponse(0, false), HttpStatus.CREATED);
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

//        public double getChargeBalance() {
//            return 0;
//        }
    }

    public static class TransactionRequest {
        private boolean isCharge = true;
        private String creditCardNumber;
        private Double amount;
        private Instant dateOfTransaction;
        private String retailer;

        public TransactionRequest() {}

        public TransactionRequest(String creditCardNumber, Double amount, Instant dateOfTransaction, String retailer) {
            this.creditCardNumber = creditCardNumber;
            this.amount = amount;
            this.dateOfTransaction = dateOfTransaction;
            this.retailer = retailer;
        }

        public TransactionRequest(String creditCardNumber, Double amount, Instant dateOfTransaction, String retailer, Boolean isCharge) {
            this.creditCardNumber = creditCardNumber;
            this.amount = amount;
            this.dateOfTransaction = dateOfTransaction;
            this.retailer = retailer;
            this.isCharge = isCharge;
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


    }

}
