package com.pillar.account;

import com.pillar.cardholder.Cardholder;
import com.pillar.customer.Customer;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Account {
    public static final String CREDIT_CARD_NUMBER = "creditCardNumber";
    public static final String CREDIT_LIMIT = "creditLimit";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String creditCardNumber;
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

    @Transient
    private double transactionBalance = 0;

    public Account() {}

    public Account(Cardholder cardholder, Customer customer) {
        this.id = null;
        this.creditLimit = 10000;
        this.creditCardNumber = UUID.randomUUID().toString();
        this.active = true;
        this.cardholder = cardholder;
        this.customer = customer;
    }

    public Account(int id, int creditLimit, String creditCardNumber, boolean active, Cardholder cardholder) {
        this.id = id;
        this.creditLimit = creditLimit;
        this.creditCardNumber = creditCardNumber;
        this.active = active;
        this.cardholder = cardholder;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
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

    public double getTransactionBalance() {
        return transactionBalance;
    }

    public void setTransactionBalance(double amount) {
        transactionBalance = amount;
    }

    public double getChargeBalance() {
        return 0;
    }
}
