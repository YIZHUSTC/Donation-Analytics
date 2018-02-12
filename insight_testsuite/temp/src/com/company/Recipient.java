package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recipient {

    private List<Double> amount = new ArrayList<Double>();
    private Double totalAmount = 0.0;

    public Recipient(){
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public void insertAmount(Double amt) {
        this.amount.add(amt);
        this.totalAmount += amt;
    }

    public Long getPercentile(Integer percentile) {
        Collections.sort(this.amount);
        Double n = Math.ceil((double) percentile / 100.0 * this.amount.size());
        return Math.round(this.amount.get(n.intValue() - 1));
    }

    public Integer getContributionNumber() {
        return this.amount.size();
    }
}