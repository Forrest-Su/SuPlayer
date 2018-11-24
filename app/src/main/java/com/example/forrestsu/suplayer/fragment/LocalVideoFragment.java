package com.example.forrestsu.suplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.forrestsu.suplayer.R;
import com.example.forrestsu.suplayer.activity.PlayVideoActivity;
import com.example.forrestsu.suplayer.adapter.VideoAdapter;
import com.example.forrestsu.suplayer.bean.VideoBean;
import com.example.forrestsu.suplayer.my_interface.AbstractProvider;
import com.example.forrestsu.suplayer.utils.VideoProvider;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LocalVideoFragment extends Fragment implements VideoAdapter.OnItemClickListener {

    private static final String TAG = "LocalVideoFragment";

    private static final int NOTIFY_ADAPTER = 1;
    private static final int NO_VIDEO = 2;
    private static final int TAKE_VIDEO = 1;

    private MyHandler myHandler = new MyHandler();

    private List<VideoBean> videoBeanList = new ArrayList<VideoBean>();
    private static VideoAdapter videoAdapter;

    private static ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_video, container, false);
        initView(view);
        initVideo();
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
        inflater.inflate(R.menu.local_video_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.take_video:
                takeVideo();
                break;
            case R.id.refresh:
                initVideo();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
        intent.putExtra("source", videoBeanList.get(position).getPath());
        startActivity(intent);

        //JzvdStd.startFullscreen(this, JzvdStd.class, videoBeanList.get(position).getPath(), videoBeanList.get(position).getTitle());
    }

    /*
    初始化控件
     */
    public void initView(View view) {
        //addressET = (EditText) view.findViewById(R.id.et_address);
        //Button goBT = (Button) view.findViewById(R.id.bt_go_to_address);
        //goBT.setOnClickListener(this);
        RecyclerView videoRV = (RecyclerView) view.findViewById(R.id.rv_video);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        videoRV.setLayoutManager(linearLayoutManager);
        videoAdapter = new VideoAdapter(getContext(), videoBeanList);
        videoAdapter.setOnItemClickListener(this);
        videoRV.setAdapter(videoAdapter);
        progressBar = (ProgressBar) view.findViewById(R.id.bar_progress);
        progressBar.setVisibility(GONE);
    }

    /*
    初始化视频数据
     */
    public void initVideo() {
        if (progressBar.getVisibility() == GONE) {
            progressBar.setVisibility(VISIBLE);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                AbstractProvider abstractProvider = new VideoProvider(getActivity());
                List<VideoBean> list = (List<VideoBean>) abstractProvider.getList();
                if (list == null || list.size() == 0) {
                    Message message = new Message();
                    message.what = NO_VIDEO;
                    myHandler.sendMessage(message);
                } else {
                    videoBeanList.clear();
                    for (VideoBean videoBean : list) {
                        videoBeanList.add(videoBean);
                        Log.i(TAG, "############################################");
                        Log.i(TAG, "title:" + videoBean.getTitle());
                        Log.i(TAG, "album:" + videoBean.getAlbum());
                        Log.i(TAG, "artist:" + videoBean.getArtist());
                        Log.i(TAG, "displayName:" + videoBean.getDisplayName());
                        Log.i(TAG, "mineType:" + videoBean.getMimeType());
                        Log.i(TAG, "path:" + videoBean.getPath());
                        Log.i(TAG, "size:" + videoBean.getSize());
                        Log.i(TAG, "duration:" + videoBean.getDuration());
                    }
                    Message message = new Message();
                    message.what = NOTIFY_ADAPTER;
                    myHandler.sendMessage(message);
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
                    videoAdapter.notifyDataSetChanged();
                    if (progressBar.getVisibility() == VISIBLE) {
                        progressBar.setVisibility(GONE);
                    }
                    break;
                case NO_VIDEO:
                    Log.i(TAG, "handleMessage: list为空，可能没有扫描到视频");
                    if (progressBar.getVisibility() == VISIBLE) {
                        progressBar.setVisibility(GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /*
    拍摄视频
     */
    public void takeVideo() {
        Log.i(TAG, "takeVideo: 录视频");
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, TAKE_VIDEO);
        } else {
            Log.i(TAG, "takeVideo: 错误");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //如果希望结果传回Fragment，必须加上下面这一句
        super.onActivityResult(requestCode, resultCode,data);
        switch (requestCode) {
            case TAKE_VIDEO:
                if (resultCode == RESULT_OK) {
                    initVideo();
                    Toast.makeText(getContext(), "拍摄成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "拍摄失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /*
    关闭软键盘
     */
    private void closeKeyboard() {
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
        View view = appCompatActivity.getWindow().getDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager
                    = (InputMethodManager) appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
