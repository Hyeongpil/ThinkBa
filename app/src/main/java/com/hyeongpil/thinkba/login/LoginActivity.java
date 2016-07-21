package com.hyeongpil.thinkba.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.example.games.basegameutils.BaseGameActivity;
import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.BasicValue;
import com.hyeongpil.thinkba.util.GlobalApplication;
import com.hyeongpil.thinkba.util.kakao.KakaoSignupActivity;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016-04-17.
 */
public class LoginActivity extends BaseGameActivity {
    final static String TAG = "LoginActivity";
    SharedPreferences sp;

    private SessionCallback callback;      //콜백 선언
    @Bind(R.id.login_layout) FrameLayout login_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //카카오 콜백 선언
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        getSetting();
        autologin();
    }

    /**
     * getSetting
     * 설정 페이지의 설정 값을 세팅
     */
    private void getSetting(){
        sp = getSharedPreferences("pref",MODE_PRIVATE);
//        BasicValue.getInstance().setAutoLogin(sp.getBoolean("autoLogin",false));
        BasicValue.getInstance().setAccident(sp.getBoolean("accident",false));
        BasicValue.getInstance().setAccident_alarm(sp.getBoolean("accident_alarm",false));
        BasicValue.getInstance().setRobber(sp.getBoolean("robber",false));
    }

    private void autologin(){
        if(sp.getBoolean("autoLogin",false)){
        }
    }

    /**
     * SessionCallback
     * 카카오 세션 콜백 선언
     */
    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }
    protected void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSignInFailed() {
        Log.e(TAG,"구글 로그인 실패");
    }

    @Override
    public void onSignInSucceeded() {
        Log.e(TAG,"구글 로그인 성공");
        GlobalApplication.getInstance().connGoogleApiClient();
    }
}
