package com.hyeongpil.thinkba.navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.BaseActivity;
import com.hyeongpil.thinkba.util.BasicValue;
import com.hyeongpil.thinkba.util.CalculateUtil;
import com.hyeongpil.thinkba.util.SharedManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by hp on 2016. 7. 18.
 */
public class MyPageActivity extends BaseActivity {
    final static String TAG = "MyPageActivity";
    SharedManager sharedManager = new SharedManager();
    CalculateUtil calculateUtil = new CalculateUtil();

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
    View containView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container.setLayoutResource(R.layout.activity_mypage);
        containView = container.inflate();
        actionBarTitleSet("프로필", Color.BLACK);
        ButterKnife.bind(this);

        setProfile();
    }

    private void setProfile(){
        Glide.with(MyPageActivity.this)
                .load(BasicValue.getInstance().getProfile_img())
                .skipMemoryCache(true)
                .error(R.drawable.default_profile)
                .bitmapTransform(new CropCircleTransformation(Glide.get(MyPageActivity.this).getBitmapPool())).into(profile_img);
        profile_name.setText(BasicValue.getInstance().getProfile_name());
        distance_text.setText(calculateUtil.convertDistanceStr(Double.parseDouble(sharedManager.getDistance())));
    }

}
