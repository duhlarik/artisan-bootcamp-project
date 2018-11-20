package com.pillar.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByCustomerIdAndCardholderId(Integer customerId, Integer cardholderId);

    Account findOneByCreditCardNumber(String creditCardNumber);
}
