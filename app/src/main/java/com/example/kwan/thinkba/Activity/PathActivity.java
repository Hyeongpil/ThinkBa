package com.example.kwan.thinkba.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.kwan.thinkba.GpsInfo;
import com.example.kwan.thinkba.R;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.io.IOException;

/**
 * Created by hp on 2016-05-21.
 */
public class PathActivity extends AppCompatActivity {
    final static String TAG = "PathActivity";

    TMapView mapView;
    FrameLayout mapLayout;
    Button startBtn;
    GpsInfo gpsInfo;

    TMapPoint startPoint;
    TMapPoint arrivePoint;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        mapLayout = (FrameLayout)findViewById(R.id.pathMapLayout);
        startBtn = (Button)findViewById(R.id.start);
        startBtn.setOnClickListener(startClickListener);

        getGpsInfo();
        getArriveInfo();

        tMapInit();

    }
    /**
     * getGpsInfo
     * 현재 위치를 시작점으로 선택한다.
     */
    private void getGpsInfo(){
        gpsInfo = new GpsInfo(PathActivity.this);
        if(gpsInfo.isGetLocation()) {
            latitude = gpsInfo.getLatitude();
            longitude = gpsInfo.getLongitude();
            startPoint = new TMapPoint(latitude,longitude);
        }else{
            Toast.makeText(PathActivity.this, "GPS 연동 실패", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * getArriveInfo
     * FindRoad_Activity 에서 받은 위치를 받아 도착지로 선택한다.
     */
    private void getArriveInfo(){
        try {
            Intent intent = getIntent();
            String arrive = intent.getStringExtra("arrivePoint");
            String temp[] = arrive.split(" ");

            double latitude = Double.parseDouble(temp[1]);
            double longitude = Double.parseDouble(temp[3]);
            arrivePoint = new TMapPoint(latitude,longitude);
        }catch (Exception e){
            Log.e(TAG, "getArriveInfo 에러");
        }
    }

    private void tMapInit(){
        try {
            TMapData tmapdata = new TMapData();
            tmapdata.findPathDataWithType(TMapData.TMapPathType.BICYCLE_PATH,startPoint, arrivePoint, new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine tMapPolyLine) {
                    mapView.addTMapPath(tMapPolyLine);
                }
            });
            mapView = new TMapView(this);
            mapView.setSKPMapApiKey("d5c4630e-a1ac-3ddc-8417-03e1bf83e1b4");
            mapView.setLocationPoint(longitude,latitude);
            mapView.setCenterPoint(longitude,latitude);
            mapLayout.addView(mapView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // TODO: 2016-05-21 안내 시작 클릭 리스너 구현하기
    Button.OnClickListener startClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
