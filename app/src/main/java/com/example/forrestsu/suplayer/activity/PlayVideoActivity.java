package com.example.forrestsu.suplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.forrestsu.suplayer.MyJzvdStd;
import com.example.forrestsu.suplayer.R;
import com.example.forrestsu.suplayer.base.BaseActivity;
import com.example.forrestsu.suplayer.bean.PlayedHistory;

import cn.jzvd.Jzvd;

public class PlayVideoActivity extends BaseActivity implements MyJzvdStd.StateListener {

    private static final String TAG = "PlayVideoActivity";
    //第一次播放成功时记录当前时间
    private boolean firstOnPlaying = true;

    private String videoSource;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        Intent intent = getIntent();
        videoSource = intent.getStringExtra("source");
        Log.i(TAG, "视频来源：" + videoSource);
        title = videoSource.substring(videoSource.lastIndexOf("/") + 1);
        Log.i(TAG, "视频标题：" + title);

        MyJzvdStd mJzvdStd = (MyJzvdStd) findViewById(R.id.jz_player_video);
        mJzvdStd.setStateListener(this);
        mJzvdStd.setUp(videoSource, title, Jzvd.SCREEN_WINDOW_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onStatePlaying() {
        Log.i(TAG, "onStatePlaying: 正在播放");
        //进入播放状态，说明播放成功，将当前视频加入播放历史
        if (firstOnPlaying) {
            Log.i(TAG, "onStatePlaying: 开始存储播放历史");
            long currentTime = System.currentTimeMillis();
            PlayedHistory history = new PlayedHistory();
            history.setTitle(title);
            history.setSource(videoSource);
            history.setPlayedTime(currentTime);
            history.save();
            firstOnPlaying = false;
        }
    }

}
