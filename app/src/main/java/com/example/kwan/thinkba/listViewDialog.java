package com.example.kwan.thinkba;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by hp on 2016-05-16.
 */
public class ListViewDialog extends Dialog {
    private static final String TAG = "ListViewDialog";
    private ListViewDialogSelectListener mListener;
    private Context mContext;
    private ArrayList<String> mStrListItem;
    private ListView mListView;

    public ListViewDialog(Context context, String title, ArrayList<String> item) {
        super(context);
        setContentView(R.layout.map_menu_listview);
        mContext = context;
        findViews();
        setTitle(title);
        mStrListItem = item;
        createListViewDialog();
    }

    private void findViews(){
        mListView = (ListView) findViewById(R.id.list_view_dialog);
    }
    private void createListViewDialog(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mStrListItem);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mListener.onSetOnItemClickListener(arg2);
            }
        });
    }
    /**
     * listener 함수
     */
    public void onOnSetItemClickListener(ListViewDialogSelectListener listener){
        mListener = listener;
    }
    /**
     * interface
     */
    public interface ListViewDialogSelectListener{
        public void onSetOnItemClickListener(int position);
    }
}
