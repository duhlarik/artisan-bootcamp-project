package com.pillar.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByName(String name);
    Customer findOneByName(String name);
    List<Customer> findByName(String name);
}
