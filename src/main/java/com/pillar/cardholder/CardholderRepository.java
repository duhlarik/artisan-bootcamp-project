package com.pillar.cardholder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardholderRepository extends JpaRepository<Cardholder, Integer> {
    boolean existsBySsn(String ssn);
    Optional<Cardholder> findOneBySsn(String ssn);
    List<Cardholder> findBySsn(String ssn);
}
