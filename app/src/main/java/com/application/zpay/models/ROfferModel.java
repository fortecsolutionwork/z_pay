package com.application.zpay.models;

public class ROfferModel {

    String rechargeCost;
    String description;

    public ROfferModel(String rechargeCost, String description) {
        this.rechargeCost = rechargeCost;
        this.description = description;
    }

    public String getRechargeCost() {
        return rechargeCost;
    }

    public void setRechargeCost(String rechargeCost) {
        this.rechargeCost = rechargeCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
