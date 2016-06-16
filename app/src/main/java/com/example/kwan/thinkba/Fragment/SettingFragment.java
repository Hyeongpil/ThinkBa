package com.example.kwan.thinkba.Fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;

import com.example.kwan.thinkba.R;

/**
 * Created by hp on 2016-06-17.
 */
public class SettingFragment extends PreferenceFragment {

    // TODO: 2016-06-17 설정 기능 추가하기
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
    }
}
