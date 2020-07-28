package com.application.zpay.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.application.zpay.R;
import com.application.zpay.Utilities.AppUrl;
import com.application.zpay.Utilities.SharedPreference;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.activities.ConfirmOTPForgot;
import com.application.zpay.activities.LoginActivity;
import com.application.zpay.activities.NewPassActivity;
import com.application.zpay.interfaces.Constants;
import com.application.zpay.interfaces.iNetworkCallback;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Gaganjot Singh on 26/06/2020.
 */
public class MyAccountFrg extends zPayFragment {

    @BindView(R.id.userNameTv)
    TextView mUserNameTv;
    @BindView(R.id.userMobileTv)
    TextView mUserMobileTv;
    @BindView(R.id.myQrLl)
    LinearLayout mMyQrLl;
    @BindView(R.id.kycLl)
    LinearLayout mKycLl;
    @BindView(R.id.changePasswordLl)
    LinearLayout mChangePasswordLl;
    @BindView(R.id.changeAppPinLl)
    LinearLayout mChangeAppPinLl;
    @BindView(R.id.termsLl)
    LinearLayout mTermsLl;
    @BindView(R.id.logoutLl)
    LinearLayout mLogoutLl;



    Unbinder unbinder;
    private static MyAccountFrg mInstance;
    public static MyAccountFrg getInstance() {
        if (mInstance == null) {
            mInstance = new MyAccountFrg();
        }
        return mInstance;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myaccount_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserMobileTv.setText(SharedPreference.getUserMobileNumber());
        mUserNameTv.setText(SharedPreference.getUserName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.myQrLl, R.id.kycLl, R.id.changePasswordLl, R.id.changeAppPinLl, R.id.termsLl, R.id.logoutLl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myQrLl:
                break;
            case R.id.kycLl:
                break;
            case R.id.changePasswordLl:
                getToken();
                break;
            case R.id.changeAppPinLl:
                break;
            case R.id.termsLl:
                break;
            case R.id.logoutLl:
                showConfirmationAlert(getActivity(),"You want to logout?");
                break;
        }
    }

    private void getToken() {

        if(Utils.isInternetConnected(getActivity())){
            showProgressing(getActivity());
            callService(AppUrl.FOREGET, new iNetworkCallback() {
                @Override
                public void addParameters(Map<Object, Object> params) {
                    params.put(Constants.USER_MOBILE,SharedPreference.getUserMobileNumber());
                    Log.e("Params",params.toString());

                }

                @Override
                public void failure(VolleyError volleyError) {
                    hideProgressing();
                    Log.e("VolleyError", "zzz " + volleyError.getMessage());
                    Utils.showWarningAlert(getActivity(), getString(R.string.something_went_wrong));
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
                            Bundle bundle=new Bundle();
                            bundle.putString("Token",resetToken);
                            ChangePasswordFrg changePasswordFrg=new ChangePasswordFrg();
                            changePasswordFrg.setArguments(bundle);
                            homeInteractiveListener.toChangePassword(changePasswordFrg);
                        }
                        else{
                            String errorMessage=response.getString("errorMessage");
                            Utils.showWarningAlert(getActivity(),errorMessage);
                        }


                    } catch (Exception e) {
                        hideProgressing();
                        Log.e("ExceptionSendCode", e.getMessage());
                    }

                }
            });
        }
        else{
            Utils.showWarningAlert(getActivity(), "Please check your internet connection.");
        }

    }

    public void showConfirmationAlert(Context context, String title){
        SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("Are you sure!");
        sweetAlertDialog.setContentText(title);
        sweetAlertDialog.setConfirmText("Yes");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                SharedPreference.removeAllData();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        sweetAlertDialog.setCancelText("No");
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.show();

    }
}
