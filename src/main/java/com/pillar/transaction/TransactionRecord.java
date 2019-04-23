package com.pillar.transaction;

import com.pillar.account.Account;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double amount;
    private Instant dateOfTransaction;
    private boolean approved;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public TransactionRecord() {}

    public TransactionRecord(Double amount, Instant dateOfTransaction, boolean approved, Account account) {
        this.amount = amount;
        this.dateOfTransaction = dateOfTransaction;
        this.approved = approved;
        this.account = account;
    }

    public Integer getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Instant getDateOfTransaction() {
        return dateOfTransaction;
    }

    public Account getAccount() {
        return account;
    }

    public boolean isApproved() {
        return approved;
    }

    public boolean isValid(Account account, List<TransactionRecord> transactionRecordList) {
        double balance = transactionRecordList.stream().mapToDouble(TransactionRecord::getAmount).sum();
        return amount + balance <= account.getCreditLimit();
    }
}
