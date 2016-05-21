package com.example.kwan.thinkba.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kwan.thinkba.R;

public class IntroActivity extends AppCompatActivity {

    Handler myHandler = new Handler();
    Runnable myRunnable = new Runnable() {

        //핸들러를 이용하여 로그인 액티비티로 넘겨줌
        @Override
        public void run() {
            Intent i = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO 인트로 늘리기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        myHandler.postDelayed(myRunnable, 500);
    }
}
