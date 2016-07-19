package com.example.kwan.thinkba.navigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kwan.thinkba.R;
import com.example.kwan.thinkba.model.ArchiveModel;

import java.util.ArrayList;

/**
 * Created by hp on 2016. 7. 19..
 */
public class Archive_Adapter extends RecyclerView.Adapter<Archive_Adapter.ArchiveViewHolder>{

    private Context mContext;
    private ArrayList<ArchiveModel> archiveList;

    public Archive_Adapter(Context mContext, ArrayList<ArchiveModel> archiveList) {
        this.mContext = mContext;
        this.archiveList = archiveList;
    }

    @Override
    public ArchiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.archive_card,parent,false);

        return new ArchiveViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ArchiveViewHolder holder, int position) {
        ArchiveModel archive = archiveList.get(position);
        holder.title.setText(archive.getName());
        holder.detail.setText(archive.getDetail());
        Glide.with(mContext).load(archive.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "상세보기 구현", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return archiveList.size();
    }

    public class ArchiveViewHolder extends RecyclerView.ViewHolder{
        public TextView title, detail;
        public ImageView thumbnail, overflow;

        public ArchiveViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.archive_title);
            detail = (TextView) view.findViewById(R.id.archive_detail);
            thumbnail = (ImageView) view.findViewById(R.id.archive_thumbnail);
            overflow = (ImageView) view.findViewById(R.id.archive_overflow);
        }
    }
}
