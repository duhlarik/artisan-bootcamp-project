package com.pillar.cardholder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardholderRepository extends JpaRepository<Cardholder, Integer> {
    boolean existsBySsn(String ssn);
    List<Cardholder> findBySsn(String ssn);
}
