package com.application.zpay.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.application.zpay.R;
import com.application.zpay.Utilities.SharedPreference;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.Constants;
import com.application.zpay.interfaces.iNetworkCallback;
import com.application.zpay.models.PlansModel;
import com.application.zpay.models.ROfferModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PrepaidBrowsePlans extends zPayFragment {

    String mobileNumber;
    PlansModel rOfferModel;
    List<PlansModel> parentList;
    ROfferAdapter rOfferAdapter;

    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.noDataTv)
    TextView mNoDataTv;

    private static PrepaidBrowsePlans mInstance;
    public static PrepaidBrowsePlans getInstance() {
        if (mInstance == null) {
            mInstance = new PrepaidBrowsePlans();
        }
        return mInstance;
    }


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
        rOfferAdapter=new ROfferAdapter(getActivity(),parentList);
        mRecyclerView.setAdapter(rOfferAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mobileNumber = bundle.getString("MobileNumber");
        }


        showProgressing(getActivity());
        callServiceGet("http://182.77.56.114:8080/zpay/api/getPlanMobileSIMPLE/" + mobileNumber, new iNetworkCallback() {
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
                    if (statusCode == 1) {
                        JSONObject results = response.getJSONObject("records");
                        JSONArray rateCutterArray=results.getJSONArray("RATE CUTTER");
                        for (int i = 0; i < rateCutterArray.length(); i++) {
                            JSONObject internalData = rateCutterArray.getJSONObject(i);
                            String rechargeCost = internalData.getString("rs");
                            String validity = internalData.getString("validity");
                            String description = internalData.getString("desc");
                            rOfferModel = new PlansModel(rechargeCost,validity,description);
                            parentList.add(rOfferModel);
                        }
                        JSONArray topUpArray=results.getJSONArray("TOPUP");
                        for (int i = 0; i < topUpArray.length(); i++) {
                            JSONObject internalData = topUpArray.getJSONObject(i);
                            String rechargeCost = internalData.getString("rs");
                            String validity = internalData.getString("validity");
                            String description = internalData.getString("desc");
                            rOfferModel = new PlansModel(rechargeCost,validity,description);
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
        homeInteractiveListener.setToolBarTitle("Plans");
        homeInteractiveListener.toggleLogoVisiblity(View.GONE);
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setStatusBarColoyr(R.color.toolbar_clr);
        homeInteractiveListener.toggleNavigationMenuVisibility(false);
    }



    //--------------------------------------Roffer Adater-----------------------------------------
    public class ROfferAdapter extends RecyclerView.Adapter<ROfferAdapter.MyViewHolder> {

        Context context;
        List<PlansModel> childFeedList;

        public ROfferAdapter(Context context, List<PlansModel> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;

        }

        @Override
        public ROfferAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_offer_design, parent, false);
            return new ROfferAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ROfferAdapter.MyViewHolder holder, int position) {
            PlansModel childFeedsModel = childFeedList.get(position);
            holder.mRechargeCost.setText("₹ "+childFeedsModel.getRechargeCost());
            holder.mDescription.setText(childFeedsModel.getDesc());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrepaidFrg prepaidFrg=new PrepaidFrg();
                    Bundle bundle=new Bundle();
                    bundle.putString(Constants.AMOUNT,childFeedsModel.getRechargeCost());
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
