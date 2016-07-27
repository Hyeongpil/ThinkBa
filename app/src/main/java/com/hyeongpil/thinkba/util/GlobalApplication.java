package com.hyeongpil.thinkba.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.hyeongpil.thinkba.login.kakao.KakaoSDKAdapter;
import com.kakao.auth.KakaoSDK;
import com.tsengvn.typekit.Typekit;

/**
 * Created by hp on 2016-06-09.
 */
public class GlobalApplication extends Application implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    final static String TAG = "GlobalApplication";

    private static volatile Activity currentActivity = null;
    private static volatile GlobalApplication instance = null;
    public static GoogleApiClient mGoogleApiClient;

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

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumGothic.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NanumBarunGothicBold.ttf"));


        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * 구글 API 연결
     */
    public void connGoogleApiClient(){
        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                // add other APIs and scopes here as needed
                .build();
        mGoogleApiClient.connect();
        // TODO: 2016. 7. 21. 업적 달성 시 알림 띄워줘야함
    }
    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
        Log.e(TAG,"연결 해제 진입");
        mGoogleApiClient.disconnect(); // 구글 게임 연결 해제
    }



    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG,"구글 연결");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG,"구글 연결 재시도");
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG,"구글 연결 실패");
    }


}
