package com.application.zpay.activities;

import android.os.Bundle;

import com.application.zpay.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoActivity extends zPayActivity {

    List<SlideModel> slideModelList;

    @BindView(R.id.image_slider)
    ImageSlider mImageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);

        slideModelList=new ArrayList<>();
        slideModelList.add(new SlideModel(R.drawable.slider_image1, "", ScaleTypes.CENTER_CROP));
        slideModelList.add(new SlideModel(R.drawable.slider_image2, "", ScaleTypes.CENTER_CROP));
        slideModelList.add(new SlideModel("https://bit.ly/3fLJf72", "Baby Owl", ScaleTypes.CENTER_CROP));
        mImageSlider.setImageList(slideModelList,ScaleTypes.CENTER_CROP);

        mImageSlider.startSliding(3000);
        mImageSlider.stopSliding();


    }


}