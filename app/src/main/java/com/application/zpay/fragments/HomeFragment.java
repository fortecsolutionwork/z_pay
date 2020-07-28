package com.application.zpay.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.application.zpay.R;
import com.application.zpay.Utilities.SharedPreference;
import com.application.zpay.Utilities.Utils;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Gaganjot Singh on 26/06/2020.
 */
public class HomeFragment extends zPayFragment {

    List<SlideModel> slideModelList;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.firstDotImageView)
    ImageView mFirstDotImageView;

    @BindView(R.id.secondDotImageView)
    ImageView mSecondDotImageView;

    @BindView(R.id.thirdDotImageView)
    ImageView mThirdDotImageView;

    @BindView(R.id.fourthDotImageView)
    ImageView mFourthDotImageView;

    @BindView(R.id.viewPagerIndicatorIconsLayout)
    LinearLayout mViewPagerIndicatorIconsLayout;

    @BindView(R.id.prepaidLl)
    LinearLayout mPrepaidLl;

    @BindView(R.id.postpaidLl)
    LinearLayout mPostpaidLl;

    @BindView(R.id.electricityLl)
    LinearLayout mElectricityLl;

    @BindView(R.id.dthLl)
    LinearLayout mDthLl;

    @BindView(R.id.landlineLl)
    LinearLayout mLandlineLl;

    @BindView(R.id.gasLl)
    LinearLayout mGasLl;

    @BindView(R.id.waterLl)
    LinearLayout mWaterLl;

    @BindView(R.id.broadbandLl)
    LinearLayout mBroadbandLl;

    ViewPagerAdapter pagerAdapter;

    @BindView(R.id.image_slider)
    ImageSlider mImageSlider;

    Unbinder unbinder;

    private static HomeFragment mInstance;
    public static HomeFragment getInstance() {
        if (mInstance == null) {
            mInstance = new HomeFragment();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefrg_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        slideModelList=new ArrayList<>();
        slideModelList.add(new SlideModel(R.drawable.slider_image1, "", ScaleTypes.CENTER_CROP));
        slideModelList.add(new SlideModel(R.drawable.slider_image2, "", ScaleTypes.CENTER_CROP));
        slideModelList.add(new SlideModel("https://bit.ly/3fLJf72", "Baby Owl", ScaleTypes.CENTER_CROP));
         mImageSlider.setImageList(slideModelList,ScaleTypes.CENTER_CROP);

        mImageSlider.startSliding(3000);
        mImageSlider.stopSliding();

        Utils.hideKeyboard(getActivity());
        pagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        addPagerFragments();
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mFirstDotImageView.setImageResource(R.drawable.ic_dots_blue);
                        mSecondDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mThirdDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mFourthDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        break;

                    case 1:
                        mFirstDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mSecondDotImageView.setImageResource(R.drawable.ic_dots_blue);
                        mThirdDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mFourthDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        break;

                    case 2:
                        mFirstDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mSecondDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mThirdDotImageView.setImageResource(R.drawable.ic_dots_blue);
                        mFourthDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        break;


                    case 3:
                        mFirstDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mSecondDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mThirdDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mFourthDotImageView.setImageResource(R.drawable.ic_dots_blue);
                        break;

                    default:
                        mFirstDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mSecondDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mThirdDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        mFourthDotImageView.setImageResource(R.drawable.ic_dots_grey);
                        break;


                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void addPagerFragments() {
        pagerAdapter.addFragment(Intro1.getInstance());
        pagerAdapter.addFragment(Intro2.getInstance());
        pagerAdapter.addFragment(Intro3.getInstance());
        pagerAdapter.addFragment(Intro4.getInstance());
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
        homeInteractiveListener.toggleLogoVisiblity(View.VISIBLE);
        homeInteractiveListener.toggleBackArrowVisiblity(View.GONE);
        homeInteractiveListener.setToolBarTitle("");
        homeInteractiveListener.toggleNavigationMenuVisibility(true);
    }

    @OnClick({R.id.prepaidLl, R.id.postpaidLl, R.id.electricityLl, R.id.dthLl, R.id.landlineLl, R.id.gasLl, R.id.waterLl, R.id.broadbandLl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prepaidLl:
                SharedPreference.setRechargeAmount("");
                homeInteractiveListener.toPrepaidFrg(new PrepaidFrg());
                break;
            case R.id.postpaidLl:
                homeInteractiveListener.toPostpaidFrg(new PostpaidFrg());
                break;
            case R.id.electricityLl:
                Toast.makeText(getActivity(), "Coming soon...", Toast.LENGTH_SHORT).show();
                //homeInteractiveListener.toElectricityFrg(new ElectricityFrg());
                break;
            case R.id.dthLl:
                Toast.makeText(getActivity(), "Coming soon...", Toast.LENGTH_SHORT).show();
                //homeInteractiveListener.toDthFrg(new DTHFrg());
                break;
            case R.id.landlineLl:
                Toast.makeText(getActivity(), "Coming soon...", Toast.LENGTH_SHORT).show();
                //homeInteractiveListener.toLandlineFrg(new LandlineFrg());
                break;
            case R.id.gasLl:
                Toast.makeText(getActivity(), "Coming soon...", Toast.LENGTH_SHORT).show();
                //homeInteractiveListener.toGasFrg(new GasFrg());
                break;
            case R.id.waterLl:
                Toast.makeText(getActivity(), "Coming soon...", Toast.LENGTH_SHORT).show();
                //homeInteractiveListener.toWaterBillFrg(new WaterBill());
                break;
            case R.id.broadbandLl:
                Toast.makeText(getActivity(), "Coming soon...", Toast.LENGTH_SHORT).show();
                //homeInteractiveListener.toBroadbandFrg(new BroadbandFrg());
                break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            return mList.get(i);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        public void addFragment(Fragment fragment) {
            mList.add(fragment);
        }

    }


}
