package com.application.zpay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.VolleyError;
import com.application.zpay.R;
import com.application.zpay.Utilities.AppUrl;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.Constants;
import com.application.zpay.interfaces.iNetworkCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmOTPForgot extends zPayActivity {

    String mobileNumber, verificationId,confirmUserPin;
    @BindView(R.id.messageTv)
    TextView mMessageTv;
    @BindView(R.id.code1_et)
    EditText mCode1;
    @BindView(R.id.code2_et)
    EditText mCode2;
    @BindView(R.id.code3_et)
    EditText mCode3;
    @BindView(R.id.code4_et)
    EditText mCode4;
    @BindView(R.id.code5_et)
    EditText mCode5;
    @BindView(R.id.code6_et)
    EditText mCode6;
    @BindView(R.id.buttonOne)
    Button mButtonOne;
    @BindView(R.id.buttonTwo)
    Button mButtonTwo;
    @BindView(R.id.buttonThree)
    Button mButtonThree;
    @BindView(R.id.buttonFour)
    Button mButtonFour;
    @BindView(R.id.buttonFive)
    Button mButtonFive;
    @BindView(R.id.buttonSix)
    Button mButtonSix;
    @BindView(R.id.buttonSeven)
    Button mButtonSeven;
    @BindView(R.id.buttonEight)
    Button mButtonEight;
    @BindView(R.id.buttonNine)
    Button mButtonNine;
    @BindView(R.id.buttonDelete)
    ImageButton mButtonDelete;
    @BindView(R.id.buttonZero)
    Button mButtonZero;
    @BindView(R.id.buttonOkay)
    ImageButton mButtonOkay;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_o_t_p_forgot);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(ConfirmOTPForgot.this, "Got the code", Toast.LENGTH_SHORT).show();
                String code = phoneAuthCredential.getSmsCode();


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(ConfirmOTPForgot.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(ConfirmOTPForgot.this, "Verification code sent to mobile number.", Toast.LENGTH_LONG).show();
                verificationId = s;

            }
        };

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mobileNumber = extras.getString("MOBILE_NUMBER");
        }

        mMessageTv.setText("Enter OTP sent to 91 " + mobileNumber);

        sendVerificationCode(mobileNumber);
    }

    private void verifyOTP(String verificationId) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, confirmUserPin);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    showProgressing(ConfirmOTPForgot.this);
                    callService(AppUrl.FOREGET, new iNetworkCallback() {
                        @Override
                        public void addParameters(Map<Object, Object> params) {
                            params.put(Constants.USER_MOBILE,mobileNumber);
                            Log.e("Params",params.toString());

                        }

                        @Override
                        public void failure(VolleyError volleyError) {
                            hideProgressing();
                            Log.e("VolleyError", "zzz " + volleyError.getMessage());
                            Utils.showWarningAlert(ConfirmOTPForgot.this, getString(R.string.something_went_wrong));
                        }

                        @Override
                        public void response(JSONObject response) {
                            hideProgressing();
                            try {
                                int statusCode = response.getInt("statusCode");
                                Log.e("statusCode",statusCode+"");
                                if(statusCode==1){
                                    String userMobile=response.getString("userMobile");
                                    String resetToken=response.getString("resetToken");
                                    Intent intent = new Intent(ConfirmOTPForgot.this, NewPassActivity.class);
                                    intent.putExtra("TOKEN", resetToken);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    String errorMessage=response.getString("errorMessage");
                                    Utils.showWarningAlert(ConfirmOTPForgot.this,errorMessage);
                                }


                            } catch (Exception e) {
                                hideProgressing();
                                Log.e("ExceptionSendCode", e.getMessage());
                            }

                        }
                    });

                }
                else{
                    hideProgressing();
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(ConfirmOTPForgot.this, "Verification Failed, Invalid OTP", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(ConfirmOTPForgot.this, "Verification failed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void sendVerificationCode(String mobileNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    @OnClick({R.id.buttonOne, R.id.buttonTwo, R.id.buttonThree, R.id.buttonFour, R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven, R.id.buttonEight, R.id.buttonNine, R.id.buttonDelete, R.id.buttonZero, R.id.buttonOkay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonOne:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "1");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "1");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "1");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "1");
                } else if (mCode5.getText().toString().length() == 0) {
                    setOtp(mCode5, "1");
                } else if (mCode6.getText().toString().length() == 0) {
                    setOtp(mCode6, "1");
                }
                break;
            case R.id.buttonTwo:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "2");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "2");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "2");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "2");
                } else if (mCode5.getText().toString().length() == 0) {
                    setOtp(mCode5, "2");
                } else if (mCode6.getText().toString().length() == 0) {
                    setOtp(mCode6, "2");
                }
                break;
            case R.id.buttonThree:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "3");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "3");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "3");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "3");
                } else if (mCode5.getText().toString().length() == 0) {
                    setOtp(mCode5, "3");
                } else if (mCode6.getText().toString().length() == 0) {
                    setOtp(mCode6, "3");
                }
                break;
            case R.id.buttonFour:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "4");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "4");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "4");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "4");
                } else if (mCode5.getText().toString().length() == 0) {
                    setOtp(mCode5, "4");
                } else if (mCode6.getText().toString().length() == 0) {
                    setOtp(mCode6, "4");
                }
                break;
            case R.id.buttonFive:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "5");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "5");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "5");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "5");
                } else if (mCode5.getText().toString().length() == 0) {
                    setOtp(mCode5, "5");
                } else if (mCode6.getText().toString().length() == 0) {
                    setOtp(mCode6, "5");
                }
                break;
            case R.id.buttonSix:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "6");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "6");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "6");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "6");
                } else if (mCode5.getText().toString().length() == 0) {
                    setOtp(mCode5, "6");
                } else if (mCode6.getText().toString().length() == 0) {
                    setOtp(mCode6, "6");
                }
                break;
            case R.id.buttonSeven:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "7");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "7");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "7");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "7");
                } else if (mCode5.getText().toString().length() == 0) {
                    setOtp(mCode5, "7");
                } else if (mCode6.getText().toString().length() == 0) {
                    setOtp(mCode6, "7");
                }
                break;
            case R.id.buttonEight:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "8");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "8");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "8");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "8");
                } else if (mCode5.getText().toString().length() == 0) {
                    setOtp(mCode5, "8");
                } else if (mCode6.getText().toString().length() == 0) {
                    setOtp(mCode6, "8");
                }
                break;
            case R.id.buttonNine:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "9");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "9");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "9");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "9");
                } else if (mCode5.getText().toString().length() == 0) {
                    setOtp(mCode5, "9");
                } else if (mCode6.getText().toString().length() == 0) {
                    setOtp(mCode6, "9");
                }
                break;
            case R.id.buttonDelete:
                if (mCode6.getText().toString().length() != 0) {
                    mCode6.setText("");
                } else if (mCode5.getText().toString().length() != 0) {
                    mCode5.setText("");
                } else if (mCode4.getText().toString().length() != 0) {
                    mCode4.setText("");
                } else if (mCode3.getText().toString().length() != 0) {
                    mCode3.setText("");
                } else if (mCode2.getText().toString().length() != 0) {
                    mCode2.setText("");
                } else if (mCode1.getText().toString().length() != 0) {
                    mCode1.setText("");
                }
                break;
            case R.id.buttonZero:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "0");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "0");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "0");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "0");
                } else if (mCode5.getText().toString().length() == 0) {
                    setOtp(mCode5, "0");
                } else if (mCode6.getText().toString().length() == 0) {
                    setOtp(mCode6, "0");
                }
                break;
            case R.id.buttonOkay:
                confirmUserPin = mCode1.getText().toString().trim() + mCode2.getText().toString().trim() + mCode3.getText().toString().trim() + mCode4.getText().toString().trim() + mCode5.getText().toString().trim() + mCode6.getText().toString().trim();
                if (mCode1.length() == 0 || mCode2.length() == 0 || mCode3.length() == 0 || mCode4.length() == 0|| mCode5.length() == 0|| mCode6.length() == 0) {
                    Utils.showWarningAlert(ConfirmOTPForgot.this,"Enter verification code");
                }
                else{
                    verifyOTP(verificationId);

                }
                break;
        }
    }

    private void setOtp(TextView editText, String value) {
        editText.setText(value);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
