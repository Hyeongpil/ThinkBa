package com.hyeongpil.thinkba.main.findroad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.BaseActivity;
import com.hyeongpil.thinkba.util.ViewPageAdapter;
import com.hyeongpil.thinkba.util.model.POI_Data;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hp on 2016-05-17.
 */
public class FindRoadActivity extends BaseActivity {
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

    View containView;
    ViewPageAdapter adapter;
    ViewPager viewPager;
    DistanceFragment mDistanceFragment;
    AccurateFragment mAccurateFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container.setLayoutResource(R.layout.activity_findroad);
        containView = container.inflate();
        ButterKnife.bind(this);
        actionBarTitleSet("길 찾기", Color.WHITE);

        getStartpoint();

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        setViewpager();


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
    private void setViewpager(){
        mDistanceFragment = new DistanceFragment();
        mAccurateFragment = new AccurateFragment();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(mAccurateFragment);
        fragments.add(mDistanceFragment);
        List<String> titles = new ArrayList<String>();
        titles.add("정확도 순");
        titles.add("거리 순");

        adapter = new ViewPageAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
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
                    mDistanceFragment.setItemList(temp_itemList);
                    mAccurateFragment.setItemList(temp_itemList);
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



}
