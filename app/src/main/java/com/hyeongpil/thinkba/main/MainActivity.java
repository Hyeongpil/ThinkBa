package com.hyeongpil.thinkba.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.main.findroad.FindRoadActivity;
import com.hyeongpil.thinkba.main.nearby.NearbyActivity;
import com.hyeongpil.thinkba.navigation.SettingActivity;
import com.hyeongpil.thinkba.util.BaseNavigationActivity;
import com.hyeongpil.thinkba.util.BasicValue;
import com.hyeongpil.thinkba.util.GlobalApplication;
import com.hyeongpil.thinkba.util.GpsInfo;
import com.hyeongpil.thinkba.util.ListViewDialog;
import com.hyeongpil.thinkba.util.model.TmapPointArr;
import com.hyeongpil.thinkba.util.retrofit.AccidentThread;
import com.hyeongpil.thinkba.util.retrofit.WeatherThread;
import com.skp.Tmap.TMapCircle;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseNavigationActivity implements TMapGpsManager.onLocationChangedCallback {
    final static String TAG = "MainActivity";
    Context mContext;
    TMapGpsManager gps = null;
    GpsInfo gpsInfo;
    TMapPoint startPoint;
    static TMapPoint arrivePoint;
    static TMapPoint passPoint; // 경유지
    ArrayList<TMapPoint> arr_tMapPoint;
    ArrayList<TMapCircle> arr_tMapCircle;
    TmapPointArr tmapPointArr;

    TMapView mapView;
    FrameLayout mapLayout;
    @Bind(R.id.main_achieve) FrameLayout achieve_popup;
    @Bind(R.id.main_speed)
    TextView tv_speed;

    double latitude; //위도
    double longitude; // 경도
    String arrive; // 도착지 좌표

    private boolean m_bTrackingMode = false;
    private com.hyeongpil.thinkba.util.ListViewDialog mDialog;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        super.naviDrawerInit();

        tMapGps();
        tMapInit();
        weatherInit();
    }

    private void init(){
        mContext = this;
        ButterKnife.bind(this);
        mapLayout = (FrameLayout)findViewById(R.id.mapLayout);
        arr_tMapPoint = new ArrayList<TMapPoint>();
        arr_tMapCircle = new ArrayList<TMapCircle>();
        mGoogleApiClient = GlobalApplication.getInstance().getmGoogleApiClient();
        setAchievement();
    }

    private void tMapInit() {

        gpsInfo = new GpsInfo(MainActivity.this);
        if (gpsInfo.isGetLocation()) { // 현재 위치 받아오기
            latitude = gpsInfo.getLatitude();
            longitude = gpsInfo.getLongitude();
            startPoint = new TMapPoint(latitude, longitude);
        }
        mapView = new TMapView(this); // 지도 위도, 경도, 줌레벨
        mapView.setSKPMapApiKey(getString(R.string.skplanet_key));
        mapView.setTrackingMode(m_bTrackingMode); // 트래킹 모드 사용
        mapView.setZoomLevel(16);
//        mapView.setBicycleInfo(true);//자전거 도로 표시
        mapView.setBicycleFacilityInfo(true);//자전거 시설물 표시
        mapView.setIconVisibility(true); // 현재 위치 표시하는지 여부
        mapView.setLocationPoint(longitude, latitude); // 지도 현재 좌표 설정
        mapView.setCenterPoint(longitude, latitude); // 지도 현재 위치로
        mapView.setTMapLogoPosition(TMapView.TMapLogoPositon.POSITION_BOTTOMLEFT);

        mapLayout.addView(mapView);
    }
    private void weatherInit(){
        Handler handler = new WeatherReceiveHandler();
        Thread weatherThread = new WeatherThread(handler, MainActivity.this, latitude, longitude);
        weatherThread.start();
    }

    /**
     * setPath
     * 도착지 정보가 있다면 맵에 경로를 그려준다
     */
    private void setPath() {
        try {
            TMapData tmapdata = new TMapData();
            if (passPoint.getLatitude() > 0.0 && arrivePoint != null) { // 출발지와 도착지 사이의 주변검색 경유지를 그려줌
                Log.d(TAG, "setPath 1번 진입");
                ArrayList<TMapPoint> passlist = new ArrayList<TMapPoint>();
                passlist.add(passPoint);
                tmapdata.findPathDataWithType(TMapData.TMapPathType.BICYCLE_PATH, startPoint, arrivePoint, passlist, 0, new TMapData.FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine tMapPolyLine) {
                        mapView.addTMapPath(tMapPolyLine);
                    }
                });
            } else if (arrivePoint != null) { //출발지와 도착지 결과를 받아 경로를 그려줌
                Log.d(TAG, "setPath 2번 진입");
                tmapdata.findPathDataWithType(TMapData.TMapPathType.BICYCLE_PATH, startPoint, arrivePoint, new TMapData.FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine tMapPolyLine) {
                        mapView.addTMapPath(tMapPolyLine);
                    }
                });
            } else if (passPoint.getLatitude() > 0.0) { //출발지와 주변겸색 결과를 받아 자전거 경로를 그려줌
                Log.d(TAG, "setPath 3번 진입");
                tmapdata.findPathDataWithType(TMapData.TMapPathType.BICYCLE_PATH, startPoint, passPoint, new TMapData.FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine tMapPolyLine) {
                        mapView.addTMapPath(tMapPolyLine);
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "setPath error");
        }
    }

    private void tMapGps() {
        gps = new TMapGpsManager(MainActivity.this);
        gps.setMinTime(500);
        gps.setMinDistance(3);
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
        Log.d(TAG, "onLocationChange " + location.getLatitude() + " " + location.getLongitude() + " " + location.getSpeed() + " " + location.getAccuracy());
        tv_speed.setText(String.valueOf(location.getSpeed())+" km");
        if (m_bTrackingMode) {
            mapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    /**
     * setTrackingMode
     * 화면중심을 단말의 현재위치로 이동시켜주는 트래킹모드로 설정한다.
     */
    public void setTrackingMode() {
        m_bTrackingMode = !m_bTrackingMode;
        if (m_bTrackingMode) {
            Toast.makeText(MainActivity.this, "트래킹 켜짐", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "트래킹 꺼짐", Toast.LENGTH_SHORT).show();
        }
        mapView.setTrackingMode(m_bTrackingMode);
    }


    /**
     * nowClickListener
     * 화면중심을 단말의 현재위치로 이동시켜준다.
     */
    @OnClick(R.id.nowLocation)
    void now_btnClick(View view) {
        try {
            gpsInfo = new GpsInfo(MainActivity.this);
            if (gpsInfo.isGetLocation()) {
                latitude = gpsInfo.getLatitude();
                longitude = gpsInfo.getLongitude();
                mapView.setLocationPoint(longitude, latitude);
                mapView.setCenterPoint(longitude, latitude);
                //Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.map_pin_red);
                //mapView.setIcon(bitmap);
                Games.Achievements.unlock(GlobalApplication.getInstance().getmGoogleApiClient(),getString(R.string.achievement_target_capture));
            } else {
                Toast.makeText(MainActivity.this, "GPS 연동 실패", Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException e){Log.e(TAG,"now_btnClick 오류");}
    }

    @OnClick(R.id.mapMenu)
    void mapMenu_btnClick(View view) {
        String[] item = getResources().getStringArray(R.array.list_dialog_list_item);
        List<String> listItem = Arrays.asList(item);
        ArrayList<String> itemArrayList = new ArrayList<String>(listItem);
        mDialog = new ListViewDialog(mContext, getString(R.string.list_dialog_title), itemArrayList);
        mDialog.onOnSetItemClickListener(new ListViewDialog.ListViewDialogSelectListener() {
            @Override
            public void onSetOnItemClickListener(int position) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(MainActivity.this,FindRoadActivity.class);
                        intent.putExtra("startPoint",startPoint.toString());
                        startActivity(intent);
                        break;
                    case 1:
                        startActivity( new Intent(MainActivity.this, NearbyActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this,SettingActivity.class));
                        break;
                }
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
    //트래킹 모드 토글
    @OnClick(R.id.tracking)
    void tracking_btnClick(View view) {
        setTrackingMode();
    }
    //경로 안내 라인 모두 제거
    @OnClick(R.id.deletePath)
    void deletePath_btnClick(View view){mapView.removeTMapPath();}

    private List<TMapCircle> setCircle(ArrayList<TMapPoint> tMapPointArr) {
        for(int i = 0; i < tMapPointArr.size(); i++){
            TMapPoint tempPoint = tMapPointArr.get(i);
            TMapCircle tempcircle = new TMapCircle();
            tempcircle.setCenterPoint(tempPoint);
            tempcircle.setRadius(70);
            tempcircle.setAreaColor(Color.rgb(255, 0, 0));
            tempcircle.setAreaAlpha(60);
            arr_tMapCircle.add(tempcircle);
        }
        return arr_tMapCircle;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getArriveInfo();
        if (arrivePoint != null || passPoint.getLatitude() != 0.0) {
            setPath();
        }
    }
    /**
     * getArriveInfo
     * 길 찾기 에서 도착지를 받아온다
     */
    private void getArriveInfo() {
        try {
            Intent intent = getIntent();
            try {
                arrive = intent.getStringExtra("arrivePoint");
                String temp[] = arrive.split(" ");
                double latitude = Double.parseDouble(temp[1]);
                double longitude = Double.parseDouble(temp[3]);
                arrivePoint = new TMapPoint(latitude, longitude); // 도착지 포인트
            } catch (Exception e) {
                Log.e(TAG, "get arrivePoint error");
            }

            double pass_latitude = intent.getDoubleExtra("near_latitude", 0);
            double pass_Longitude = intent.getDoubleExtra("near_longitude", 0);
            passPoint = new TMapPoint(pass_latitude, pass_Longitude); //경유지 포인트

        } catch (Exception e) {
            Log.d(TAG, "getArriveInfo error");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.removeAllTMapCircle();
        //사고다발지역 표시 여부가 true라면
        Log.d(TAG, "accident :" + BasicValue.getInstance().isAccident());
        if (BasicValue.getInstance().isAccident()) {
            Handler handler = new AccidentReceiveHandler();
            Thread accidentThread = new AccidentThread(handler, MainActivity.this);
            accidentThread.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"구글 연결 해제");
        mGoogleApiClient.disconnect();
    }

    /**
     * 로그인 카운트를 받아 업적을 띄워준다.
     */
    private void setAchievement(){
        Games.setViewForPopups(mGoogleApiClient,achieve_popup);
        int loginCount = getIntent().getIntExtra("loginCount",0); // 카카오 사인업 액티비티에서 받음
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
            Toast.makeText(MainActivity.this, "구글 게임 연동이 실패하였습니다 다시 로그인 해 주세요", Toast.LENGTH_SHORT).show();
        }}catch (NullPointerException e){}
    }
    private class AccidentReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tmapPointArr = (TmapPointArr) msg.getData().getSerializable("THREAD"); // 스레드에서 tMapPointArr를 받음

            try {
                setCircle(tmapPointArr.gettMapPointArr());

                for (int i = 0; i < arr_tMapCircle.size(); i++) {
                    mapView.addTMapCircle("circle" + i, arr_tMapCircle.get(i));
                }
            }catch (NullPointerException e){Log.e(TAG,"TmapCircle error :"+e.getMessage());}
        }
    }
    private class WeatherReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }

}
