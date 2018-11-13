package com.pillar.merchant;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Merchant {
    @Id
    private Integer id;

    private String name;

    public Merchant() {}

    public Merchant(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!getClass().isInstance(other)) {
            return false;
        }
        return id.equals(((Merchant)other).getId());
    }
}
