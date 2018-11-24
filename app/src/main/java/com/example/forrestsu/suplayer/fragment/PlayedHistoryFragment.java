package com.example.forrestsu.suplayer.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.forrestsu.suplayer.R;
import com.example.forrestsu.suplayer.activity.PlayVideoActivity;
import com.example.forrestsu.suplayer.adapter.PlayedHistoryAdapter;
import com.example.forrestsu.suplayer.bean.PlayedHistory;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class PlayedHistoryFragment extends Fragment implements PlayedHistoryAdapter.OnItemClickListener {

    private static final String TAG = "PlayedHistoryFragment";
    private static final int NOTIFY_ADAPTER = 1;
    private static final int NO_HISTORY = 2;

    private MyHandler myHandler = new MyHandler();

    private List<PlayedHistory> playedHistoryList = new ArrayList<PlayedHistory>();
    private static PlayedHistoryAdapter playedHistoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_played_history, container, false);
        initView(view);
        initPlayedHistory();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //指明fragment愿意添加item到选项菜单(否则, fragment将接收不到对 onCreateOptionsMenu()的调用)
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.played_history_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_history:
                initPlayedHistory();
                break;
            case R.id.delete_history:
                //清空播放历史
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("注意");
                alertDialog.setMessage("清空播放历史");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playedHistoryList.clear();
                        myHandler.sendEmptyMessage(NOTIFY_ADAPTER);
                        LitePal.deleteAll(PlayedHistory.class);
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), PlayVideoActivity.class);
        intent.putExtra("source", playedHistoryList.get(position).getSource());
        startActivity(intent);
    }

    //初始化控件
    public void initView(View view) {
        playedHistoryAdapter = new PlayedHistoryAdapter(getContext(), playedHistoryList);
        playedHistoryAdapter.setOnItemClickListener(this);
        RecyclerView historyRV = (RecyclerView) view.findViewById(R.id.rv_played_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        historyRV.setLayoutManager(linearLayoutManager);
        historyRV.setAdapter(playedHistoryAdapter);
    }

    /*
    初始化历史数据
     */
    public void initPlayedHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PlayedHistory> list = LitePal.findAll(PlayedHistory.class);
                Log.i(TAG, "run: " + list.size() + "条数据");
                if (list == null || list.size() == 0) {
                    myHandler.sendEmptyMessage(NO_HISTORY);
                } else {
                    playedHistoryList.clear();
                    for (PlayedHistory playedHistory : list) {
                        playedHistoryList.add(playedHistory);
                        Log.i(TAG, "############################################");
                        Log.i(TAG, "title:" + playedHistory.getTitle());
                        Log.i(TAG, "source:" + playedHistory.getSource());
                        Log.i(TAG, "time:" + playedHistory.getPlayedTime());
                    }
                    myHandler.sendEmptyMessage(NOTIFY_ADAPTER);
                }
            }
        }).start();
    }
    private static class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case NOTIFY_ADAPTER:
                    Log.i(TAG, "handleMessage: 更新RecyclerView");
                    playedHistoryAdapter.notifyDataSetChanged();
                    break;
                case NO_HISTORY:
                    Log.i(TAG, "handleMessage: list为空，可能没有扫描到视频");
                    break;
                default:
                    break;
            }
        }
    }

}


