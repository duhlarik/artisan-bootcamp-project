package com.pillar.cardholder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Cardholder {
    public static final String NAME = "name";
    public static final String SSN = "ssn";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ssn;
    private String name;

    public Cardholder() {}

    public Cardholder(String ssn, String name) {
        this(null, ssn, name);
    }

    public Cardholder(Integer id, String ssn, String name) {
        this.id = id;
        this.ssn = ssn;
        this.name = name;
    }

    public Integer getId() {
        return id;
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
