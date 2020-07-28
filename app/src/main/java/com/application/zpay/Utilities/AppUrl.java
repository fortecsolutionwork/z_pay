package com.application.zpay.Utilities;

/**
 * Created by hp I3 on 27-11-2018.
 */

public class AppUrl {
    //--------------------------------For Local ---------------------------------------------
    public static String MAIN_URL= "http://182.77.56.114:8080/zpay/";
    //public static String MAIN_URL= "http://192.168.1.245:8080/zpay/";
    public static String REGISTER = MAIN_URL+"registerCustomer";// for local server
    public static String LOGIN = MAIN_URL+"login";// for local server
    public static String PREPAID = MAIN_URL+"prepaid/recharge";// for local server
    public static String POSTPAID = MAIN_URL+"postpaid/recharge";// for local server
    public static String POSTPAID_FETCH_BILL = MAIN_URL+"api/getAnyPostPaidAndCylinder";// for local server
    public static String FOREGET = MAIN_URL+"forget";// for local server
    public static String UPDATE_PASSWORD = MAIN_URL+"updatePassword";// for local server
    public static String ADD_MONEY = MAIN_URL+"api/addMoney";// for local server
    public static String TXN_HISTORY = MAIN_URL+"getCustomerTransactionHistory";// for local server
    public static String GENERATE_PAYMENT_TOKEN_LIVE = "https://icp-api.bankopen.co/api/payment_token";
    public static String GENERATE_PAYMENT_TOKEN_SANDBOX = "https://sandbox-icp-api.bankopen.co/api/payment_token";



}
