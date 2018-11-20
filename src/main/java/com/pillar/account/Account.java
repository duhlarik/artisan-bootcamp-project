package com.pillar.account;

import com.pillar.cardholder.Cardholder;
import com.pillar.customer.Customer;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Account {
    @Id
    private Integer id;

    private String cardNumber;
    private int creditLimit;
    private boolean active;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "cardholder_id")
    private Cardholder cardholder;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Account() {

    }

    public Account(Cardholder cardholder) {
        this.id = null;
        this.creditLimit = 10000;
        this.cardNumber = UUID.randomUUID().toString();
        this.active = true;
        this.cardholder = cardholder;
    }

    public Account(int id, int creditLimit, String cardNumber, boolean active, Cardholder cardholder) {
        this.id = id;
        this.creditLimit = creditLimit;
        this.cardNumber = cardNumber;
        this.active = active;
        this.cardholder = cardholder;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public boolean isActive() {
        return active;
    }

    public Cardholder getCardholder() {
        return cardholder;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
