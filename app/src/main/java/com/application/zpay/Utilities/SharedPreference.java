package com.application.zpay.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.application.zpay.interfaces.Constants;


public class SharedPreference {
    public static String My_Prefrences = "zPay_Preferences";
    static SharedPreferences mPref;
    Editor editor;

    public SharedPreference(Context mContext) {
        mPref = mContext.getSharedPreferences(My_Prefrences, Context.MODE_PRIVATE);
        editor = mPref.edit();
    }

    public static String getUserMobileNumber() {
        return mPref.getString(Constants.USER_MOBILE, "");
    }

    public static void setUserMobileNumber(String userMobileNumber) {
        Editor editor2 = mPref.edit();
        editor2.putString(Constants.USER_MOBILE, userMobileNumber);
        editor2.commit();
    }

    public static String getUserName() {
        return mPref.getString(Constants.USER_NAME, "");
    }

    public static void setUserName(String userName) {
        Editor editor2 = mPref.edit();
        editor2.putString(Constants.USER_NAME, userName);
        editor2.commit();
    }

    public static String getRechargeAmount() {
        return mPref.getString(Constants.RECHARGE_AMOUNT, "");
    }

    public static void setRechargeAmount(String rechargeAmount) {
        Editor editor2 = mPref.edit();
        editor2.putString(Constants.RECHARGE_AMOUNT, rechargeAmount);
        editor2.commit();
    }

    public static String getBalance() {
        return mPref.getString(Constants.BALANCE, "0");
    }

    public static void setBalance(String userName) {
        Editor editor2 = mPref.edit();
        editor2.putString(Constants.BALANCE, userName);
        editor2.commit();
    }

    public static String getUserId() {
        return mPref.getString(Constants.USER_ID, "");
    }

    public static void setUserId(String userId) {
        Editor editor2 = mPref.edit();
        editor2.putString(Constants.USER_ID, userId);
        editor2.commit();
    }

    public static String getUserEmail() {
        return mPref.getString(Constants.USER_EMAIL, "");
    }

    public static void setUserEmail(String email) {
        Editor editor2 = mPref.edit();
        editor2.putString(Constants.USER_EMAIL, email);
        editor2.commit();
    }

    public static void setLogin() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(Constants.IS_LOGIN, true);
        editor.commit();
    }

    public static boolean isLogin() {
        return mPref.getBoolean(Constants.IS_LOGIN,false);
    }

    public static void resetLogin() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(Constants.IS_LOGIN, false);
        editor.commit();
    }

    public static void removeAllData(){
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(Constants.USER_NAME);
        editor.remove(Constants.USER_EMAIL);
        editor.remove(Constants.USER_NAME);
        editor.remove(Constants.USER_ID);
      resetLogin();

        editor.apply();
        Log.e("removeAllData","true");
    }


}
