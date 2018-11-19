package com.pillar.account;

import com.pillar.cardholder.Cardholder;

import javax.persistence.Id;
import java.util.Objects;

public class Account {
    @Id
    private final int id;

    private final String cardNumber;
    private final int creditLimit;
    private final boolean active;

    public Account(int id, int creditLimit, String cardNumber, boolean active) {
        this.id = id;
        this.creditLimit = creditLimit;
        this.cardNumber = cardNumber;
        this.active = active;
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
