package com.hyeongpil.thinkba.main.findroad;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.model.POI_Data;

import java.util.List;

/**
 * Created by hp on 2016. 7. 27..
 */
public class AccurateFragment extends DistanceFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.findload_listview, container, false);
        init(view);
        return view;
    }

    @Override
    protected void init(View view) {
        super.init(view);
    }

    @Override
    protected void setAnimation() {
        super.setAnimation();
    }

    @Override
    public FindRoad_Adapter getadapter() {
        return super.getadapter();
    }

    public void setItemList(List<POI_Data> temp_itemList) {
        findRoad_adapter.setArrayItems(temp_itemList);
    }
}
