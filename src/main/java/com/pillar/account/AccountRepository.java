package com.pillar.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByCustomerIdAndCardholderId(Integer customerId, Integer cardholderId);

    Optional<Account> findOneByCreditCardNumber(String creditCardNumber);
}
