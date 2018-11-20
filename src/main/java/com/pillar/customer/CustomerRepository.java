package com.pillar.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByName(String name);
    Optional<Customer> findOneByName(String name);
    List<Customer> findByName(String name);
}
