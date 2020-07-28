package com.application.zpay.fragments;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import com.application.zpay.activities.HomeActivity;
import com.application.zpay.interfaces.HomeInteractiveListener;


public class BaseFragment extends Fragment {
    public Activity activity;
    protected HomeInteractiveListener homeInteractiveListener;

    public void onAttach(Context context) {
        super.onAttach(context);
        activity = null;
        if (context instanceof HomeActivity) {
            activity = (HomeActivity) context;
        }

        homeInteractiveListener = (HomeInteractiveListener) activity;
    }


}
