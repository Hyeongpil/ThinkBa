package com.example.kwan.thinkba.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.example.kwan.thinkba.util.BasicValue;
import com.example.kwan.thinkba.R;

/**
 * Created by hp on 2016-06-17.
 */
public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    final static String TAG = "SettingFragment";
    final String AUTO_LOGIN = "auto_login";
    final String ACCIDENT = "accident";
    final String ACCIDENT_ALARM = "accident_alarm";
    final String ROBBER = "robber";
    SharedPreferences sp;

    // TODO: 2016-06-17 설정 기능 추가하기
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        //SharedPreferences 기본값 설정
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().commit();
        PreferenceManager.setDefaultValues(getActivity(),R.xml.setting,false);

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            }
        };
    }

    /**
     * onSharedPreferenceChanged
     * sharedPreferences에서 설정 값을 받아 BasicValue와 pref에 넣는다.
     * @param sharedPreferences
     * @param key
     */

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //설정값을 담을 pref 선언
        SharedPreferences pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

//        if(key.equals(AUTO_LOGIN)){
//            editor.putBoolean("autoLogin",sharedPreferences.getBoolean(AUTO_LOGIN,false));
//            BasicValue.getInstance().setAutoLogin(sharedPreferences.getBoolean(AUTO_LOGIN,false));
//            editor.commit();
//            Log.d(TAG,"basicvalue 값 :"+BasicValue.getInstance().isAutoLogin());
//        }
        if(key.equals(ACCIDENT)){
            editor.putBoolean("accident",sharedPreferences.getBoolean(ACCIDENT,false));
            BasicValue.getInstance().setAccident(sharedPreferences.getBoolean(ACCIDENT,false));
            editor.commit();
            Log.d(TAG,"basicvalue 값 :"+BasicValue.getInstance().isAccident());
        }
        if(key.equals(ACCIDENT_ALARM)){
            editor.putBoolean("accident_alarm",sharedPreferences.getBoolean(ACCIDENT_ALARM,false));
            BasicValue.getInstance().setAccident_alarm(sharedPreferences.getBoolean(ACCIDENT_ALARM,false));
            editor.commit();
            Log.d(TAG,"basicvalue 값 :"+BasicValue.getInstance().isAccident_alarm());
        }
        if(key.equals(ROBBER)){
            editor.putBoolean("robber",sharedPreferences.getBoolean(ROBBER,false));
            BasicValue.getInstance().setRobber(sharedPreferences.getBoolean(ROBBER,false));
            editor.commit();
            Log.d(TAG,"basicvalue 값 :"+BasicValue.getInstance().isRobber());
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}
