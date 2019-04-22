package com.pillar;

class RewardsBalance {
    double chargeBalance;
    double percentage;

    RewardsBalance(double chargeBalance, double percentage) {
        this.chargeBalance = chargeBalance;
        this.percentage = percentage;
    }

    double calculate() {
        return chargeBalance * percentage / 100;
    }
}
