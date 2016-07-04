package com.example.kwan.thinkba.util;

import android.app.Activity;
import android.app.Application;

import com.example.kwan.thinkba.util.kakao.KakaoSDKAdapter;
import com.kakao.auth.KakaoSDK;

/**
 * Created by hp on 2016-06-09.
 */
public class GlobalApplication extends Application {
    private static volatile Activity currentActivity = null;
    private static volatile GlobalApplication instance = null;

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */

    public static GlobalApplication getInstance() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        KakaoSDK.init(new KakaoSDKAdapter());
    }
    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
