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
import com.application.zpay.Utilities.SharedPreference;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.Constants;
import com.application.zpay.interfaces.iNetworkCallback;
import com.application.zpay.models.ROfferModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PrepaidROffers extends zPayFragment {

    String mobileNumber;
    ROfferModel rOfferModel;
    List<ROfferModel> parentList;
    ROfferAdapter rOfferAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.noDataTv)
    TextView mNoDataTv;

    @BindView(R.id.rootLayout)
    LinearLayout mRootLayout;

    private static PrepaidROffers mInstance;
    public static PrepaidROffers getInstance() {
        if (mInstance == null) {
            mInstance = new PrepaidROffers();
        }
        return mInstance;
    }


    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prepaidrofferfrg_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rOfferAdapter = new ROfferAdapter(getActivity(), parentList);
        mRecyclerView.setAdapter(rOfferAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mobileNumber = bundle.getString("MobileNumber");
        }

        showProgressing(getActivity());
        callServiceGet("http://182.77.56.114:8080/zpay/api/getPlanMobileROFFER/" + mobileNumber, new iNetworkCallback() {
            @Override
            public void addParameters(Map<Object, Object> params) {

            }

            @Override
            public void failure(VolleyError volleyError) {
                hideProgressing();
                Log.e("VolleyError", "zzz " + volleyError.getMessage());
                Utils.showSnackBar(getActivity(), mRootLayout, getString(R.string.something_went_wrong));
                //Utils.showWarningAlert(getActivity(), getString(R.string.something_went_wrong));
            }

            @Override
            public void response(JSONObject response) {
                hideProgressing();
                try {
                    int statusCode = response.getInt("statusCode");
                    if (statusCode == 1) {
                        JSONArray results = response.getJSONArray("records");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject internalData = results.getJSONObject(i);
                            String rechargeCost = internalData.getString("rs");
                            String description = internalData.getString("desc");
                            rOfferModel = new ROfferModel(rechargeCost, description);
                            parentList.add(rOfferModel);
                        }

                        mRecyclerView.setAdapter(rOfferAdapter);
                        rOfferAdapter.notifyDataSetChanged();


                    } else {
                        String errorMessage = response.getString("errorMessage");
                        mRecyclerView.setVisibility(View.GONE);
                        mNoDataTv.setVisibility(View.VISIBLE);
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
        homeInteractiveListener.setToolBarTitle("ROffers");
        homeInteractiveListener.toggleLogoVisiblity(View.GONE);
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setStatusBarColoyr(R.color.toolbar_clr);
        homeInteractiveListener.toggleNavigationMenuVisibility(false);
    }


    //--------------------------------------Roffer Adater-----------------------------------------
    public class ROfferAdapter extends RecyclerView.Adapter<ROfferAdapter.MyViewHolder> {

        Context context;
        List<ROfferModel> childFeedList;

        public ROfferAdapter(Context context, List<ROfferModel> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_offer_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ROfferModel childFeedsModel = childFeedList.get(position);
            holder.mRechargeCost.setText("₹ " + childFeedsModel.getRechargeCost());
            holder.mDescription.setText(childFeedsModel.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrepaidFrg prepaidFrg = new PrepaidFrg();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.AMOUNT, childFeedsModel.getRechargeCost());
                    prepaidFrg.setArguments(bundle);
                    SharedPreference.setRechargeAmount(childFeedsModel.getRechargeCost());
                    homeInteractiveListener.toPrepaidFrg(prepaidFrg);
                }
            });
        }

        @Override
        public int getItemCount() {
            return childFeedList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mRechargeCost;
            TextView mDescription;

            public MyViewHolder(View itemView) {
                super(itemView);
                mRechargeCost = (TextView) itemView.findViewById(R.id.costTv);
                mDescription = (TextView) itemView.findViewById(R.id.descTv);

            }
        }
    }


}
