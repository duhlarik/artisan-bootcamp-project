package com.pillar;

import com.pillar.account.Account;
import com.pillar.transaction.TransactionRecord;

import java.time.Instant;

public class AuthorizationTransactionRecord extends TransactionRecord {
    private static final boolean APPROVED = true;

    public  AuthorizationTransactionRecord(double amount) {
        super(amount, Instant.now(), APPROVED, new Account());
    }
}
