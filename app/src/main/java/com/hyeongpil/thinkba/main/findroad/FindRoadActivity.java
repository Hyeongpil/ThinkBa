package com.hyeongpil.thinkba.main.findroad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.model.POI_Data;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hp on 2016-05-17.
 */
public class FindRoadActivity extends AppCompatActivity {
    final static String TAG = "FindRoadActivity";
    TMapData tmapdata = new TMapData();
    TMapPoint startPoint;
    String distanceStr;
    @Bind(R.id.search) Button search;
    @Bind(R.id.goal) EditText goal;

    ArrayList<POI_Data> itemList;
    private InputMethodManager imm;
    ListView mListView = null;
    FindRoad_Adapter findRoad_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findroad);
        ButterKnife.bind(this);
        setTitle("길 찾기");
        getStartpoint();

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        findRoad_adapter = new FindRoad_Adapter(FindRoadActivity.this);
        mListView = (ListView)findViewById(R.id.findload_listview);
        mListView.setAdapter(findRoad_adapter);

    }
    /**
     * getArriveInfo
     * 길 찾기 에서 도착지를 받아온다
     */
    private void getStartpoint() {
        try {
            Intent intent = getIntent();
            String arrive = intent.getStringExtra("startPoint");
            String temp[] = arrive.split(" ");
            double latitude = Double.parseDouble(temp[1]);
            double longitude = Double.parseDouble(temp[3]);
            startPoint = new TMapPoint(latitude, longitude); // 도착지 포인트
            Log.e(TAG,"startPoint :"+startPoint);
        } catch (Exception e) {
            Log.d(TAG, "getStartpoint null");
        }
    }

    /**
     * searchClickListener
     * 검색어를 검색하여 item 으로 받는다.
     */
    @OnClick(R.id.search)
    void search_btnClick(View view){
        try {
            final String strData = goal.getText().toString();
            tmapdata.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                    List<POI_Data> temp_itemList = new ArrayList<POI_Data>();
                    for (int i = 0; i < poiItem.size(); i++) {
                        TMapPOIItem  item = poiItem.get(i);
                        POI_Data poi_data = new POI_Data();

                        poi_data.poiname = item.getPOIName().toString();
                        poi_data.address = item.getPOIAddress().replace("null", "");
                        poi_data.point = item.getPOIPoint().toString();
                        poi_data.distance =  item.getDistance(startPoint);
                        poi_data.distanceStr =  calDistance(item.getDistance(startPoint));
                        temp_itemList.add(poi_data);
                    }
                    Collections.sort(temp_itemList, new DistanceCompare()); // 정렬
                    findRoad_adapter.setArrayItems(temp_itemList);
                }
            });
            imm.hideSoftInputFromWindow(goal.getWindowToken(), 0); // 키보드 숨김
        }catch (Exception e){
            Log.e(TAG,"검색 오류");
        }
    }

    /**
     * calDistance
     * 거리 계산
     * @param distance
     */
    private String calDistance(Double distance){
        if(distance > 1000) { // 1km 이상
            distanceStr = ""+distance / 1000;
            distanceStr = distanceStr.substring(0,distanceStr.indexOf(".")+2)+"km";
            return distanceStr;
        }
        else{
            distanceStr = ""+distance;
            distanceStr = distanceStr.substring(0,distanceStr.indexOf("."))+'m';
            return distanceStr;
        }
    }

    /**
     * DistanceCompare
     * 거리 정렬
     */
    private class DistanceCompare implements Comparator<POI_Data> {
        @Override
        public int compare(POI_Data lhs, POI_Data rhs) {
            return lhs.distance.compareTo(rhs.distance);
        }
    }

}
