package com.application.zpay.models;

public class TransactionModel {
    String date;
    String operatorType;
    String rechargeAmount;
    String operatorName;
    String rechargeNumber;
    String rechargeStatus;

    public TransactionModel(String date, String operatorType, String rechargeAmount, String operatorName, String rechargeNumber, String rechargeStatus) {
        this.date = date;
        this.operatorType = operatorType;
        this.rechargeAmount = rechargeAmount;
        this.operatorName = operatorName;
        this.rechargeNumber = rechargeNumber;
        this.rechargeStatus = rechargeStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getRechargeNumber() {
        return rechargeNumber;
    }

    public void setRechargeNumber(String rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    public String getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(String rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }
}
