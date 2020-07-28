package com.application.zpay.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.zpay.R;
import com.application.zpay.Utilities.SharedPreference;
import com.application.zpay.Utilities.Utils;
import com.application.zpay.fragments.AddMoneyFrg;
import com.application.zpay.fragments.HomeFragment;
import com.application.zpay.fragments.MyAccountFrg;
import com.application.zpay.fragments.PrepaidFrg;
import com.application.zpay.fragments.QrCodeFrg;
import com.application.zpay.fragments.SupportFrg;
import com.application.zpay.fragments.TransactionsFrg;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.ButterKnife;

public class HomeActivity extends zPayActivity implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {
    DrawerLayout drawer;
    TabLayout tabLayout;
    private View navHeader;
    private NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    TextView mUserName,mUserMobile,mToolbarTitle;
    ImageView mBackArrow,mToolbarLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tabLayout = (TabLayout) findViewById(R.id.mTablayout);
        tabLayout.addOnTabSelectedListener(this);
        mToolbarTitle=(TextView)findViewById(R.id.mToolbarTitle) ;
        mBackArrow=(ImageView)findViewById(R.id.mBackArrow);
        mToolbarLogo=(ImageView)findViewById(R.id.mToolBarlogo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // THIS IS FOR SHOWINH ICON OF NAVIGATION
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.toolbar_clr));

        navHeader = navigationView.getHeaderView(0);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        mUserName = (TextView) navHeader.findViewById(R.id.userNameTv);
        mUserMobile = (TextView) navHeader.findViewById(R.id.userPhoneTv);
        mUserName.setText(SharedPreference.getUserName());
        mUserMobile.setText(SharedPreference.getUserMobileNumber());

        toHome(new HomeFragment());
        setupTabLayout();

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.super.onBackPressed();
            }
        });


    }

    private void setupTabLayout() {
        tabLayout.getTabAt(0).setCustomView(R.layout.custom_tab);
        tabLayout.getTabAt(1).setCustomView(R.layout.custom_tab);
        tabLayout.getTabAt(2).setCustomView(R.layout.custom_tab);
        tabLayout.getTabAt(3).setCustomView(R.layout.custom_tab);


        TextView tabHome = (TextView) tabLayout.getTabAt(0).getCustomView();
        TextView tabQrCode = (TextView) tabLayout.getTabAt(1).getCustomView();
        TextView tabTransactions = (TextView) tabLayout.getTabAt(2).getCustomView();
        TextView tabProfile = (TextView) tabLayout.getTabAt(3).getCustomView();


        tabHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_coloured, 0, 0);
        tabHome.setText(getString(R.string.home));

        tabQrCode.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_qr_code_grey, 0, 0);
        tabQrCode.setText(getString(R.string.qrcode));

        tabTransactions.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_history_grey, 0, 0);
        tabTransactions.setText(getString(R.string.reports));

        tabProfile.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_grey, 0, 0);
        tabProfile.setText(getString(R.string.profile));

    }

    @Override
    public void onTabSelected(TabLayout.Tab tabs) {

        TextView tab = (TextView) tabs.getCustomView();
        Log.e("onTabSelected", tab.getText().toString());

        if (tab.getText().equals(getString(R.string.reports))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_history_blue, 0, 0);
            tab.setTextColor(Color.parseColor("#002037"));
            toTransactionsFrg(new TransactionsFrg());
        } else if (tab.getText().equals(getString(R.string.qrcode))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_qr_code_colored, 0, 0);
            tab.setTextColor(Color.parseColor("#002037"));
            Toast.makeText(this, "Coming soo...", Toast.LENGTH_SHORT).show();
            //toQrCodeFrg(QrCodeFrg.getInstance());
        } else if (tab.getText().equals(getString(R.string.profile))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#002037"));
            toMyAccountFrg(MyAccountFrg.getInstance());
        } else {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#002037"));
            toHome(HomeFragment.getInstance());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tabs) {

        TextView tab = (TextView) tabs.getCustomView();
        Log.e("onTabUnselected", tab.getText().toString());

        if (tab.getText().equals(getString(R.string.reports))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_history_grey, 0, 0);
            tab.setTextColor(Color.parseColor("#707070"));

        } else if (tab.getText().equals(getString(R.string.qrcode))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_qr_code_grey, 0, 0);
            tab.setTextColor(Color.parseColor("#707070"));

        } else if (tab.getText().equals(getString(R.string.profile))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_grey, 0, 0);
            tab.setTextColor(Color.parseColor("#707070"));

        } else {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_grey, 0, 0);
            tab.setTextColor(Color.parseColor("#707070"));
        }


    }

    @Override
    public void onTabReselected(TabLayout.Tab tabs) {

        TextView tab = (TextView) tabs.getCustomView();

        if (tab.getText().equals(getString(R.string.reports))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_history_blue, 0, 0);
            tab.setTextColor(Color.parseColor("#002037"));
            toTransactionsFrg(new TransactionsFrg());
        } else if (tab.getText().equals(getString(R.string.qrcode))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_qr_code_colored, 0, 0);
            tab.setTextColor(Color.parseColor("#002037"));
            //toQrCodeFrg(QrCodeFrg.getInstance());
            Toast.makeText(this, "Coming soo...", Toast.LENGTH_SHORT).show();
        } else if (tab.getText().equals(getString(R.string.profile))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#002037"));
            toMyAccountFrg(MyAccountFrg.getInstance());
        } else {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#002037"));
            toHome(HomeFragment.getInstance());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        LinearLayout item1 = (LinearLayout) menu.findItem(R.id.wallet_menu).getActionView().findViewById(R.id.rootLayout);
        TextView mWalletBalance=(TextView)menu.findItem(R.id.wallet_menu).getActionView().findViewById(R.id.walletBalanceTv);
        mWalletBalance.setText("₹ "+SharedPreference.getBalance());
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              toAddMoney(new AddMoneyFrg());
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.wallet_menu) {
           toAddMoney(new AddMoneyFrg());
        }

        else if (id == R.id.notification_menu) {
            Toast.makeText(this, "Coming soon..", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            toHome(new HomeFragment());
        } else if (id == R.id.nav_myQr) {
            Toast.makeText(HomeActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_history) {
            toTransactionsFrg(new TransactionsFrg());
        } else if (id == R.id.nav_addMoney) {
            //startActivity(new Intent(HomeActivity.this,DemoActivity.class));
            toAddMoney(new AddMoneyFrg());
        } else if (id == R.id.nav_appLock) {
            Toast.makeText(HomeActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_faq) {
            Toast.makeText(HomeActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_rateUs) {
            Toast.makeText(HomeActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_help) {
            toSupportFrg(SupportFrg.getInstance());
        }
        drawer.closeDrawer(Gravity.LEFT);
        return true;
    }

    private void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out this amazing app at here");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setStatusBarColoyr(int colour) {
        if(Build.VERSION.SDK_INT >=21 ){
            Window window = HomeActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this, colour));
        }

    }


    @Override
    public void setToolBarTitle(String titleName) {
        mToolbarTitle.setText(titleName);
    }

    @Override
    public void toggleBackArrowVisiblity(int visiblity) {
        mBackArrow.setVisibility(visiblity);
    }

    @Override
    public void toggleLogoVisiblity(int visibility) {
        mToolbarLogo.setVisibility(visibility);
    }

    @Override
    public void toggleNavigationMenuVisibility(boolean visibility) {
       toggle.setDrawerIndicatorEnabled(visibility);
    }
}
