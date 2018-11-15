package com.pillar;

import com.pillar.customer.Customer;
import com.pillar.customer.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerApiController {
    private CustomerRepository repository;

    public CustomerApiController(CustomerRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = {RequestMethod.GET})
    public List<Customer> getAll() {
        return repository.findAll();
    }

    @RequestMapping(path = "/{id}", method = {RequestMethod.GET})
    public ResponseEntity<Customer> getCustomer(@PathVariable Integer id) {
        return repository.findById(id)
                .map((customer) -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
