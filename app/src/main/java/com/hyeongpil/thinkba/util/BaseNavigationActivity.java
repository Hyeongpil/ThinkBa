package com.hyeongpil.thinkba.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.login.LoginActivity;
import com.hyeongpil.thinkba.navigation.MyPageActivity;
import com.hyeongpil.thinkba.navigation.SettingActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.tsengvn.typekit.TypekitContextWrapper;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by hp on 2016. 7. 18.
 */
public class BaseNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final static String TAG = "BaseNavigationActivity";
    ImageView profile_img;
    TextView profile_name;
    private GoogleApiClient mGoogleApiClient = GlobalApplication.getInstance().getmGoogleApiClient();
    DrawerLayout drawer;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case (R.id.nav_myinfo):
                startActivity(new Intent(BaseNavigationActivity.this, MyPageActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                drawer.setBackgroundColor(Color.WHITE);
                break;

            case (R.id.nav_archive):
//                startActivity(new Intent(BaseNavigationActivity.this, ArchiveActivity.class));
                if(mGoogleApiClient.isConnected()){
                    startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), 124);
                }else {Toast.makeText(BaseNavigationActivity.this, "구글 게임 연동이 실패하였습니다 다시 로그인 해 주세요", Toast.LENGTH_SHORT).show();}
                drawer.setBackgroundColor(Color.WHITE);
                break;

            case (R.id.nav_setting):
                Intent setting_intent = new Intent(this,SettingActivity.class);
                startActivity(setting_intent);
                drawer.setBackgroundColor(Color.WHITE);
                break;

            case (R.id.nav_logout):
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Toast.makeText(BaseNavigationActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                drawer.setBackgroundColor(Color.WHITE);

                Intent out_intent = new Intent(BaseNavigationActivity.this, LoginActivity.class);
                out_intent.putExtra("logout","logout");
                startActivity(out_intent);
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawers();
        return true;
    }

    protected void naviDrawerInit() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.archive_toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        profile_img = (ImageView)nav_header.findViewById(R.id.header_profile_image);
        profile_name = (TextView)nav_header.findViewById(R.id.header_profile_text);

        //네비게이션 프로필
        Glide.with(BaseNavigationActivity.this)
                .load(BasicValue.getInstance().getProfile_img())
                .skipMemoryCache(true)
                .error(R.drawable.default_profile)
                .bitmapTransform(new CropCircleTransformation(Glide.get(BaseNavigationActivity.this).getBitmapPool())).into(profile_img);
        profile_name.setText(BasicValue.getInstance().getProfile_name());
    }

    /**
     * 글씨체 일괄 적용
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

}
