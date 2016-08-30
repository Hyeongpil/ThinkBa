package com.hyeongpil.thinkba.util.retrofit;

import android.content.SharedPreferences;

import com.hyeongpil.thinkba.util.GlobalApplication;

/**
 * Created by hp on 2016-08-30.
 */
public class SharedManager {
    final static String TAG = "SharedManager";
    SharedPreferences sp = GlobalApplication.getInstance().getSp();
    SharedPreferences.Editor editor =  sp.edit();

    public void putString(String key, String value){
        editor.putString(key,value);
        editor.commit();
    }
    public void putDistance(double value){
        double distance = Double.parseDouble(sp.getString("distance","0"))+value;
        putString("distance",String.valueOf(distance));
    }

    public String getDistance(){
        return sp.getString("distance","0");
    }
}
