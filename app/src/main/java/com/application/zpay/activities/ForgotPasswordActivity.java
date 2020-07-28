package com.application.zpay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.application.zpay.R;
import com.application.zpay.Utilities.Utils;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends zPayActivity {

    @BindView(R.id.otpBtn)
    Button mOtpBtn;
    @BindView(R.id.phoneNumber)
    TextInputEditText mPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @OnClick(R.id.otpBtn)
    public void onClick() {
        if (mPhoneNumber.getText().toString().trim().isEmpty()) {
            Utils.showWarningAlert(ForgotPasswordActivity.this, "Please enter mobile number");
        } else if (mPhoneNumber.getText().toString().trim().length() < 10) {
            Utils.showWarningAlert(ForgotPasswordActivity.this, "Mobile number should be of 10 digits");
        }
        else{
            Intent intent = new Intent(ForgotPasswordActivity.this, ConfirmOTPForgot.class);
            intent.putExtra("MOBILE_NUMBER", mPhoneNumber.getText().toString().trim());
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
