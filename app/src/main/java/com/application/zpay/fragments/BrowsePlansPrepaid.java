package com.application.zpay.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.application.zpay.R;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.iNetworkCallback;

import org.json.JSONObject;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Gaganjot Singh on 14/07/2020.
 */
public class BrowsePlansPrepaid extends zPayFragment {
    String mobileNumber;

    Unbinder unbinder;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.noData_ll)
    LinearLayout mNoDataLl;


    private static BrowsePlansPrepaid mInstance;
    public static BrowsePlansPrepaid getInstance() {
        if (mInstance == null) {
            mInstance = new BrowsePlansPrepaid();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.browseplans_prepaid_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle=getArguments();
        if(bundle!=null){
            mobileNumber=bundle.getString("MobileNumber");
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
                Utils.showWarningAlert(getActivity(), getString(R.string.something_went_wrong));

            }

            @Override
            public void response(JSONObject response) {


            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
