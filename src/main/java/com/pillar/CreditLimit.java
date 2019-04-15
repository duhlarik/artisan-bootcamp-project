package com.pillar;

import com.pillar.transaction.TransactionRecord;

import java.util.ArrayList;

public class CreditLimit {
    public static double calculateBalance(ArrayList<TransactionRecord> transactionRecordList) {
        return transactionRecordList.stream().
                mapToDouble(tr -> tr.getAmount()).
                sum();
    }

}
