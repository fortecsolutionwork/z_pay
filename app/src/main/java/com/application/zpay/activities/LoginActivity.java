package com.application.zpay.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.VolleyError;
import com.application.zpay.R;
import com.application.zpay.Utilities.AppUrl;
import com.application.zpay.Utilities.SharedPreference;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.Constants;
import com.application.zpay.interfaces.iNetworkCallback;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends zPayActivity {

    String secureId, androidDeviceId;
    private final int REQUEST_CODE_IME = 20;

    @BindView(R.id.signUp_tv)
    LinearLayout mSignUpTv;

    @BindView(R.id.forgotPassword_tv)
    TextView mForgotPasswordTv;


    @BindView(R.id.phoneEt)
    TextInputEditText mPhoneEt;

    @BindView(R.id.passwordEt)
    TextInputEditText mPasswordEt;

    @BindView(R.id.signInBtn)
    Button mSignInBtn;
    @BindView(R.id.rootLayout)
    LinearLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (SharedPreference.isLogin()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        mPhoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mPhoneEt.getText().toString().trim().length() == 10) {
                    mPhoneEt.clearFocus();
                    mPasswordEt.requestFocus();
                    mPasswordEt.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.signUp_tv, R.id.forgotPassword_tv, R.id.signInBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUp_tv:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                break;
            case R.id.forgotPassword_tv:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.signInBtn:
                doLogin();
                break;
        }
    }

    private void doLogin() {
        if (mPhoneEt.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(LoginActivity.this,mRootLayout , "Mobile number should not be empty");
        } else if (mPhoneEt.getText().toString().trim().length() < 10) {
            Utils.showSnackBar(LoginActivity.this,mRootLayout , "Mobile number should be of 10 digits");
        } else if (mPasswordEt.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(LoginActivity.this,mRootLayout , "Password should not be empty");
        } else if (Build.VERSION.SDK_INT < 23) {
            getPhoneDetails();
            if (Utils.isInternetConnected(LoginActivity.this)) {
                Utils.hideKeyboard(LoginActivity.this);
                showProgressing(LoginActivity.this);
                callService(AppUrl.LOGIN, new iNetworkCallback() {
                    @Override
                    public void addParameters(Map<Object, Object> params) {
                        params.put(Constants.USER_MOBILE, mPhoneEt.getText().toString().trim());
                        params.put(Constants.USER_PASSWORD, mPasswordEt.getText().toString().trim());
                        params.put(Constants.MODEL, Build.MODEL + " " + Build.MANUFACTURER);
                        if (Build.VERSION.SDK_INT >= 29) {
                            params.put(Constants.IMEI_NUMBER, secureId);
                        } else {
                            params.put(Constants.IMEI_NUMBER, androidDeviceId);
                        }

                        Log.e("Params", params.toString());
                    }

                    @Override
                    public void failure(VolleyError volleyError) {
                        hideProgressing();
                        Log.e("VolleyError", "zzz " + volleyError.getMessage());
                        Utils.showSnackBar(LoginActivity.this,mRootLayout , getString(R.string.something_went_wrong));
                       // Utils.showWarningAlert(LoginActivity.this, getString(R.string.something_went_wrong));

                    }

                    @Override
                    public void response(JSONObject response) {
                        hideProgressing();
                        try {
                            int statusCode = response.getInt("statusCode");
                            if (statusCode == 1) {
                                String userId = response.getString("userId");
                                String userName = response.getString("userName");
                                String userEmail = response.getString("useremail");
                                String userMobile = response.getString("userMobile");
                                String balance = response.getString("balance");

                                SharedPreference.setUserMobileNumber(userMobile);
                                SharedPreference.setUserEmail(userEmail);
                                SharedPreference.setUserId(userId);
                                SharedPreference.setUserName(userName);
                                SharedPreference.setBalance(balance);
                                SharedPreference.setLogin();

                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                String errorMessage = response.getString("errorMessage");
                                Utils.showSnackBar(LoginActivity.this,mRootLayout , errorMessage);
                                //Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                //Utils.showWarningAlert(LoginActivity.this,errorMessage);
                            }


                        } catch (Exception e) {
                            hideProgressing();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            } else {
                Utils.showWarningAlert(LoginActivity.this, "Please check your internet connection.");
            }
        } else {
            if (checkPermission()) {
                getPhoneDetails();
                if (checkPermission()) {
                    getPhoneDetails();

                    if (Utils.isInternetConnected(LoginActivity.this)) {
                        Utils.hideKeyboard(LoginActivity.this);
                        showProgressing(LoginActivity.this);
                        callService(AppUrl.LOGIN, new iNetworkCallback() {
                            @Override
                            public void addParameters(Map<Object, Object> params) {
                                params.put(Constants.USER_MOBILE, mPhoneEt.getText().toString().trim());
                                params.put(Constants.USER_PASSWORD, mPasswordEt.getText().toString().trim());
                                params.put(Constants.MODEL, Build.MODEL + " " + Build.MANUFACTURER);
                                if (Build.VERSION.SDK_INT >= 29) {
                                    params.put(Constants.IMEI_NUMBER, secureId);
                                } else {
                                    params.put(Constants.IMEI_NUMBER, androidDeviceId);
                                }

                                Log.e("Params", params.toString());
                            }

                            @Override
                            public void failure(VolleyError volleyError) {
                                hideProgressing();
                                Log.e("VolleyError", "zzz " + volleyError.getMessage());
                                Toast.makeText(LoginActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                // Utils.showWarningAlert(LoginActivity.this, getString(R.string.something_went_wrong));

                            }

                            @Override
                            public void response(JSONObject response) {
                                hideProgressing();
                                try {
                                    int statusCode = response.getInt("statusCode");
                                    if (statusCode == 1) {
                                        String userId = response.getString("userId");
                                        String userName = response.getString("userName");
                                        String userEmail = response.getString("useremail");
                                        String userMobile = response.getString("userMobile");
                                        String balance = response.getString("balance");

                                        SharedPreference.setUserMobileNumber(userMobile);
                                        SharedPreference.setUserEmail(userEmail);
                                        SharedPreference.setUserId(userId);
                                        SharedPreference.setUserName(userName);
                                        SharedPreference.setBalance(balance);
                                        SharedPreference.setLogin();

                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();
                                    } else {
                                        String errorMessage = response.getString("errorMessage");
                                        Utils.showSnackBar(LoginActivity.this,mRootLayout , errorMessage);
                                        //Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                    }


                                } catch (Exception e) {
                                    hideProgressing();
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    } else {
                        Utils.showWarningAlert(LoginActivity.this, "Please check your internet connection.");
                    }

                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_IME);
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_IME);
            }

        }


    }


    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            return false;
        }
        return true;
    }

    public void getPhoneDetails() {

        if (Build.VERSION.SDK_INT >= 29) {
            secureId = Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            androidDeviceId = mTelephony.getDeviceId();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_IME && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Utils.hideKeyboard(LoginActivity.this);
            showProgressing(LoginActivity.this);
            getPhoneDetails();
            callService(AppUrl.LOGIN, new iNetworkCallback() {
                @Override
                public void addParameters(Map<Object, Object> params) {
                    params.put(Constants.USER_MOBILE, mPhoneEt.getText().toString().trim());
                    params.put(Constants.USER_PASSWORD, mPasswordEt.getText().toString().trim());
                    params.put(Constants.MODEL, Build.MODEL + " " + Build.MANUFACTURER);
                    if (Build.VERSION.SDK_INT >= 29) {
                        params.put(Constants.IMEI_NUMBER, secureId);
                    } else {
                        params.put(Constants.IMEI_NUMBER, androidDeviceId);
                    }

                    Log.e("Params", params.toString());
                }

                @Override
                public void failure(VolleyError volleyError) {
                    hideProgressing();
                    Log.e("VolleyError", "zzz " + volleyError.getMessage());
                    Utils.showWarningAlert(LoginActivity.this, getString(R.string.something_went_wrong));

                }

                @Override
                public void response(JSONObject response) {
                    hideProgressing();
                    try {
                        int statusCode = response.getInt("statusCode");
                        if (statusCode == 1) {
                            String userId = response.getString("userId");
                            String userName = response.getString("userName");
                            String userEmail = response.getString("useremail");
                            String userMobile = response.getString("userMobile");
                            String balance = response.getString("balance");

                            SharedPreference.setUserMobileNumber(userMobile);
                            SharedPreference.setUserEmail(userEmail);
                            SharedPreference.setUserId(userId);
                            SharedPreference.setUserName(userName);
                            SharedPreference.setBalance(balance);
                            SharedPreference.setLogin();

                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            String errorMessage = response.getString("errorMessage");
                            Utils.showSnackBar(LoginActivity.this,mRootLayout , errorMessage);
                        }


                    } catch (Exception e) {
                        hideProgressing();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
