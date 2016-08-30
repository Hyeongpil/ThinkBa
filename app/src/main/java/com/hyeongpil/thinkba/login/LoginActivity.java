package com.hyeongpil.thinkba.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.login.kakao.KakaoSignupActivity;
import com.hyeongpil.thinkba.util.BasicValue;
import com.hyeongpil.thinkba.util.GlobalApplication;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import javax.microedition.khronos.opengles.GL;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016-04-17.
 */
public class LoginActivity extends BaseGameActivity {
    final static String TAG = "LoginActivity";

    private SessionCallback callback;      //콜백 선언
    @Bind(R.id.login_layout) FrameLayout login_layout;
    @Bind(R.id.login_achieve) FrameLayout achieve_popup;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //카카오 콜백 선언
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        sp = GlobalApplication.getInstance().getSp();

        getSetting();
        getIntentData();
        autologin();
    }

    private void getIntentData(){
        try {
            //로그아웃 시 업적 진입
            if (getIntent().getStringExtra("logout").equals("logout")) { // BaseNavigationActivity 에서 받음
                setAchievement();
            }
        }catch (NullPointerException e){}
    }
    /**
     * getSetting
     * 설정 페이지의 설정 값을 세팅
     */
    private void getSetting(){
//        BasicValue.getInstance().setAutoLogin(sp.getBoolean("autoLogin",false));
        BasicValue.getInstance().setAccident(sp.getBoolean("accident",true));
        BasicValue.getInstance().setAccident_alarm(sp.getBoolean("accident_alarm",true));
        BasicValue.getInstance().setRobber(sp.getBoolean("robber",true));
    }

    private void autologin(){
        // TODO: 2016. 7. 25. 자동 로그인 구현 해야함
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

    /**
     * 로그아웃 시 업적 진입
     */
    private void setAchievement(){
        Games.setViewForPopups(GlobalApplication.getInstance().getmGoogleApiClient(),achieve_popup);
        Games.Achievements.unlock(GlobalApplication.getInstance().getmGoogleApiClient(),getString(R.string.achievement_hate_but_retry));
    }
}
