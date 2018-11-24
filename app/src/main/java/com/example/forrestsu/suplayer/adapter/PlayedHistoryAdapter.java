package com.example.forrestsu.suplayer.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.forrestsu.suplayer.R;
import com.example.forrestsu.suplayer.bean.PlayedHistory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PlayedHistoryAdapter extends RecyclerView.Adapter<PlayedHistoryAdapter.ViewHolder> {

    private static final String TAG = "PlayedHistoryAdapter";
    private List<PlayedHistory> playedHistoryList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoIV;
        TextView titleTV;
        TextView timeTV;

        public ViewHolder (View view) {
            super(view);
            videoIV = (ImageView) view.findViewById(R.id.iv_video);
            titleTV = (TextView) view.findViewById(R.id.tv_title);
            timeTV = (TextView) view.findViewById(R.id.tv_time);
        }
    }

    public PlayedHistoryAdapter (Context context, List<PlayedHistory> playedHistoryList) {
        mContext = context;
        this.playedHistoryList = playedHistoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_palyed_history, parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick((Integer) v.getTag());
                }
            }
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (playedHistoryList != null && playedHistoryList.size() != 0) {
            Log.i(TAG, "onBindViewHolder: 开始绑定数据");
            PlayedHistory playedHistory = playedHistoryList.get(position);
            holder.titleTV.setText(playedHistory.getTitle());
            Log.i(TAG, "onBindViewHolder: title：" + playedHistory.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
            Date date = new Date((playedHistory.getPlayedTime()));
            String time = format.format(date) ;
            holder.timeTV.setText(time);
            Log.i(TAG, "onBindViewHolder: time:" + time);
            RequestOptions options = new RequestOptions()
                    .centerCrop();
            Glide.with(mContext)
                    .load(Uri.fromFile(new File(playedHistory.getSource())))
                    .apply(options)
                    .into(holder.videoIV);
            Log.i(TAG, "onBindViewHolder: source:" + playedHistory.getSource());
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        if (playedHistoryList != null && playedHistoryList.size() != 0) {
            return playedHistoryList.size();
        } else {
            return 0;
        }
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //子项点击监听
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
