package com.application.zpay.models;

public class PlansModel {

    String rechargeCost;
    String validity;
    String desc;

    public PlansModel(String rechargeCost, String validity, String desc) {
        this.rechargeCost = rechargeCost;
        this.validity = validity;
        this.desc = desc;
    }

    public String getRechargeCost() {
        return rechargeCost;
    }

    public void setRechargeCost(String rechargeCost) {
        this.rechargeCost = rechargeCost;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
