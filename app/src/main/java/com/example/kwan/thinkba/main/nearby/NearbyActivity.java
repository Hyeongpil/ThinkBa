package com.example.kwan.thinkba.main.nearby;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.kwan.thinkba.util.daum_search.Item;
import com.example.kwan.thinkba.util.daum_search.OnFinishSearchListener;
import com.example.kwan.thinkba.util.daum_search.Searcher;
import com.example.kwan.thinkba.util.GpsInfo;
import com.example.kwan.thinkba.R;


import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by hp on 2016-05-29.
 */
public class NearbyActivity extends AppCompatActivity implements View.OnClickListener {
    final static String TAG = "NearbyActivity";
    String apiKey = "965df1f1c4824c1f097710f996cc5de2";
    Nearby_Adapter nearby_adapter;

    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();
    LinearLayout nearby_maplayout;
    MapView mapView;
    GpsInfo gpsInfo;

    @Bind(R.id.super_btn) Button super_btn;
    @Bind(R.id.hospital_btn) Button hospital_btn;
    @Bind(R.id.hotel_btn) Button hotel_btn;
    @Bind(R.id.food_btn) Button food_btn;
    @Bind(R.id.leisure_btn) Button leisure_btn;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        setTitle("주변 검색");
        ButterKnife.bind(this);

        super_btn.setOnClickListener(this);
        hospital_btn.setOnClickListener(this);
        hotel_btn.setOnClickListener(this);
        food_btn.setOnClickListener(this);
        leisure_btn.setOnClickListener(this);
        nearby_adapter = new Nearby_Adapter(NearbyActivity.this);
        ((ListView)findViewById(R.id.nearby_listview)).setAdapter(nearby_adapter);
        nearby_maplayout = (LinearLayout)findViewById(R.id.nearby_mapview);

        daumMapinit();

    }

    private void daumMapinit(){
        gpsInfo = new GpsInfo(NearbyActivity.this);
        if(gpsInfo.isGetLocation()) { // 현재 위치 받아오기
            latitude = gpsInfo.getLatitude();
            longitude = gpsInfo.getLongitude();
        }
        mapView = new MapView(this);
        mapView.setDaumMapApiKey(apiKey);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude),-1, true); // 현재위치로, 줌 설정
        moveCamera(latitude,longitude);
        nearby_maplayout.addView(mapView);
    }

    /**
     * moveCamera
     * 지도의 화면을 옮겨준다.
     **/
    public void moveCamera(double latitude, double longitude){
        MapPointBounds mapPointBounds = new MapPointBounds();
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude,longitude);
        MapPOIItem poiItem = new MapPOIItem();
        poiItem.setItemName("현재 위치");
        poiItem.setMapPoint(mapPoint);
        poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
        poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
        poiItem.setCustomImageAutoscale(false);
        poiItem.setCustomImageAnchor(0.5f, 1.0f);
        mapPointBounds.add(mapPoint);
        mapView.addPOIItem(poiItem);
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));
    }

    /**
     * search
     * 키워드 검색
     **/
    public void search(String query){
        int radius = 10000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
        int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개

        Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
        searcher.searchKeyword(getApplicationContext(), query, latitude, longitude, radius, page, apiKey, new OnFinishSearchListener() {
            @Override
            public void onSuccess(List<Item> itemList) {
                mapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                showResult(itemList); // 검색 결과 보여줌
                nearby_adapter.setArrayItems(itemList);
            }

            @Override
            public void onFail() {Log.e(TAG,"지도 검색 오류");
            }
        });
    }

    /**
     * showResult
     * 검색 결과 POI List
     **/
    private void showResult(List<Item> itemList) {
        MapPointBounds mapPointBounds = new MapPointBounds();

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);

            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(item.title);
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
            poiItem.setCustomImageAutoscale(false);
            poiItem.setCustomImageAnchor(0.5f, 1.0f);

            mapView.addPOIItem(poiItem);
            mTagItemMap.put(poiItem.getTag(), item);
        }

        // 지도 위치 변경
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));
        MapPOIItem[] poiItems = mapView.getPOIItems();
        if(poiItems.length >0){
            mapView.selectPOIItem(poiItems[0],false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.super_btn:
                search("편의점");
               break;
            case R.id.hospital_btn:
                search("병원");
                break;
            case R.id.hotel_btn:
                search("숙박");
                break;
            case R.id.food_btn:
                search("음식점");
                break;
            case R.id.leisure_btn:
                search("관광명소");
                break;
        }
    }
}
