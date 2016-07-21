package com.hyeongpil.thinkba.util.kakao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.login.LoginActivity;
import com.hyeongpil.thinkba.main.MainActivity;
import com.hyeongpil.thinkba.util.BasicValue;
import com.hyeongpil.thinkba.util.GlobalApplication;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

/**
 * Created by hp on 2016-06-09.
 */
public class KakaoSignupActivity extends Activity{
    final static String TAG = "KakaoSignupActivity";
    private GoogleApiClient mGoogleApiClient = GlobalApplication.getInstance().getmGoogleApiClient();
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }
    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Log.e(TAG,"getUserProfile :"+userProfile);
                BasicValue.getInstance().setProfile_img(userProfile.getProfileImagePath());
                BasicValue.getInstance().setProfile_name(userProfile.getNickname());
                redirectMainActivity(); // 로그인 성공시 MainActivity로
            }
        });
    }

    private void redirectMainActivity() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int loginCount = pref.getInt("loginCount",0)+1;
        editor.putBoolean("autoLogin", true);
        editor.putInt("loginCount",loginCount);
        editor.commit();

        Log.d(TAG,"loginCount :"+loginCount);
        try{if(mGoogleApiClient.isConnected()) {
            if (loginCount == 1) {
                Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_thinkba_people), 1);
            } else if (loginCount == 10) {
                Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_thinkba_people), 2);
            } else if (loginCount == 30) {
                Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_thinkba_people), 3);
            }
        }else {
            Toast.makeText(KakaoSignupActivity.this, "구글 게임 연동이 실패하였습니다 다시 로그인 해 주세요", Toast.LENGTH_SHORT).show();
        }}catch (NullPointerException e){}
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}
