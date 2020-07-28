package com.application.zpay.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.zpay.R;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.interfaces.HomeInteractiveListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseActivity extends AppCompatActivity implements HomeInteractiveListener {

    protected void doFragmentTransition(int container, Fragment fragment, FragmentManager manager, boolean addtostack) {
        String fragTag = fragment.getClass().getSimpleName();
        if (manager.findFragmentByTag(fragTag) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(container, fragment, fragTag);
            if (addtostack) {
                transaction.addToBackStack(fragTag);
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commitAllowingStateLoss();
            return;
        }
        try {
            manager.popBackStackImmediate(fragTag, 0);
        } catch (IllegalStateException e) {
        }
    }

    public void showConfirmationAlert(Context context, String title){
        SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("Are you sure!");
        sweetAlertDialog.setContentText(title);
        sweetAlertDialog.setConfirmText("Yes");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finishAffinity();
            }
        });
        sweetAlertDialog.setCancelText("No");
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.show();

    }

    public void onBackPressed() {
        int stackcount = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("StackCount",stackcount+"");

        if (stackcount == 0) {
            finish();
        }

        if (stackcount == 1) {
            showConfirmationAlert(BaseActivity.this,"You want to exit the application? ");

            //finish();
        }
        else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            int index = fragmentManager.getBackStackEntryCount() - 1;
            String currentFragmentName=fragmentManager.getBackStackEntryAt(index).getName();
            fragmentManager.popBackStackImmediate();
        }


    }

    @Override
    public ImageView getToolBarImage() {
        return null;
    }

    @Override
    public TextView getToolBarText() {
        return null;
    }

    @Override
    public void toggleBackArrowVisiblity(int visibility) {

    }

    @Override
    public void toggleLogoVisiblity(int visibility) {

    }

    @Override
    public void toggleNavigationMenuVisibility(boolean visibility) {

    }

    @Override
    public void setToolBarTitle(String title) {

    }

    @Override
    public void setStatusBarColoyr(int colour) {

    }

    @Override
    public void toHome(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toTransactionsFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toPostpaidFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toQrCodeFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toMyAccountFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toIntro1(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);

    }

    @Override
    public void toIntro2(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);

    }

    @Override
    public void toIntro3(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);

    }

    @Override
    public void toIntro4(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);

    }

    @Override
    public void toSupportFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toPrepaidFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toBroadbandFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toDthFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toElectricityFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toGasFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toLandlineFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toWaterBillFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toFaqFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toAddMoney(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toConfirmRechargePrepaid(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toBrowsePlansPrepaid(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toConfirmMoneyAdded(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toChangePassword(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toPrepaidROFFER(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toPrepaidBrowsePlans(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }
}
