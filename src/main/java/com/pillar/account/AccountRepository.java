package com.pillar.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByCustomerIdAndCardholderId(Integer customerId, Integer cardholderId);

    @Query("SELECT a FROM Account a WHERE a.creditCardNumber = :cardNumber")
    Account findByCardNumber(@Param("cardNumber") String cardNumber);

    Optional<Account> findOneByCreditCardNumber(String creditCardNumber);
}
