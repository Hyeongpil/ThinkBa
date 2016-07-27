package com.hyeongpil.thinkba.main.findroad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.model.POI_Data;

import java.util.List;

/**
 * Created by hp on 2016-05-20.
 */
public class FindRoad_Adapter extends BaseAdapter {
    final static String TAG = "FindRoad_Adapter";
    private Context mContext = null;
    public List<POI_Data> mListData;

    public FindRoad_Adapter(Context mContext) {
        super();
        this.mContext = mContext;
    }
    public void setArrayItems(List<POI_Data> items){
        mListData = items;
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        if(mListData == null){
            return  0;
        }
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final int finalposition = position;
        if(convertView == null){
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.findload_listview_item,null);

            holder.poi_name = (TextView)convertView.findViewById(R.id.POI_name);
            holder.poi_adress = (TextView)convertView.findViewById(R.id.POI_address);
            holder.poi_distance = (TextView)convertView.findViewById(R.id.POI_distance);
            convertView.setTag(holder);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pathIntent = new Intent(mContext,PathActivity.class);
                    pathIntent.putExtra("arrivePoint",mListData.get(finalposition).point);
                    mContext.startActivity(pathIntent);
                    ((Activity)mContext).finish();
                    ((Activity)mContext).overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                }
            });
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        POI_Data poi_data = mListData.get(position);
        holder.poi_name.setText(poi_data.poiname);
        holder.poi_adress.setText(poi_data.address);
        holder.poi_distance.setText(poi_data.distanceStr);
        return convertView;
    }

    private class ViewHolder{
        public TextView poi_name;
        public TextView poi_adress;
        public TextView poi_distance;
    }
}
