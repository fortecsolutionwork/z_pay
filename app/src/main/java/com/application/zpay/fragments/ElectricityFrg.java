package com.application.zpay.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.application.zpay.R;
import com.application.zpay.Utilities.AppUrl;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.iNetworkCallback;
import com.application.zpay.models.ROfferModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Gaganjot Singh on 09/07/2020.
 */
public class ElectricityFrg extends zPayFragment {

    Unbinder unbinder;

    @BindView(R.id.operatorNameTv)
    TextView mOperatorNameTv;

    @BindView(R.id.accountName_et)
    EditText mAccountNameEt;

    @BindView(R.id.fecthBill)
    Button mFecthBill;

    private static ElectricityFrg mInstance;
    public static ElectricityFrg getInstance() {
        if (mInstance == null) {
            mInstance = new ElectricityFrg();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.electricityfrg_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.setToolBarTitle("Electricity");
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


    @OnClick(R.id.fecthBill)
    public void onViewClicked() {
        if(mOperatorNameTv.getText().toString().trim().isEmpty()){
            Utils.showWarningAlert(getActivity(),"Select Operator ");
        }
        else if(mAccountNameEt.getText().toString().trim().isEmpty()){
            Utils.showWarningAlert(getActivity(),"Enter customer account number");
        }
        else{
            getBillDetails();
        }
    }

    private void getBillDetails() {
        showProgressing(getActivity());
        callService("http://182.77.56.114:8080/zpay/api/getElectricityCustInfo"+ mAccountNameEt.getText().toString().trim()+"/"+mOperatorNameTv.getText().toString().trim(), new iNetworkCallback() {
            @Override
            public void addParameters(Map<Object, Object> params) {

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
                    Log.e("statusCode", statusCode + "");
                    if (statusCode == 1) {
                        JSONArray results=response.getJSONArray("records");
                        for(int  i=0;i<results.length();i++){
                            JSONObject internalData=results.getJSONObject(i);
                            String rechargeCost=internalData.getString("CustomerName");
                            String billNumber=internalData.getString("BillNumber");
                            String billamount=internalData.getString("Billamount");
                            String billdate=internalData.getString("Billdate");
                            String dueDate=internalData.getString("Duedate");

                        }

                    } else {
                        String errorMessage = response.getString("errorMessage");
                        Utils.showWarningAlert(getActivity(), errorMessage);
                    }

                } catch (Exception e) {
                    hideProgressing();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
