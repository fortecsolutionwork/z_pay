package com.application.zpay.models;

/**
 * Created by Gaganjot Singh on 14/07/2020.
 */
public class PrepaidModel {
    String type;
    String providerName;
    String spKey;

    public PrepaidModel(String type, String providerName, String spKey) {
        this.type = type;
        this.providerName = providerName;
        this.spKey = spKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getSpKey() {
        return spKey;
    }

    public void setSpKey(String spKey) {
        this.spKey = spKey;
    }
}
