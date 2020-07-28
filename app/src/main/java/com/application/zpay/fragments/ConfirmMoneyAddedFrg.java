package com.application.zpay.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.application.zpay.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Gaganjot Singh on 16/07/2020.
 */
public class ConfirmMoneyAddedFrg extends zPayFragment {
    Unbinder unbinder;


    @BindView(R.id.homeBtn)
    Button mHomeBtn;

    private static ConfirmMoneyAddedFrg mInstance;
    @BindView(R.id.messageTv)
    TextView mMessageTv;
    private String message;

    public static ConfirmMoneyAddedFrg getInstance() {
        if (mInstance == null) {
            mInstance = new ConfirmMoneyAddedFrg();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_moneyadded_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            message = bundle.getString("Message");
        }
        mMessageTv.setText(message);
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
        homeInteractiveListener.setToolBarTitle("Confirmation");
        homeInteractiveListener.toggleNavigationMenuVisibility(false);
    }

    @OnClick(R.id.homeBtn)
    public void onClick() {
        homeInteractiveListener.toHome(HomeFragment.getInstance());
    }
}
