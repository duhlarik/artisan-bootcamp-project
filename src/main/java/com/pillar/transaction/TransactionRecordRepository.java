package com.pillar.transaction;

import com.pillar.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Integer> {
    List<TransactionRecord> findAllByAccount(Account account);
}
