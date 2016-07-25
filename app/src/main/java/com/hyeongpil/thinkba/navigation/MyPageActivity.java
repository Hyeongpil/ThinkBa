package com.hyeongpil.thinkba.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.BaseNavigationActivity;
import com.hyeongpil.thinkba.util.BasicValue;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by hp on 2016. 7. 18.
 */
public class MyPageActivity extends BaseNavigationActivity {

    @Bind(R.id.mypage_profile_img)
    ImageView profile_img;
    @Bind(R.id.mypage_profile_name)
    TextView profile_name;
    @Bind(R.id.mypage_distance_text)
    TextView distance_text;
    @Bind(R.id.mypage_distance_arrow)
    ImageView distance_arrow;
    @Bind(R.id.mypage_device_text)
    TextView device_text;
    @Bind(R.id.mypage_device_arrow)
    ImageView device_arrow;
    @Bind(R.id.mypage_bike_arrow)
    ImageView bike_arrow;
    @Bind(R.id.mypage_archive_1)
    ImageView archive_1;
    @Bind(R.id.mypage_archive_2)
    ImageView archive_2;
    @Bind(R.id.mypage_archive_3)
    ImageView archive_3;
    @Bind(R.id.mypage_archive_4)
    ImageView archive_4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        setTitle("프로필");
        ButterKnife.bind(this);

        naviDrawerInit();
        setProfile();
    }

    private void setProfile(){
        Glide.with(MyPageActivity.this)
                .load(BasicValue.getInstance().getProfile_img())
                .skipMemoryCache(true)
                .error(R.drawable.default_profile)
                .bitmapTransform(new CropCircleTransformation(Glide.get(MyPageActivity.this).getBitmapPool())).into(profile_img);
        profile_name.setText(BasicValue.getInstance().getProfile_name());
    }
}
