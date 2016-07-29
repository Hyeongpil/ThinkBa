package com.hyeongpil.thinkba.main.findroad;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.model.POI_Data;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hp on 2016. 7. 29..
 */
public class Find_Dis_Recycler_Adapter extends RecyclerView.Adapter<Find_Dis_Recycler_Adapter.FindRoad_ViewHolder>{
    final static String TAG = "Find_Dis_Adapter";
    private Context mContext;
    public List<POI_Data> mListData;
    private View.OnClickListener listener;
    Find_Dis_Recycler_Adapter adapter;

    public Find_Dis_Recycler_Adapter(Context mContext, View.OnClickListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public FindRoad_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.findroad_listview_item, parent, false);
        itemView.setOnClickListener(listener);
        return new FindRoad_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FindRoad_ViewHolder holder, int position) {
        POI_Data data = mListData.get(position);

        holder.getPoi_name().setText(data.getPoiname());
        holder.getPoi_adress().setText(data.getAddress());
        holder.getPoi_distance().setText(data.getDistanceStr());
    }

    @Override
    public int getItemCount() {
        if(mListData == null){
            return 0;
        }else{
            return mListData.size();
        }
    }

    public void setData(List<POI_Data> mListData){
        Collections.sort(mListData, new DistanceCompare()); // 거리순 정렬
        this.mListData = mListData;
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public class FindRoad_ViewHolder extends RecyclerView.ViewHolder{
        public TextView poi_name;
        public TextView poi_adress;
        public TextView poi_distance;

        public FindRoad_ViewHolder(View itemView) {
            super(itemView);
            poi_name = (TextView) itemView.findViewById(R.id.POI_name);
            poi_adress = (TextView) itemView.findViewById(R.id.POI_address);
            poi_distance = (TextView) itemView.findViewById(R.id.POI_distance);
        }
        public TextView getPoi_name() {return poi_name;}
        public TextView getPoi_adress() {return poi_adress;}
        public TextView getPoi_distance() {return poi_distance;}
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
