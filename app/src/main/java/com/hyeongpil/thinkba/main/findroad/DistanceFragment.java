package com.hyeongpil.thinkba.main.findroad;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.model.POI_Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by hp on 2016. 7. 27..
 */
public class DistanceFragment extends Fragment {
    final static String TAG = "DistanceFragment";
    ListView mListView = null;
    FindRoad_Adapter findRoad_adapter;
    LayoutAnimationController controller;
    List<POI_Data> temp_itemList = new ArrayList<POI_Data>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.findload_listview, container, false);

        init(view);

        return view;
    }
    protected void init(View view){
        setAnimation();
        ListView mListView = (ListView)view.findViewById(R.id.findload_listview);
        findRoad_adapter = new FindRoad_Adapter(getActivity());
        mListView.setAdapter(findRoad_adapter);
        mListView.setLayoutAnimation(controller);
    }

    protected void setAnimation(){
        //리스트뷰 애니메이션
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);
        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(200);
        set.addAnimation(animation);
        controller = new LayoutAnimationController(set, 0.5f);
    }

    public FindRoad_Adapter getadapter() {
        return findRoad_adapter;
    }

    public void setItemList(List<POI_Data> temp_itemList) {
        Collections.sort(temp_itemList, new DistanceCompare()); // 거리순 정렬
        findRoad_adapter.setArrayItems(temp_itemList);
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
