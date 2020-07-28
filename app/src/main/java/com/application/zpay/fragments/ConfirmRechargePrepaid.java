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
 * Created by Gaganjot Singh on 14/07/2020.
 */
public class ConfirmRechargePrepaid extends zPayFragment {

    private static ConfirmRechargePrepaid mInstance;
    public static ConfirmRechargePrepaid getInstance() {
        if (mInstance == null) {
            mInstance = new ConfirmRechargePrepaid();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prepaidconfirmfrg_layout, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
