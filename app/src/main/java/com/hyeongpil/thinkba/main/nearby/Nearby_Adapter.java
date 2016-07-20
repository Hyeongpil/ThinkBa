package com.hyeongpil.thinkba.main.nearby;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.main.MainActivity;
import com.hyeongpil.thinkba.util.daum_search.Item;

import java.util.List;

/**
 * Created by hp on 2016-05-29.
 */
public class Nearby_Adapter extends BaseAdapter {
    final static String TAG = "Nearby_Adapter";
    private Context mContext;
    private List<Item> mItems;

    public Nearby_Adapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setArrayItems(List<Item> items){
        mItems = items;
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        if(mItems == null){
            return  0;
        }
        return mItems.size();
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
        if(convertView == null){
            final int finalPosition = position;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nearby_listview_item,parent,false);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mapIntent = new Intent(mContext,MainActivity.class);
                    Log.e(TAG,"near_latitude :"+mItems.get(finalPosition).latitude);
                    mapIntent.putExtra("near_latitude",mItems.get(finalPosition).latitude);
                    mapIntent.putExtra("near_longitude",mItems.get(finalPosition).longitude);
                    mContext.startActivity(mapIntent);
                }
            });
        }
        ((TextView)convertView.findViewById(R.id.tv_title)).setText(mItems.get(position).title);
        ((TextView)convertView.findViewById(R.id.tv_tel)).setText(mItems.get(position).phone);
        ((TextView)convertView.findViewById(R.id.tv_distance)).setText(convertDistance(mItems.get(position).distance));

        return convertView;

    }
    public String convertDistance(double distance){
        String reDistance;
        if(distance > 1000){
            distance = distance / 1000;
            reDistance = String.valueOf(distance)+"km";
            return reDistance;
        }
        reDistance = String.valueOf(distance)+"m";
        return reDistance;
    }
}
