package com.application.zpay.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.zpay.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Gaganjot Singh on 08/07/2020.
 */
public class WaterBill extends zPayFragment {

    private static WaterBill mInstance;
    public static WaterBill getInstance() {
        if (mInstance == null) {
            mInstance = new WaterBill();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waterbillfrg_layout, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.setToolBarTitle("Water");
        homeInteractiveListener.toggleLogoVisiblity(View.GONE);
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setStatusBarColoyr(R.color.toolbar_clr);
        homeInteractiveListener.toggleNavigationMenuVisibility(false);
    }
}
