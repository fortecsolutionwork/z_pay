package com.application.zpay.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.application.zpay.R;
import com.application.zpay.Utilities.AppUrl;
import com.application.zpay.Utilities.SharedPreference;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.Constants;
import com.application.zpay.interfaces.iNetworkCallback;
import com.application.zpay.models.PrepaidModel;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Gaganjot Singh on 07/07/2020.
 */
public class PrepaidFrg extends zPayFragment {

    PrepaidModel prepaidModel;
    List<PrepaidModel> prepaidList;
    OperatorAdapter operatorAdapter;
    BottomSheetDialog dialog2;
    String commissioToSend;

    List<String> stateList;
    BottomSheetDialog dialog1;
    StateAdapter stateAdapter;
    @BindView(R.id.commissionTv)
    TextView mcCommissionTv;
    @BindView(R.id.rootLayout)
    LinearLayout mRootLayout;

    private int[] mMaterialColors;
    private static final Random RANDOM = new Random();


    @BindView(R.id.prepaidRb)
    RadioButton mprepaidRb;

    @BindView(R.id.postPaidRb)
    RadioButton mPostPaidRb;

    @BindView(R.id.operatorRl)
    RelativeLayout mOperatorRl;

    @BindView(R.id.offersBtn)
    Button mOffersBtn;

    @BindView(R.id.dropdownImg)
    ImageView dropdownImg;

    @BindView(R.id.browsePlansBtn)
    Button mBrowsePlansBtn;

    @BindView(R.id.mobileNumber_et)
    EditText mMobileNumberEt;

    @BindView(R.id.operatorNameTv)
    TextView mOperatorNameTv;

    @BindView(R.id.stateTv)
    TextView mStateTv;

    @BindView(R.id.amountEt)
    EditText mAmountEt;

    @BindView(R.id.finalAmountEt)
    TextView mFinalAmountEt;

    @BindView(R.id.proceedBtn)
    Button proceedBtn;

    @BindView(R.id.stateRl)
    RelativeLayout mStateRl;

    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;

    Unbinder unbinder;
    private static PrepaidFrg mInstance;
    private String type = "Prepaid";

