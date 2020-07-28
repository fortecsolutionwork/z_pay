package com.application.zpay.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.application.zpay.R;
import com.application.zpay.Utilities.AppUrl;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.activities.LoginActivity;
import com.application.zpay.activities.NewPassActivity;
import com.application.zpay.interfaces.Constants;
import com.application.zpay.interfaces.iNetworkCallback;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Gaganjot Singh on 16/07/2020.
 */
public class ChangePasswordFrg extends zPayFragment {
    
    Unbinder unbinder;

    @BindView(R.id.passwordEt)
    TextInputEditText mPasswordEt;
    
    @BindView(R.id.confirmPassEt)
    TextInputEditText mConfirmPassEt;
    
    @BindView(R.id.changePasswordBtn)
    Button mChangePasswordBtn;
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.changepassword, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Bundle bundle=getArguments();
        if(bundle!=null){
            token=bundle.getString("Token");
        }
    }

    @OnClick(R.id.changePasswordBtn)
    public void onClick() {
        changePassword();
    }

    private void changePassword() {
        if(mPasswordEt.getText().toString().trim().isEmpty()){
            Utils.showWarningAlert(getActivity(),"Please enter new password");
        }else if(mConfirmPassEt.getText().toString().trim().isEmpty()){
            Utils.showWarningAlert(getActivity(),"Please re-enter password");
        }else if(!(mPasswordEt.getText().toString().trim().equals(mConfirmPassEt.getText().toString().trim()))){
            Utils.showWarningAlert(getActivity(),"Passwords does not match.");
        }
        else{
            showProgressing(getActivity());
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
                    Utils.showWarningAlert(getActivity(), getString(R.string.something_went_wrong));

                }

                @Override
                public void response(JSONObject response) {
                    hideProgressing();
                    try {
                        int statusCode = response.getInt("statusCode");
                        Log.e("statusCode",statusCode+"");
                        if(statusCode==1){
                            Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_LONG).show();
                            Bundle bundle=new Bundle();
                            bundle.putString("Message","Password updated successfully.");
                            ConfirmMoneyAddedFrg confirmMoneyAddedFrg=ConfirmMoneyAddedFrg.getInstance();
                            confirmMoneyAddedFrg.setArguments(bundle);
                            homeInteractiveListener.toConfirmMoneyAdded(confirmMoneyAddedFrg);
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
        
    }


    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.setToolBarTitle("Change Password");
        homeInteractiveListener.toggleLogoVisiblity(View.GONE);
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setStatusBarColoyr(R.color.toolbar_clr);
        homeInteractiveListener.toggleNavigationMenuVisibility(false);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
