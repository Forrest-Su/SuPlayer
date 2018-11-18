package com.example.forrestsu.suplayer.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.forrestsu.suplayer.R;
import com.example.forrestsu.suplayer.bean.VideoBean;
import com.example.forrestsu.suplayer.utils.TimeUtils;

import java.io.File;
import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<VideoBean> mVideoBeanList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoIV;
        TextView titleTV;
        TextView durationTV;

        public ViewHolder (View view) {
             super(view);
             videoIV = (ImageView) view.findViewById(R.id.iv_video);
             titleTV = (TextView) view.findViewById(R.id.tv_title);
             durationTV = (TextView) view.findViewById(R.id.tv_duration);
        }
    }

    public VideoAdapter (Context context, List<VideoBean> videoBeanList) {
        mContext = context;
        mVideoBeanList = videoBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent,false);
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
        if (mVideoBeanList != null && mVideoBeanList.size() != 0) {
            VideoBean videoBean = mVideoBeanList.get(position);
            holder.titleTV.setText(videoBean.getTitle());
            if (!TextUtils.isEmpty(videoBean.getDuration())) {
                String time = TimeUtils.milliSecondsTo(Long.parseLong(videoBean.getDuration()), ":/:/:");
                holder.durationTV.setText(time);
            }
            RequestOptions options = new RequestOptions()
                    .centerCrop();
            Glide.with(mContext)
                    .load(Uri.fromFile(new File(videoBean.getPath())))
                    .apply(options)
                    .into(holder.videoIV);
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        if (mVideoBeanList != null && mVideoBeanList.size() != 0) {
            return mVideoBeanList.size();
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
