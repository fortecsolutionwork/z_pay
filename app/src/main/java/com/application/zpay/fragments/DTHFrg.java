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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.application.zpay.R;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.iNetworkCallback;
import com.application.zpay.models.PrepaidModel;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Gaganjot Singh on 09/07/2020.
 */
public class DTHFrg extends zPayFragment {

    String nextRechargeDate="";
    String lastRechargeAmount="";
    String planName="";
    String monthlyRecharge="";
    String lastRechargeDate="";
    String balance="";
    String customerName="";
    String status="";

    PrepaidModel prepaidModel;
    List<PrepaidModel> prepaidList;
    OperatorAdapter operatorAdapter;
    BottomSheetDialog dialog2;

    @BindView(R.id.operatorRl)
    RelativeLayout mOperatorRl;

    @BindView(R.id.finalAmountEt)
    TextView mFinalAmountEt;
    @BindView(R.id.amountEt)
    EditText mAmountEt;

    private int[] mMaterialColors;
    private static final Random RANDOM = new Random();


    @BindView(R.id.customerDetailsBtn)
    Button mCustomerDetailsBtn;

    @BindView(R.id.dthNumber_et)
    EditText mDthNumberEt;

    @BindView(R.id.operatorNameTv)
    TextView mOperatorNameTv;

    Unbinder unbinder;
    private static DTHFrg mInstance;

    public static DTHFrg getInstance() {
        if (mInstance == null) {
            mInstance = new DTHFrg();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dthfrg_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepaidList = new ArrayList<>();
        mMaterialColors = getActivity().getResources().getIntArray(R.array.colors);

        showProgressing(getActivity());
        callServiceGet("http://182.77.56.114:8080/zpay/api/getCustomOperator", new iNetworkCallback() {
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
                    JSONArray data = response.getJSONArray("SpKey");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject internalData = data.getJSONObject(i);
                        String type = internalData.getString("Type");

                        if (type.equalsIgnoreCase("DTH")) {
                            String serviceProvider = internalData.getString("Service Provider");
                            String spKey = internalData.getString("SP Key");

                            prepaidModel = new PrepaidModel(type, serviceProvider, spKey);
                            prepaidList.add(prepaidModel);
                            setOperatorListAdapter();
                        } else {
                            Log.e("Operator List", prepaidList.toString());
                        }

                    }
                    //mOperatorNameTv.setText(prepaidList.get(0).getProviderName());

                } catch (Exception e) {
                    hideProgressing();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        mOperatorRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();
            }
        });

        mAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFinalAmountEt.setText("₹ " + mAmountEt.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.setToolBarTitle("DTH");
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

    @OnClick(R.id.customerDetailsBtn)
    public void onViewClicked() {
        if (mDthNumberEt.getText().toString().trim().isEmpty()) {
            Utils.showWarningAlert(getActivity(), "Enter valid DTH number");
        } else if (mOperatorNameTv.getText().toString().trim().isEmpty()) {
            Utils.showWarningAlert(getActivity(), "Select operator");
        } else {
            getCustomerDetails();
        }

    }

    private void getCustomerDetails() {
        showProgressing(getActivity());
        callServiceGet("http://182.77.56.114:8080/zpay/api/getDTHCustomer/"+mDthNumberEt.getText().toString().trim(), new iNetworkCallback() {
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
                        JSONArray results = response.getJSONArray("records");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject internalData = results.getJSONObject(i);
                            if (internalData.has("NextRechargeDate")) {
                               nextRechargeDate = internalData.getString("NextRechargeDate");
                            }if (internalData.has("lastrechargeamount")) {
                               lastRechargeAmount = internalData.getString("lastrechargeamount");
                            }if (internalData.has("planname")) {
                                 planName = internalData.getString("planname");
                            }if (internalData.has("MonthlyRecharge")) {
                                 monthlyRecharge = internalData.getString("MonthlyRecharge");
                            }if (internalData.has("lastrechargedate")) {
                                 lastRechargeDate = internalData.getString("lastrechargedate");
                            }if (internalData.has("Balance")) {
                                  balance = internalData.getString("Balance");
                            }if (internalData.has("customerName")) {
                                 customerName = internalData.getString("customerName");
                            }if (internalData.has("status")) {
                                 status = internalData.getString("status");
                            }


                            Utils.showDialog(getActivity(),customerName,monthlyRecharge,nextRechargeDate,status,planName,balance);

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
