package com.example.kwan.thinkba.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.kwan.thinkba.BasicValue;
import com.example.kwan.thinkba.Kakao.KakaoSignupActivity;
import com.example.kwan.thinkba.R;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kwan on 2016-04-17.
 */
public class LoginActivity extends Activity {
    final static String TAG = "LoginActivity";

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
    }

    /**
     * getSetting
     * 설정 페이지의 설정 값을 세팅
     */
    private void getSetting(){
        SharedPreferences sp = getSharedPreferences("pref",MODE_PRIVATE);
        BasicValue.getInstance().setAutoLogin(sp.getBoolean("autoLogin",false));
        BasicValue.getInstance().setAccident(sp.getBoolean("accident",false));
        BasicValue.getInstance().setAccident_alarm(sp.getBoolean("accident_alarm",false));
        BasicValue.getInstance().setRobber(sp.getBoolean("robber",false));
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
}
