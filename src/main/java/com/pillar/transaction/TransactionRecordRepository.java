package com.pillar.transaction;

import com.pillar.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Integer> {
    ArrayList<TransactionRecord> findAllByAccount(Account account);

    ArrayList<TransactionRecord> findAllByAccountAndIsChargeTrue(Account account);
}
