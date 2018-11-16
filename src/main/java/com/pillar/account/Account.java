package com.pillar.account;

import javax.persistence.Id;
import java.util.Objects;

public class Account {
    @Id
    private final int id;

    private final String cardNumber;
    private final double creditLimit;
    private final boolean active;

    public Account(int id, double creditLimit, String cardNumber, boolean active) {
        this.id = id;
        this.creditLimit = creditLimit;
        this.cardNumber = cardNumber;
        this.active = active;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
