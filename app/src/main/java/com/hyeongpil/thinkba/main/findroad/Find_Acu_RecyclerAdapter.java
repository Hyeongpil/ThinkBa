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

import java.util.List;

/**
 * Created by hp on 2016. 7. 29..
 */
public class Find_Acu_RecyclerAdapter extends RecyclerView.Adapter<Find_Acu_RecyclerAdapter.FindRoad_ViewHolder>{
    final static String TAG = "Find_Acu_Adapter";
    private Context mContext;
    public List<POI_Data> mListData;
    private View.OnClickListener listener;
    Find_Acu_RecyclerAdapter adapter;

    public Find_Acu_RecyclerAdapter(Context mContext, View.OnClickListener listener) {
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
        this.mListData = mListData;
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public List<POI_Data> getmListData() {
        return mListData;
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
}
