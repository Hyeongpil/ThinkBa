package com.example.kwan.thinkba.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kwan.thinkba.Adapter.FindRoad_Adapter;
import com.example.kwan.thinkba.ListViewDialog;
import com.example.kwan.thinkba.POI_Data;
import com.example.kwan.thinkba.R;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016-05-17.
 */
public class FindRoadActivity extends AppCompatActivity {
    final static String TAG = "FindRoadActivity";
    TMapData tmapdata = new TMapData();
    Button search;
    EditText goal;

    ListView mListView = null;
    FindRoad_Adapter findRoad_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findroad);
        setTitle("길 찾기");

        search = (Button)findViewById(R.id.search);
        search.setOnClickListener(searchClickListener);
        goal = (EditText)findViewById(R.id.goal);
        mListView = (ListView)findViewById(R.id.loadList);

        findRoad_adapter = new FindRoad_Adapter(FindRoadActivity.this);
        mListView.setAdapter(findRoad_adapter);
    }

    /**
     * searchClickListener
     * 검색어를 검색하여 item 으로 받는다.
     */
    Button.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String strData = goal.getText().toString();
                tmapdata.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                        List<POI_Data> itemList = new ArrayList<POI_Data>();
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem  item = poiItem.get(i);
                            POI_Data poi_data = new POI_Data();
                            Log.d(TAG,"POI Name: " + item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                                    "Point: " + item.getPOIPoint().toString());
                            poi_data.poiname = item.getPOIName().toString();
                            poi_data.address = item.getPOIAddress().replace("null", "");
                            poi_data.point = item.getPOIPoint().toString();
                            itemList.add(poi_data);
                        }
                        findRoad_adapter.setArrayItems(itemList);
                    }
                });
            }catch (Exception e){
                Log.e(TAG,"검색 오류");
            }
        }
    };

}
