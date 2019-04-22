package com.pillar.rewardsProgramme;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RewardsProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String retailer;
    private double percentage;

    public RewardsProgramme(){}

    public RewardsProgramme(String retailer, double percentage) {
        this.retailer = retailer;
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getRetailer() {
        return retailer;
    }
}
