package com.example.kwan.thinkba.Activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.kwan.thinkba.GpsInfo;
import com.example.kwan.thinkba.ListViewDialog;
import com.example.kwan.thinkba.R;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TMapGpsManager.onLocationChangedCallback {
    final static String TAG = "MainActivity";
    Context mContext;
    TMapGpsManager gps = null;
    GpsInfo gpsInfo;
    TMapPoint startPoint;
    TMapPoint arrivePoint;

    FrameLayout mapLayout;
    TMapView mapView;
    Button nowLocationBtn;
    Button mapMenuBtn;
    Button trackingBtn;

    double latitude; //위도
    double longitude; // 경도
    String arrive; // 도착지 좌표

    private boolean m_bTrackingMode = false;
    private ListViewDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mapLayout = (FrameLayout)findViewById(R.id.mapLayout);
        nowLocationBtn = (Button)findViewById(R.id.nowLocation);
        nowLocationBtn.setOnClickListener(nowClickListener);
        mapMenuBtn = (Button)findViewById(R.id.mapMenu);
        mapMenuBtn.setOnClickListener(menuClickListener);
        trackingBtn = (Button)findViewById(R.id.tracking);
        trackingBtn.setOnClickListener(trackingClickListener);

        getArriveInfo();
        naviDrawerInit();
        tMapGps();
        tMapInit();

    }

    /**
     * getArriveInfo
     * 길 찾기 에서 도착지를 받아온다
     */
    private void getArriveInfo(){
        try {
            Intent intent = getIntent();
            arrive = intent.getStringExtra("arrivePoint");
            String temp[] = arrive.split(" ");
            Log.e(TAG, "arrive :"+arrive);

            double latitude = Double.parseDouble(temp[1]);
            double longitude = Double.parseDouble(temp[3]);
            arrivePoint = new TMapPoint(latitude,longitude);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void tMapInit(){
        gpsInfo = new GpsInfo(MainActivity.this);
        if(gpsInfo.isGetLocation()) { // 현재 위치 받아오기
            latitude = gpsInfo.getLatitude();
            longitude = gpsInfo.getLongitude();
            startPoint = new TMapPoint(latitude,longitude);
        }
        mapView = new TMapView(this); // 지도 위도, 경도, 줌레벨
        mapView.setSKPMapApiKey("d5c4630e-a1ac-3ddc-8417-03e1bf83e1b4");
        mapView.setTrackingMode(m_bTrackingMode); // 트래킹 모드 사용
        mapView.setZoomLevel(16);
//        mapView.setBicycleInfo(true);//자전거 도로 표시
        mapView.setBicycleFacilityInfo(true);//자전거 시설물 표시
        mapView.setIconVisibility(true); // 현재 위치 표시하는지 여부
        mapView.setLocationPoint(longitude,latitude); // 지도 현재 좌표 설정
        mapView.setCenterPoint(longitude,latitude); // 지도 현재 위치로
        Log.e(TAG,"arrivePoint :"+arrivePoint);
        if(arrivePoint != null){setPath();}
        mapLayout.addView(mapView);
    }
    /**
     * setPath
     * 도착지 정보가 있다면 맵에 경로를 그려준다
     */
    private void setPath() {
        Log.e(TAG,"setPath 진입");
        TMapData tmapdata = new TMapData();
        //출발지와 도착지를 받아 자전거 경로를 그려줌
        tmapdata.findPathDataWithType(TMapData.TMapPathType.BICYCLE_PATH, startPoint, arrivePoint, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                mapView.addTMapPath(tMapPolyLine);
            }
        });
    }

    private void tMapGps(){
        gps = new TMapGpsManager(MainActivity.this);
        gps.setMinTime(1000);
        gps.setMinDistance(5);
        gps.setProvider(gps.NETWORK_PROVIDER);
        gps.OpenGps();
    }
    /**
     * onLocationChange
     * 사용자가 이동할 때 이를 감지하여 위치값을 받는다. 트래킹모드가 활성화 되어 있다면 화면의 위치를 계속 바꿔준다.
     */
    @Override
    public void onLocationChange(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.e(TAG,"onLocationChange " + location.getLatitude() +  " " + location.getLongitude() + " " + location.getSpeed() + " " + location.getAccuracy());
        if(m_bTrackingMode) {
            mapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }
    /**
     * setTrackingMode
     * 화면중심을 단말의 현재위치로 이동시켜주는 트래킹모드로 설정한다.
     */
    public void setTrackingMode() {
        m_bTrackingMode = !m_bTrackingMode;
        if(m_bTrackingMode){
            Toast.makeText(MainActivity.this, "트래킹 켜짐", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "트래킹 꺼짐", Toast.LENGTH_SHORT).show();
        }
        mapView.setTrackingMode(m_bTrackingMode);
    }

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

        if (id == R.id.nav_myinfo) {
        } else if (id == R.id.nav_archive) {
        } else if (id == R.id.nav_setting) {
        } else if (id == R.id.nav_logout) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawers();
        return true;
    }

    private void naviDrawerInit(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    /**
     * nowClickListener
     * 화면중심을 단말의 현재위치로 이동시켜준다.
     */
    Button.OnClickListener nowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gpsInfo = new GpsInfo(MainActivity.this);
            if(gpsInfo.isGetLocation()) {
                latitude = gpsInfo.getLatitude();
                longitude = gpsInfo.getLongitude();
                mapView.setLocationPoint(longitude,latitude);
                mapView.setCenterPoint(longitude,latitude);
//                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.map_pin_red);
//                mapView.setIcon(bitmap);
            }else{
                Toast.makeText(MainActivity.this, "GPS 연동 실패", Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG,"longitude :"+longitude);
            Log.e(TAG,"latitude :"+latitude);
        }
    };

    Button.OnClickListener menuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] item = getResources().getStringArray(R.array.list_dialog_list_item);
            List<String> listItem = Arrays.asList(item);
            ArrayList<String> itemArrayList = new ArrayList<String>(listItem);
            mDialog = new ListViewDialog(mContext, getString(R.string.list_dialog_title), itemArrayList);
            mDialog.onOnSetItemClickListener(new ListViewDialog.ListViewDialogSelectListener(){
                @Override
                public void onSetOnItemClickListener(int position) {
                    if (position == 0){
                        Intent intent = new Intent(MainActivity.this,FindRoadActivity.class);
                        startActivity(intent);
                    }
                    else if (position == 1){
                        Intent intent = new Intent(MainActivity.this,NearbyActivity.class);
                        startActivity(intent);
                    }
                    else if(position == 2){
                        Log.v(TAG, " 세번째 인덱스가 선택되었습니다");
                    }
                    mDialog.dismiss();
                }
            });
            mDialog.show();
        }
    };
    Button.OnClickListener trackingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setTrackingMode();
        }
    };

}