    public static PrepaidFrg getInstance() {
        if (mInstance == null) {
            mInstance = new PrepaidFrg();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prepaidfrg_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        prepaidList = new ArrayList<>();
        showProgressing(getActivity());
        callServiceGet("http://182.77.56.114:8080/zpay/api/getCustomOperator", new iNetworkCallback() {
            @Override
            public void addParameters(Map<Object, Object> params) {

            }

            @Override
            public void failure(VolleyError volleyError) {
                hideProgressing();
                Log.e("VolleyError", "zzz " + volleyError.getMessage());
                Utils.showSnackBar(getActivity(), mRootLayout, getString(R.string.something_went_wrong));
            }

            @Override
            public void response(JSONObject response) {
                hideProgressing();
                try {
                    JSONArray data = response.getJSONArray("SpKey");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject internalData = data.getJSONObject(i);
                        String type = internalData.getString("Type");

                        if (type.equalsIgnoreCase("Prepaid")) {
                            String serviceProvider = internalData.getString("Service Provider");
                            String spKey = internalData.getString("SP Key");

                            prepaidModel = new PrepaidModel(type, serviceProvider, spKey);
                            prepaidList.add(prepaidModel);
                            setOperatorListAdapter();
                        } else {
                            Log.e("OperatorList", prepaidList.toString());
                        }

                        //mOperatorNameTv.setText(prepaidList.get(0).getProviderName());

                    }

                    Log.e("List", prepaidList.toString());


                } catch (Exception e) {
                    hideProgressing();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        stateList = Arrays.asList(getResources().getStringArray(R.array.stateList));
        mMaterialColors = getActivity().getResources().getIntArray(R.array.colors);

        setStateAdapter();

        mStateRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.show();
            }
        });

        mOperatorRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();
            }
        });


        mMobileNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mMobileNumberEt.getText().toString().trim().length() == 10) {
                    showProgressing(getActivity());
                    callServiceGet("http://182.77.56.114:8080/zpay/api/getMobileOperator/" + mMobileNumberEt.getText().toString().trim(), new iNetworkCallback() {
                        @Override
                        public void addParameters(Map<Object, Object> params) {

                        }

                        @Override
                        public void failure(VolleyError volleyError) {
                            hideProgressing();
                            Log.e("VolleyError", "zzz " + volleyError.getMessage());
                            Utils.showSnackBar(getActivity(), mRootLayout, getString(R.string.something_went_wrong));
                        }

                        @Override
                        public void response(JSONObject response) {
                            hideProgressing();
                            Utils.hideKeyboard(getActivity());
                            try {
                                int statusCode = response.getInt("statusCode");
                                if (statusCode == 1) {
                                    JSONObject records = response.getJSONObject("records");
                                    String operator = records.getString("Operator");
                                    String circle = records.getString("circle");
                                    String comcircle = records.getString("comcircle");
                                    mOperatorNameTv.setText(operator);
                                    mStateTv.setText(comcircle);
                                    if (records.has("segment")) {
                                        String segment = records.getString("segment");
                                        if (segment.equalsIgnoreCase("Prepaid")) {
                                            mprepaidRb.setChecked(true);
                                            type = "Prepaid";
                                        } else {
                                            mPostPaidRb.setChecked(true);
                                            type = "Postpaid";
                                        }
                                    }

                                    mAmountEt.setText(SharedPreference.getRechargeAmount());


                                } else {
                                    //JSONObject records = response.getJSONObject("records");
                                    String errorMessage = response.getString("errorMessage");
                                    Utils.showSnackBar(getActivity(), mRootLayout, errorMessage);
                                    //Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                hideProgressing();
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFinalAmountEt.setText("₹ " + mAmountEt.getText().toString().trim());
                if (s.length() != 0) {
                    calculateCommission();
                } else {
                    mcCommissionTv.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mOffersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMobileNumberEt.getText().toString().trim().isEmpty()) {
                    Utils.showSnackBar(getActivity(), mRootLayout, "Enter mobile number");
                } else if (mMobileNumberEt.getText().toString().trim().length() < 10) {
                    Utils.showSnackBar(getActivity(), mRootLayout, "Mobile number should be of 10 digits");
                } else {
                    SharedPreference.setRechargeAmount("");
                    Bundle bundle = new Bundle();
                    bundle.putString("MobileNumber", mMobileNumberEt.getText().toString().trim());
                    PrepaidROffers prepaidROffers = PrepaidROffers.getInstance();
                    prepaidROffers.setArguments(bundle);
                    homeInteractiveListener.toPrepaidROFFER(prepaidROffers);

                }
            }
        });

        mBrowsePlansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMobileNumberEt.getText().toString().trim().isEmpty()) {
                    Utils.showSnackBar(getActivity(), mRootLayout, "Enter mobile number");
                } else if (mMobileNumberEt.getText().toString().trim().length() < 10) {
                    Utils.showSnackBar(getActivity(), mRootLayout, "Mobile number should be of 10 digits");
                } else {
                    SharedPreference.setRechargeAmount("");
                    Bundle bundle = new Bundle();
                    bundle.putString("MobileNumber", mMobileNumberEt.getText().toString().trim());
                    PrepaidBrowsePlans prepaidROffers = PrepaidBrowsePlans.getInstance();
                    prepaidROffers.setArguments(bundle);
                    homeInteractiveListener.toPrepaidBrowsePlans(prepaidROffers);

                }

            }
        });


    }

    private void calculateCommission() {
        double commissionRate, totalCommission, markedprice;
        markedprice = Double.parseDouble(mAmountEt.getText().toString().trim());
        commissionRate = 3;
        totalCommission = (markedprice * commissionRate) / 100;
        Log.e("Total commission=", totalCommission + "");
        mcCommissionTv.setText("₹ " + String.valueOf(totalCommission));
        commissioToSend = "";
        commissioToSend = String.valueOf(totalCommission);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setStateAdapter() {
        View view = getLayoutInflater().inflate(R.layout.state_layout, null);

        RecyclerView recyclerVieww = (RecyclerView) view.findViewById(R.id.recyclerVieww);
        recyclerVieww.setHasFixedSize(true);
        recyclerVieww.setLayoutManager(new LinearLayoutManager(getActivity()));

        stateAdapter = new StateAdapter(getActivity(), stateList);
        recyclerVieww.setAdapter(stateAdapter);

        dialog1 = new BottomSheetDialog(getActivity());
        dialog1.setContentView(view);
    }

    private void setOperatorListAdapter() {
        View view = getLayoutInflater().inflate(R.layout.operator_list_layout, null);

        RecyclerView recyclerVieww = (RecyclerView) view.findViewById(R.id.recyclerVieww);
        recyclerVieww.setHasFixedSize(true);
        recyclerVieww.setLayoutManager(new LinearLayoutManager(getActivity()));

        operatorAdapter = new OperatorAdapter(getActivity(), prepaidList);
        recyclerVieww.setAdapter(operatorAdapter);

        dialog2 = new BottomSheetDialog(getActivity());
        dialog2.setContentView(view);
    }


    @OnClick(R.id.proceedBtn)
    public void onClick() {
        doRecharge();
    }

    private void doRecharge() {
        Utils.hideKeyboard(getActivity());
        if (mMobileNumberEt.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(getActivity(),mRootLayout,"Enter mobile number");
        } else if (mMobileNumberEt.getText().toString().trim().length() < 10) {
            Utils.showSnackBar(getActivity(),mRootLayout,"Mobile number should be of 10 digits");
        } else if (mOperatorNameTv.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(getActivity(),mRootLayout,"Select operator");
        } else if (mOperatorNameTv.getText().toString().trim().equalsIgnoreCase("Select Operator")) {
            Utils.showSnackBar(getActivity(),mRootLayout,"Select operator");
        } else if (mStateTv.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(getActivity(),mRootLayout,"Select state");
        } else if (mStateTv.getText().toString().trim().equalsIgnoreCase("Select State")) {
            Utils.showSnackBar(getActivity(),mRootLayout,"Select state");
        } else if (mAmountEt.getText().toString().trim().isEmpty()) {
            Utils.showSnackBar(getActivity(),mRootLayout,"Enter amount to recharge");
        } else if (Integer.parseInt(mAmountEt.getText().toString().trim()) < 10) {
            Utils.showSnackBar(getActivity(),mRootLayout,"Amount should not be less that 10");
        } else {
            if (Utils.isInternetConnected(getActivity())) {
                showProgressing(getActivity());
                callService(AppUrl.PREPAID, new iNetworkCallback() {
                    @Override
                    public void addParameters(Map<Object, Object> params) {

                        params.put("mobileNumber", mMobileNumberEt.getText().toString().trim());
                        params.put(Constants.AMOUNT, mAmountEt.getText().toString().trim());
                        params.put(Constants.OPERATOR_NAME, mOperatorNameTv.getText().toString().trim());
                        params.put(Constants.OPERATOR_STATE, mStateTv.getText().toString().trim());
                        params.put(Constants.SESSION_ID, SharedPreference.getUserMobileNumber());
                        params.put(Constants.OPERATOR_TYPE, type);
                        params.put(Constants.CASHBACK, commissioToSend);
                        params.put(Constants.OS_TYPE, "Android");
                        params.put(Constants.LATITUDE, "");
                        params.put(Constants.LONGITUDE, "");

                        Log.e("Params", params.toString());


                    }

                    @Override
                    public void failure(VolleyError volleyError) {
                        hideProgressing();
                        Log.e("VolleyError", "zzz " + volleyError.getMessage());
                        Utils.showSnackBar(getActivity(),mRootLayout,getString(R.string.something_went_wrong));
                    }

                    @Override
                    public void response(JSONObject response) {
                        hideProgressing();
                        try {
                            int statusCode = response.getInt("statusCode");
                            Log.e("statusCode", statusCode + "");
                            if (statusCode == 1) {// accepted

                                Toast.makeText(getActivity(), "Recharge Successful.", Toast.LENGTH_LONG).show();
                                String balance = response.getString(Constants.BALANCE);
                                SharedPreference.setBalance(balance);
                                Bundle bundle = new Bundle();
                                bundle.putString("Message", "Recharge done successfully");
                                ConfirmMoneyAddedFrg confirmMoneyAddedFrg = ConfirmMoneyAddedFrg.getInstance();
                                confirmMoneyAddedFrg.setArguments(bundle);
                                homeInteractiveListener.toConfirmMoneyAdded(confirmMoneyAddedFrg);

                            }
                            else if (statusCode==2){// balance low
                                String errorMessage = response.getString("errorMessage");
                                Utils.showSnackBar(getActivity(),mRootLayout,errorMessage);
                            }
                            else if (statusCode==3){ //pending
                                String errorMessage = response.getString("errorMessage");
                               // Utils.showSnackBar(getActivity(),mRootLayout,errorMessage);
                                Bundle bundle = new Bundle();
                                bundle.putString("Message", errorMessage);
                                ConfirmMoneyAddedFrg confirmMoneyAddedFrg = ConfirmMoneyAddedFrg.getInstance();
                                confirmMoneyAddedFrg.setArguments(bundle);
                                homeInteractiveListener.toConfirmMoneyAdded(confirmMoneyAddedFrg);

                            }

                            else {
                                String errorMessage = response.getString("errorMessage");
                                Utils.showSnackBar(getActivity(),mRootLayout,errorMessage);
                                //Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                //Utils.showWarningAlert(getActivity(), errorMessage);
                            }

                        } catch (Exception e) {
                            hideProgressing();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            } else {
                Utils.showSnackBar(getActivity(),mRootLayout, "Please check your internet connection.");
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.setToolBarTitle("Prepaid");
        homeInteractiveListener.toggleLogoVisiblity(View.GONE);
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setStatusBarColoyr(R.color.toolbar_clr);
        homeInteractiveListener.toggleNavigationMenuVisibility(false);
    }

    @OnClick({R.id.prepaidRb, R.id.postPaidRb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.prepaidRb:
                mPostPaidRb.setChecked(false);
                mprepaidRb.setChecked(true);
                break;
            case R.id.postPaidRb:
                mPostPaidRb.setChecked(true);
                mprepaidRb.setChecked(false);
                break;
        }
    }

    //--------------------------------------Location Adapter-----------------------------------------
    public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> {

        Context context;
        List<String> childFeedList;
        private int lastCheckedPosition = -1;

        public StateAdapter(Context context, List<String> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_list_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {


            holder.mLocationName.setText(childFeedList.get(position));

            holder.mIcon.setInitials(true);
            holder.mIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            holder.mIcon.setLetter(childFeedList.get(position).substring(0, 1));
            holder.mIcon.setLetterSize(16);
            holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

            if (lastCheckedPosition == position) {
                holder.mCardView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                holder.mLocationName.setTextColor(Color.parseColor("#010101"));

            } else {
                holder.mCardView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.mLocationName.setTextColor(Color.parseColor("#010101"));
            }

            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = position;
                    mStateTv.setText(childFeedList.get(position));
                    // selectedLocationid = childFeedsModel.getLocationId();
                    notifyDataSetChanged();
                    dialog1.dismiss();
                }
            });


        }

        @Override
        public int getItemCount() {
            return childFeedList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mLocationName;
            MaterialLetterIcon mIcon;
            CardView mCardView;


            public MyViewHolder(View itemView) {
                super(itemView);
                mLocationName = (TextView) itemView.findViewById(R.id.locationName_tv);
                mIcon = (MaterialLetterIcon) itemView.findViewById(R.id.imageIcon);
                mCardView = (CardView) itemView.findViewById(R.id.cardView);

            }
        }
    }


    //--------------------------------------Operator List Adapter-----------------------------------------
    public class OperatorAdapter extends RecyclerView.Adapter<OperatorAdapter.MyViewHolder> {

        Context context;
        List<PrepaidModel> childFeedList;
        private int lastCheckedPosition = -1;

        public OperatorAdapter(Context context, List<PrepaidModel> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.operator_list_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {


            holder.mLocationName.setText(childFeedList.get(position).getProviderName());

            holder.mIcon.setInitials(true);
            holder.mIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            holder.mIcon.setLetter(childFeedList.get(position).getProviderName().substring(0, 1));
            holder.mIcon.setLetterSize(16);
            holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

            if (lastCheckedPosition == position) {
                holder.mCardView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                holder.mLocationName.setTextColor(Color.parseColor("#010101"));

            } else {
                holder.mCardView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.mLocationName.setTextColor(Color.parseColor("#010101"));
            }

            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = position;
                    mOperatorNameTv.setText(childFeedList.get(position).getProviderName());
                    // selectedLocationid = childFeedsModel.getLocationId();
                    notifyDataSetChanged();
                    dialog2.dismiss();
                }
            });


        }

        @Override
        public int getItemCount() {
            return childFeedList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mLocationName;
            MaterialLetterIcon mIcon;
            CardView mCardView;


            public MyViewHolder(View itemView) {
                super(itemView);
                mLocationName = (TextView) itemView.findViewById(R.id.locationName_tv);
                mIcon = (MaterialLetterIcon) itemView.findViewById(R.id.imageIcon);
                mCardView = (CardView) itemView.findViewById(R.id.cardView);

            }
        }
    }
}
