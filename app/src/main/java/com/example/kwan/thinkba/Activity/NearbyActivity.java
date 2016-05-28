package com.example.kwan.thinkba.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.kwan.thinkba.GpsInfo;
import com.example.kwan.thinkba.R;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

/**
 * Created by hp on 2016-05-29.
 */
public class NearbyActivity extends AppCompatActivity implements View.OnClickListener {
    final static String TAG = "NearbyActivity";

    ListView nearby_listview;
    LinearLayout nearby_maplayout;
    TMapView mapView;
    GpsInfo gpsInfo;
    TMapPoint currentPoint;
    TMapData tmapdata;

    Button super_btn;
    Button hospital_btn;
    Button hotel_btn;
    Button bike_btn;
    Button leisure_btn;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        setTitle("주변 검색");

        super_btn = (Button)findViewById(R.id.super_btn);
        super_btn.setOnClickListener(this);
        hospital_btn = (Button)findViewById(R.id.hospital_btn);
        hospital_btn.setOnClickListener(this);
        hotel_btn = (Button)findViewById(R.id.hotel_btn);
        hotel_btn.setOnClickListener(this);
        bike_btn = (Button)findViewById(R.id.bike_btn);
        bike_btn.setOnClickListener(this);
        leisure_btn = (Button)findViewById(R.id.leisure_btn);
        leisure_btn.setOnClickListener(this);
        nearby_listview = (ListView)findViewById(R.id.nearby_listview);
        nearby_maplayout = (LinearLayout)findViewById(R.id.nearby_mapview);

        tmapinit();

    }

    private void tmapinit(){
        gpsInfo = new GpsInfo(NearbyActivity.this);
        if(gpsInfo.isGetLocation()) { // 현재 위치 받아오기
            latitude = gpsInfo.getLatitude();
            longitude = gpsInfo.getLongitude();
            currentPoint = new TMapPoint(latitude,longitude);
        }
        Log.e(TAG,"current point"+currentPoint);
        mapView = new TMapView(this);
        mapView.setSKPMapApiKey("d5c4630e-a1ac-3ddc-8417-03e1bf83e1b4");
        mapView.setLocationPoint(longitude,latitude);
        mapView.setCenterPoint(longitude,latitude); // 현재 위치로 지도화면 젼환
        nearby_maplayout.addView(mapView);
    }

    // TODO: 2016-05-29 주변 검색 리스너, 어댑터 구현 해야함
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.super_btn:
                try {
                    ArrayList<TMapPOIItem> POIItem = tmapdata.findAroundNamePOI(currentPoint,"화장실");
                    Log.e(TAG, "POI Name: " + POIItem.get(0).getPOIName().toString());
                }catch (Exception e){Log.e(TAG,"item 오류");e.printStackTrace();}
                    tmapdata.findAroundNamePOI(currentPoint, "버스", new TMapData.FindAroundNamePOIListenerCallback() {
                        @Override
                        public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                            for (int i = 0; i < poiItem.size(); i++) {
                                TMapPOIItem item = poiItem.get(i);
                                Log.e(TAG, "POI Name: " + item.getPOIName().toString() + ", " +
                                        "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                        "Point: " + item.getPOIPoint().toString());
                            }
                        }
                    });
                break;
            case R.id.hospital_btn:

                break;
            case R.id.hotel_btn:

                break;
            case R.id.bike_btn:

                break;
            case R.id.leisure_btn:

                break;
        }
    }
}
