package com.pillar.account;

import javax.persistence.Id;
import java.util.Objects;

public class Account {
    @Id
    private final int id;

    private final String card_number;
    private final double credit_limit;
    private final boolean active;

    public Account(int id, double credit_limit, String card_number, boolean active) {
        this.id = id;
        this.credit_limit = credit_limit;
        this.card_number = card_number;
        this.active = active;
    }

    public double getCreditLimit() {
        return credit_limit;
    }

    public String getCardNumber() {
        return card_number;
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
