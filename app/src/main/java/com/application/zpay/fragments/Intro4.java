package com.application.zpay.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.zpay.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Gaganjot Singh on 26/06/2020.
 */
public class Intro4 extends zPayFragment {

    private static Intro4 mInstance;
    public static Intro4 getInstance() {
        if (mInstance == null) {
            mInstance = new Intro4();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_1_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
