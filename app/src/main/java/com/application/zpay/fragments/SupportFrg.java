package com.application.zpay.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.zpay.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

/**
 * Created by Gaganjot Singh on 07/07/2020.
 */
public class SupportFrg extends zPayFragment {

    private static SupportFrg mInstance;
    public static SupportFrg getInstance() {
        if (mInstance == null) {
            mInstance = new SupportFrg();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supportfrg_layout, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.setToolBarTitle("Support");
        homeInteractiveListener.toggleLogoVisiblity(View.GONE);
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setStatusBarColoyr(R.color.toolbar_clr);
        homeInteractiveListener.toggleNavigationMenuVisibility(false);
    }
}
