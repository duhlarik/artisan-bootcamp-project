package com.pillar.transaction;

import com.pillar.account.Account;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class TransactionRecord {
    public static final double DELTA = 0.001;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double amount;
    private Instant dateOfTransaction;
    private boolean approved;
    private boolean isCharge;
    private String retailer;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public TransactionRecord() {}

    public TransactionRecord(Double amount, Instant dateOfTransaction, boolean approved, Account account) {
        setFields(amount, dateOfTransaction, approved, account);
    }

    public TransactionRecord(double amount, Instant date, boolean approved, Account account, boolean isCharge) {
        setFields(amount, date, approved, account);
        this.isCharge = isCharge;
    }

    public TransactionRecord(double amount, Instant dateOfTransaction, boolean approved, Account account, boolean isCharge, String retailer) {
        setFields(amount, dateOfTransaction, approved, account);
        this.isCharge = isCharge;
        this.retailer = retailer;
    }

    private void setFields(double amount, Instant date, boolean approved, Account account) {
        this.amount = amount;
        this.dateOfTransaction = date;
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

    public boolean isCharge() {
        return isCharge;
    }

    public String getRetailer() {
        return retailer;
    }
}
