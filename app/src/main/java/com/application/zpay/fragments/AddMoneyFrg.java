package com.application.zpay.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.application.zpay.R;
import com.application.zpay.Utilities.AppUrl;
import com.application.zpay.Utilities.SharedPreference;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.Constants;
import com.application.zpay.interfaces.iNetworkCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.open.open_web_sdk.OpenPayment;
import com.open.open_web_sdk.listener.PaymentStatusListener;
import com.open.open_web_sdk.model.TransactionDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Gaganjot Singh on 14/07/2020.
 */
public class AddMoneyFrg extends zPayFragment implements PaymentStatusListener {

    @BindView(R.id.responseText)
    TextView mResponseText;
    //Access key and payment token to be obtained for generating payment
    private String mPaymentToken = "";

    Unbinder unbinder;

    @BindView(R.id.balanceTv)
    TextView mBalanceTv;

    @BindView(R.id.amountEt)
    TextInputEditText mAmountEt;

    @BindView(R.id.signInBtn)
    Button mSignInBtn;

    @BindView(R.id.rootLayout)
    LinearLayout mRootLayout;

    private static AddMoneyFrg mInstance;

    public static AddMoneyFrg getInstance() {
        if (mInstance == null) {
            mInstance = new AddMoneyFrg();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addmoneyfrg_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBalanceTv.setText("Available balance ₹ " + SharedPreference.getBalance());

        mAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    mAmountEt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    mAmountEt.setTypeface(mAmountEt.getTypeface(), Typeface.BOLD);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.setStatusBarColoyr(R.color.toolbar_clr);
        homeInteractiveListener.toggleLogoVisiblity(View.GONE);
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setToolBarTitle("Add Money");
        homeInteractiveListener.toggleNavigationMenuVisibility(false);
    }

    @OnClick(R.id.signInBtn)
    public void onClick() {
        if (mAmountEt.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(getActivity(), mRootLayout, "Please enter amount");
        } else if (mAmountEt.getText().toString().trim().equalsIgnoreCase("0")) {
            Utils.showSnackBar(getActivity(), mRootLayout, "Amount should not be zero");
        } else {


            generateToken();

        }
    }

    private void generateToken() {
        if (Utils.isInternetConnected(getActivity())) {

            showProgressing(getActivity());
            Map<Object, Object> params = new HashMap<Object, Object>();
            String uniqueID = UUID.randomUUID().toString();
            params.put(Constants.AMOUNT, Float.parseFloat(mAmountEt.getText().toString().trim()));
            params.put(Constants.CURRENCY, "INR");
            params.put(Constants.EMAIL_ID, SharedPreference.getUserEmail());
            params.put(Constants.CONTACT_NUMBER, SharedPreference.getUserMobileNumber());
            params.put(Constants.TXN_ID, uniqueID);

            Log.e("Params", params.toString());


            JsonObjectRequest jsonObjectRequest = new
                    JsonObjectRequest(Request.Method.POST,
                            AppUrl.GENERATE_PAYMENT_TOKEN_SANDBOX,
                            new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    hideProgressing();
                                    Log.e("Tag", response.toString());
                                    try {
                                        String amount = response.getString(Constants.AMOUNT);
                                        String currency = response.getString(Constants.CURRENCY);
                                        String txnId = response.getString(Constants.TXN_ID);
                                        mPaymentToken = response.getString("id");
                                        String entity = response.getString("entity");
                                        String status = response.getString("status");
                                        JSONObject customerDetails = response.getJSONObject("customer");
                                        String contactNumber = customerDetails.getString(Constants.CONTACT_NUMBER);
                                        String emailId = customerDetails.getString(Constants.EMAIL_ID);
                                        String customerId = customerDetails.getString("id");

                                        // Initailizig SDK
                                        setPaymentView(mPaymentToken, Constants.ACCESS_KEY_SANDBOX);


                                    } catch (JSONException e) {
                                        hideProgressing();
                                        Log.e("ExceptionFeeds", e.getMessage());
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    hideProgressing();
                                    Log.e("VolleyError", "zzz " + error.getMessage());
                                    Utils.showSnackBar(getActivity(), mRootLayout, getString(R.string.something_went_wrong));
                                }
                            }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<>();
                            headers.put("Content-Type", "application/json");
                            headers.put("Authorization", "Bearer " + Constants.ACCESS_KEY_SANDBOX + ":" + Constants.SECRET_KEY_SANDBOX);
                            return headers;
                        }

                    };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(35000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
            requestQueue.getCache().clear();

        } else {
            Utils.showWarningAlert(getActivity(), "Please check your internet connection.");
        }

    }


    private void setPaymentView(String paymentToken, String accessKey) {
        OpenPayment openPayment = new OpenPayment.Builder()
                .with(getActivity())
                .setPaymentToken(paymentToken)
                .setAccessKey(accessKey)
                .setEnvironment(OpenPayment.Environment.SANDBOX)
                .build();

        openPayment.setPaymentStatusListener(this);

        openPayment.startPayment();
    }

    @Override
    public void onTransactionCompleted(final TransactionDetails transactionDetails) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String response = "On Transaction Completed:\n" +
                        "\n Payment Id: " + transactionDetails.paymentId +
                        "\n Payment Token Id: " + transactionDetails.paymentTokenId +
                        "\n Status: " + transactionDetails.status;

                mResponseText.setText(response);

                   if (Utils.isInternetConnected(getActivity())) {

                showProgressing(getActivity());
                callService(AppUrl.ADD_MONEY, new iNetworkCallback() {
                    @Override
                    public void addParameters(Map<Object, Object> params) {
                        params.put("walletNumber", SharedPreference.getUserMobileNumber());
                        params.put(Constants.BALANCE, Double.parseDouble(mAmountEt.getText().toString().trim()));
                        params.put(Constants.PAYMMENT_ID, transactionDetails.paymentId);
                        params.put(Constants.PAYMENT_TOKEN, transactionDetails.paymentTokenId);
                        params.put(Constants.PAYMENT_STATUS, transactionDetails.status);

                        Log.e("Params", params.toString());

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
                            if (statusCode == 1) {
                                String message=response.getString("errorMessage");
                                String balance=response.getString("Balance");
                                SharedPreference.setBalance(balance);
                                mAmountEt.setText("");
                                Bundle bundle=new Bundle();
                                bundle.putString("Message","Money Added to the wallet successfully");
                                ConfirmMoneyAddedFrg confirmMoneyAddedFrg = new ConfirmMoneyAddedFrg();
                                confirmMoneyAddedFrg.setArguments(bundle);
                                homeInteractiveListener.toConfirmMoneyAdded(confirmMoneyAddedFrg);
                            }
                            else if (statusCode==2){ //Pending
                                String message=response.getString("errorMessage");
                                Utils.showSnackBar(getActivity(),mRootLayout,message);
                                mAmountEt.setText("");

                            }
                            else if (statusCode==3){// Cancelled
                                String message=response.getString("errorMessage");
                                Utils.showSnackBar(getActivity(),mRootLayout,message);
                                mAmountEt.setText("");
                            }
                            else {
                                String errorMessage = response.getString("errorMessage");
                                Utils.showSnackBar(getActivity(),mRootLayout, errorMessage);
                                mAmountEt.setText("");
                            }


                        } catch (Exception e) {
                            hideProgressing();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Utils.showWarningAlert(getActivity(), "Please check your internet connection.");
            }

            }
        });
    }

    @Override
    public void onError(final String s) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mResponseText.setText(String.format("onError: \n%s", s));
            }
        });
    }
}
