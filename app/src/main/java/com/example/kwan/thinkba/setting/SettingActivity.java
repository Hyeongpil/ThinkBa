package com.example.kwan.thinkba.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kwan.thinkba.R;


/**
 * Created by hp on 2016-06-17.
 */
public class SettingActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("설정");
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingFragment()).commit();
    }

}
