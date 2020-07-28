package com.application.zpay.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.application.zpay.R;
import com.application.zpay.Utilities.AppUrl;
import com.application.zpay.Utilities.SharedPreference;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.Constants;
import com.application.zpay.interfaces.iNetworkCallback;
import com.application.zpay.models.PlansModel;
import com.application.zpay.models.TransactionModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Gaganjot Singh on 26/06/2020.
 */
public class TransactionsFrg extends zPayFragment {

    Unbinder unbinder;

    TransactionModel transactionModel;
    List<TransactionModel> parentList;
    TransactionAdapter transactionAdapter;

    private static TransactionsFrg mInstance;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.noDataTv)
    TextView mNoDataTv;
    @BindView(R.id.rootLayout)
    LinearLayout mRootLayout;

    public static TransactionsFrg getInstance() {
        if (mInstance == null) {
            mInstance = new TransactionsFrg();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        transactionAdapter=new TransactionAdapter(getActivity(),parentList);
        mRecyclerView.setAdapter(transactionAdapter);

        showProgressing(getActivity());
        callService(AppUrl.TXN_HISTORY, new iNetworkCallback() {
            @Override
            public void addParameters(Map<Object, Object> params) {
                params.put(Constants.USER_MOBILE, SharedPreference.getUserMobileNumber());
            }

            @Override
            public void failure(VolleyError volleyError) {
                hideProgressing();
                Log.e("VolleyError", "zzz " + volleyError.getMessage());
                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                ;

            }

            @Override
            public void response(JSONObject response) {
                hideProgressing();
                try {
                    int statusCode = response.getInt("statusCode");
                    Log.e("statusCode", statusCode + "");
                    if (statusCode == 1) {

                        JSONArray records = response.getJSONArray("records");
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject internalData = records.getJSONObject(i);
                            String date = internalData.getString("date");
                            String operatorType = internalData.getString("processType");
                            String rechargeAmount = internalData.getString("rechargeAmount");
                            String operatorName = internalData.getString("operatorName");
                            String rechargeNumber = internalData.getString("account");
                            String rechargeStatus = internalData.getString("status");

                            transactionModel = new TransactionModel(date, operatorType, rechargeAmount, operatorName, rechargeNumber, rechargeStatus);
                            parentList.add(transactionModel);
                        }

                        mRecyclerView.setAdapter(transactionAdapter);
                        transactionAdapter.notifyDataSetChanged();


                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        mNoDataTv.setVisibility(View.VISIBLE);
                       /* String errorMessage = response.getString("errorMessage");
                        Utils.showSnackBar(getActivity(), mRootLayout, errorMessage);*/
                    }

                } catch (Exception e) {
                    hideProgressing();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

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
        homeInteractiveListener.toggleBackArrowVisiblity(View.GONE);
        homeInteractiveListener.setToolBarTitle("Transaction History");
        homeInteractiveListener.toggleNavigationMenuVisibility(true);
    }





    //--------------------------------------Roffer Adater-----------------------------------------
    public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

        Context context;
        List<TransactionModel> childFeedList;

        public TransactionAdapter(Context context, List<TransactionModel> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;

        }

        @Override
        public TransactionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_designs, parent, false);
            return new TransactionAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TransactionAdapter.MyViewHolder holder, int position) {
            TransactionModel childFeedsModel = childFeedList.get(position);
            holder.mRechargeAmountTv.setText("₹ "+childFeedsModel.getRechargeAmount());
            holder.mOperatorNameTv.setText(childFeedsModel.getOperatorName());
            holder.mOperatorTypeTv.setText(childFeedsModel.getOperatorType());
            holder.mDateTimeTv.setText(childFeedsModel.getDate());

        }

        @Override
        public int getItemCount() {
            return childFeedList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mOperatorNameTv;
            TextView mDateTimeTv;
            TextView mOperatorTypeTv;
            TextView mRechargeAmountTv;

            public MyViewHolder(View itemView) {
                super(itemView);
                mOperatorNameTv = (TextView) itemView.findViewById(R.id.operatorNameTv);
                mDateTimeTv = (TextView) itemView.findViewById(R.id.dateTimeTv);
                mOperatorTypeTv = (TextView) itemView.findViewById(R.id.operatorTypeTv);
                mRechargeAmountTv = (TextView) itemView.findViewById(R.id.rechargeAmountTv);


            }
        }
    }
}
