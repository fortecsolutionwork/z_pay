package com.application.zpay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.application.zpay.R;
import com.application.zpay.Utilities.AppUrl;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.Constants;
import com.application.zpay.interfaces.iNetworkCallback;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewPassActivity extends zPayActivity {

    @BindView(R.id.passwordEt)
    TextInputEditText mPasswordEt;

    @BindView(R.id.confirmPassEt)
    TextInputEditText mConfirmPassEt;

    @BindView(R.id.signUpBtn)
    Button mSignUpBtn;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("TOKEN");

        }
    }

    @OnClick(R.id.signUpBtn)
    public void onViewClicked() {
        changePassword();
    }

    private void changePassword() {
        if(mPasswordEt.getText().toString().trim().isEmpty()){
            Utils.showWarningAlert(NewPassActivity.this,"Please enter new password");
        }else if(mConfirmPassEt.getText().toString().trim().isEmpty()){
            Utils.showWarningAlert(NewPassActivity.this,"Please re-enter password");
        }else if(!(mPasswordEt.getText().toString().trim().equals(mConfirmPassEt.getText().toString().trim()))){
            Utils.showWarningAlert(NewPassActivity.this,"Passwords does not match.");
        }
        else{
            showProgressing(NewPassActivity.this);
            callService(AppUrl.UPDATE_PASSWORD, new iNetworkCallback() {
                @Override
                public void addParameters(Map<Object, Object> params) {

                    params.put(Constants.USER_PASSWORD,mPasswordEt.getText().toString().trim());
                    params.put(Constants.USER_CONFIRM_PASSWORD,mPasswordEt.getText().toString().trim());
                    params.put(Constants.RESET_TOKEN,token);
                    Log.e("Params",params.toString());

                }

                @Override
                public void failure(VolleyError volleyError) {
                    hideProgressing();
                    Log.e("VolleyError", "zzz " + volleyError.getMessage());
                    Utils.showWarningAlert(NewPassActivity.this, getString(R.string.something_went_wrong));

                }

                @Override
                public void response(JSONObject response) {
                    hideProgressing();
                    try {
                        int statusCode = response.getInt("statusCode");
                        Log.e("statusCode",statusCode+"");
                        if(statusCode==1){
                            Toast.makeText(NewPassActivity.this, "Password updated successfully. Please login", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(NewPassActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            String errorMessage=response.getString("errorMessage");
                            Utils.showWarningAlert(NewPassActivity.this,errorMessage);
                        }


                    } catch (Exception e) {
                        hideProgressing();
                        Log.e("ExceptionSendCode", e.getMessage());
                    }

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
