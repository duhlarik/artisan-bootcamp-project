package com.pillar.cardholder;

import javax.persistence.Id;
import java.util.Objects;

public class Cardholder {
    @Id
    private final int id;
    private final String ssn;
    private final String name;

    public Cardholder(int id, String ssn, String name) {
        this.id = id;
        this.ssn = ssn;
        this.name = name;
    }

    public String getSsn() {
        return ssn;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cardholder that = (Cardholder) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
