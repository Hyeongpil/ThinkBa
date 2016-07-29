package com.hyeongpil.thinkba.main.findroad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.model.POI_Data;

import java.util.List;

/**
 * Created by hp on 2016. 7. 27..
 */
public class AccurateFragment extends Fragment {
    final static String TAG = "AccurateFragment";
    Find_Acu_Recycler_Adapter adapter;
    RecyclerView acu_recyclerView;
    List<POI_Data> mListData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.findload_acu_listview, container, false);
        acu_recyclerView = (RecyclerView) view.findViewById(R.id.findload_acu_recycler);
        init();

        return view;
    }

    protected void init(){

        adapter = new Find_Acu_Recycler_Adapter(getActivity(),new Find_Acu_ClickListener());
        acu_recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        acu_recyclerView.setLayoutManager(manager);
        acu_recyclerView.setHasFixedSize(true);
    }

    public void set_acu_itemlist(List<POI_Data> temp_data){
        mListData = temp_data;
        adapter.setData(mListData);
    }

    public class Find_Acu_ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int position = acu_recyclerView.getChildLayoutPosition(view);
            Intent pathIntent = new Intent(getContext(),PathActivity.class);
            pathIntent.putExtra("arrivePoint",mListData.get(position).point);
            getActivity().startActivity(pathIntent);
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        }
    }
}
