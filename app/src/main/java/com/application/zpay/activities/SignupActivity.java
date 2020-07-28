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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.application.zpay.R;
import com.application.zpay.Utilities.Utils;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends zPayActivity {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String secureId, androidDeviceId;
    private final int REQUEST_CODE_IME = 20;

    @BindView(R.id.nameEt)
    TextInputEditText mNameEt;

    @BindView(R.id.mobileNumberEt)
    TextInputEditText mMobileNumberEt;

    @BindView(R.id.emailEt)
    TextInputEditText mEmailEt;

    @BindView(R.id.passwordEt)
    TextInputEditText mPasswordEt;

    @BindView(R.id.confirmPassEt)
    TextInputEditText mConfirmPassEt;

    @BindView(R.id.signUpBtn)
    Button mSignUpBtn;

    @BindView(R.id.alreadyTv)
    TextView mAlreadyTv;
    @BindView(R.id.rootLayout)
    LinearLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mMobileNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mMobileNumberEt.getText().toString().trim().length() == 10) {
                    mMobileNumberEt.clearFocus();
                    mEmailEt.requestFocus();
                    mEmailEt.setCursorVisible(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.signUpBtn, R.id.alreadyTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpBtn:
                doSignup();
                break;
            case R.id.alreadyTv:
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                break;
        }
    }

    private void doSignup() {

        if (mNameEt.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(SignupActivity.this, mRootLayout, "Please enter username");
            //Toast.makeText(SignupActivity.this, "Please enter username", Toast.LENGTH_LONG).show();
        } else if (mMobileNumberEt.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(SignupActivity.this, mRootLayout, "Please enter mobile number");
           // Toast.makeText(SignupActivity.this, "Please enter mobile number", Toast.LENGTH_LONG).show();
        } else if (mMobileNumberEt.getText().toString().trim().length() < 10) {
            Utils.showSnackBar(SignupActivity.this, mRootLayout, "Mobile number should be of 10 digits");
            //Toast.makeText(SignupActivity.this, "Mobile number should be of 10 digits", Toast.LENGTH_LONG).show();
        } else if (mEmailEt.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(SignupActivity.this, mRootLayout, "Please enter email address");
            //Toast.makeText(SignupActivity.this, "Please enter email address", Toast.LENGTH_LONG).show();
        } else if (!mEmailEt.getText().toString().trim().matches(emailPattern)) {
            Utils.showSnackBar(SignupActivity.this, mRootLayout, "Please enter valid email address");
        } else if (mPasswordEt.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(SignupActivity.this, mRootLayout, "Please enter password");
            //Toast.makeText(SignupActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
        } else if (mConfirmPassEt.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(SignupActivity.this, mRootLayout, "Please re-enter password");
            //Toast.makeText(SignupActivity.this, "Please re-enter password", Toast.LENGTH_LONG).show();
        } else if (!(mPasswordEt.getText().toString().trim().equals(mConfirmPassEt.getText().toString().trim()))) {
            Utils.showSnackBar(SignupActivity.this, mRootLayout, "Passwords does not match.");
            //Toast.makeText(SignupActivity.this, "Passwords does not match.", Toast.LENGTH_LONG).show();
        } else if (Build.VERSION.SDK_INT < 23) {
            getPhoneDetails();
            Intent intent = new Intent(SignupActivity.this, ConfirmOTPActivity.class);
            intent.putExtra("NAME", mNameEt.getText().toString().trim());
            intent.putExtra("MOBILE_NUMBER", mMobileNumberEt.getText().toString().trim());
            intent.putExtra("EMAIL", mEmailEt.getText().toString().trim());
            intent.putExtra("PASSWORD", mPasswordEt.getText().toString().trim());
            intent.putExtra("IMEI", androidDeviceId);
            intent.putExtra("MODEL", Build.MODEL + " " + Build.MANUFACTURER);
            startActivity(intent);

        } else {
            if (checkPermission()) {
                getPhoneDetails();
                if (checkPermission()) {
                    Intent intent = new Intent(SignupActivity.this, ConfirmOTPActivity.class);
                    intent.putExtra("NAME", mNameEt.getText().toString().trim());
                    intent.putExtra("MOBILE_NUMBER", mMobileNumberEt.getText().toString().trim());
                    intent.putExtra("EMAIL", mEmailEt.getText().toString().trim());
                    intent.putExtra("PASSWORD", mPasswordEt.getText().toString().trim());
                    if (Build.VERSION.SDK_INT >= 29) {
                        intent.putExtra("IMEI", secureId);
                    } else {
                        intent.putExtra("IMEI", androidDeviceId);
                    }

                    intent.putExtra("MODEL", Build.MODEL + " " + Build.MANUFACTURER);
                    startActivity(intent);
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_IME);
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_IME);
            }


        }


    }

    @Override
    public void onBackPressed() {
        finish();
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
            secureId = Settings.Secure.getString(SignupActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
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
            getPhoneDetails();
            Intent intent = new Intent(SignupActivity.this, ConfirmOTPActivity.class);
            intent.putExtra("NAME", mNameEt.getText().toString().trim());
            intent.putExtra("MOBILE_NUMBER", mMobileNumberEt.getText().toString().trim());
            intent.putExtra("EMAIL", mEmailEt.getText().toString().trim());
            intent.putExtra("PASSWORD", mPasswordEt.getText().toString().trim());
            if (Build.VERSION.SDK_INT >= 29) {
                intent.putExtra("IMEI", secureId);
            } else {
                intent.putExtra("IMEI", androidDeviceId);
            }

            intent.putExtra("MODEL", Build.MODEL + " " + Build.MANUFACTURER);
            startActivity(intent);

        } else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }
}
