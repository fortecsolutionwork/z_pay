package com.application.zpay.interfaces;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public interface HomeInteractiveListener {
    ImageView getToolBarImage();
    TextView getToolBarText();
    void toggleBackArrowVisiblity(int visibility);
    void toggleLogoVisiblity(int visibility);
    void toggleNavigationMenuVisibility(boolean visibility);
    void setToolBarTitle(String title);
    void setStatusBarColoyr(int colour);

    void toHome(Fragment frg);
    void toTransactionsFrg(Fragment frg);
    void toPostpaidFrg(Fragment frg);
    void toQrCodeFrg(Fragment frg);
    void toMyAccountFrg(Fragment frg);
    void toIntro1(Fragment frg);
    void toIntro2(Fragment frg);
    void toIntro3(Fragment frg);
    void toIntro4(Fragment frg);
    void toSupportFrg(Fragment frg);
    void toPrepaidFrg(Fragment frg);
    void toBroadbandFrg(Fragment frg);
    void toDthFrg(Fragment frg);
    void toElectricityFrg(Fragment frg);
    void toGasFrg(Fragment frg);
    void toLandlineFrg(Fragment frg);
    void toWaterBillFrg(Fragment frg);
    void toFaqFrg(Fragment frg);
    void toAddMoney(Fragment frg);
    void toConfirmRechargePrepaid(Fragment frg);
    void toBrowsePlansPrepaid(Fragment frg);
    void toConfirmMoneyAdded(Fragment frg);
    void toChangePassword(Fragment frg);
    void toPrepaidROFFER(Fragment frg);
    void toPrepaidBrowsePlans(Fragment frg);



}
