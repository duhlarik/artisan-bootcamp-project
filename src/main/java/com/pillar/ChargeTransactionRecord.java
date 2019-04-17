package com.pillar;

import com.pillar.account.Account;
import com.pillar.transaction.TransactionRecord;

import java.time.Instant;

public class ChargeTransactionRecord extends TransactionRecord {
    private static final boolean APPROVED = true;

    public ChargeTransactionRecord(double chargeAmount) {
        super(chargeAmount, Instant.now(), APPROVED, new Account() );
    }
}
