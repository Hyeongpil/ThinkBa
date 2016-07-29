package com.hyeongpil.thinkba.navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.BaseActivity;


/**
 * Created by hp on 2016-06-17.
 */
public class SettingActivity extends BaseActivity{
    View containView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container.setLayoutResource(R.layout.activity_setting);
        containView = container.inflate();
        actionBarTitleSet("설정", Color.BLACK);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingFragment()).commit();
    }

}
