package com.hyeongpil.thinkba.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016-10-11.
 */
public class SharedPreparence {
    final static String TAG = "SharedPreparence";
    private final String mPreferenceId = "com.sonwonho.pref"; // 대충 아무거나 적어도됨

    static Context mContext;

    public SharedPreparence(Context c) {
        mContext = c;
    }

    /**
     * string 저장
     */
    public void putString(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public String getString(String key) {
        try {
            return getString(key, "");
        } catch (Exception e) {}

        return "";
    }
    public String getString(String key, String val) {
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        try {
            return pref.getString(key, val);
        } catch (Exception e) {
            return val;
        }
    }

    /**
     * Arraylist를 받아 Gson으로 변환 후 저장
     */
    public void putArrayList(ArrayList<Integer> value) {
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ArrayList", toJson((value)));
        editor.commit();
    }

    public ArrayList<Integer> getArrayList(){
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        ArrayList<Integer> arrayList = new ArrayList<>();
        try{
            String json = pref.getString("ArrayList","");
            arrayList = ((ArrayList<Integer>)fromJson(json, new TypeToken<ArrayList<Integer>>() {}.getType()));
            return arrayList;
        }catch (Exception e){
            Log.e(TAG,"gson 변환 오류"+e.getMessage());
        }
        return arrayList;
    }

    public static String toJson(Object jsonObject) {
        return new Gson().toJson(jsonObject);
    }
    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }
}
