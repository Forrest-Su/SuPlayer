package com.example.forrestsu.suplayer.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.forrestsu.suplayer.adapter.VideoAdapter;
import com.example.forrestsu.suplayer.base.BaseActivity;
import com.example.forrestsu.suplayer.R;
import com.example.forrestsu.suplayer.bean.VideoBean;
import com.example.forrestsu.suplayer.my_interface.AbstractProvider;
import com.example.forrestsu.suplayer.utils.VideoProvider;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class MainActivity extends BaseActivity implements VideoAdapter.OnItemClickListener,
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int NOTIFY_ADAPTER = 1;
    private static final int NO_VIDEO = 2;
    private static final int TAKE_VIDEO = 1;

    private List<VideoBean> videoBeanList = new ArrayList<VideoBean>();
    private static VideoAdapter videoAdapter;

    private MyHandler myHandler = new MyHandler();

    private DrawerLayout drawerLayout;
    private ConstraintLayout addressCL;
    private View glassView;

    private EditText addressET;
    private Button goBT;
    private Toolbar toolbar;
    private  RecyclerView videoRV;
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        requestPermission();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
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
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    /*
    初始化控件
     */
    public void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.layout_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //actionBar的具体实现由toolBar完成
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置导航图标，ToolBar最左侧的按钮名为HomeAsUp，原先默认图标为返回箭头，
            // 默认作用是返回上一个活动，这里将它改为打开DrawerLayout
            //必须先获取ToolBar实例再去设置图标，否则不起作用
            actionBar.setHomeAsUpIndicator(R.drawable.outline_menu_black_24);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //设置默认选中的菜单
        navigationView.setCheckedItem(R.id.nav_http);
        navigationView.setNavigationItemSelectedListener(this);

        addressCL = (ConstraintLayout) findViewById(R.id.cl_address);
        addressCL.setVisibility(GONE);
        glassView = (View) findViewById(R.id.view_glass);
        glassView.setOnClickListener(this);
        glassView.setVisibility(GONE);
        addressET = (EditText) findViewById(R.id.et_address);
        goBT = (Button) findViewById(R.id.bt_go_to_address);
        goBT.setOnClickListener(this);
        videoRV = (RecyclerView) findViewById(R.id.rv_video);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        videoRV.setLayoutManager(linearLayoutManager);
        videoAdapter = new VideoAdapter(MainActivity.this, videoBeanList);
        videoAdapter.setOnItemClickListener(this);
        videoRV.setAdapter(videoAdapter);
        progressBar = (ProgressBar) findViewById(R.id.bar_progress);
        progressBar.setVisibility(GONE);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, PlayVideoActivity.class);
        intent.putExtra("source", videoBeanList.get(position).getPath());
        startActivity(intent);

        //JzvdStd.startFullscreen(this, JzvdStd.class, videoBeanList.get(position).getPath(), videoBeanList.get(position).getTitle());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_local:
                break;
            case R.id.nav_http:
                if (glassView.getVisibility() == GONE) {
                    glassView.setVisibility(VISIBLE);
                }
                if (addressCL.getVisibility() == GONE) {
                    addressCL.setVisibility(VISIBLE);
                }
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_history:
                Toast.makeText(MainActivity.this, "History", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_setting:
                Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_about:
                Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.view_glass:
                if (glassView.getVisibility() == VISIBLE) {
                    glassView.setVisibility(GONE);
                }
                if (addressCL.getVisibility() == VISIBLE) {
                    addressCL.setVisibility(GONE);
                }
                closeKeyboard();
                addressET.setText("");
                break;
            case R.id.bt_go_to_address:
                if (!TextUtils.isEmpty(addressET.getText().toString())) {
                    Intent intent = new Intent(MainActivity.this, PlayVideoActivity.class);
                    intent.putExtra("source", addressET.getText().toString());
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }



    /*
    请求权限
    SD卡读写权限
     */
    private void requestPermission() {
        List<String> permissionList = new ArrayList<String>();
        //权限：读写SD卡
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(
                    new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            initVideo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this,
                                    "请授予存储权限", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    initVideo();
                } else {
                    Toast.makeText(MainActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /*
    初始化视频数据
     */
    public void initVideo() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(VISIBLE);
        }
         new Thread(new Runnable() {
             @Override
             public void run() {
                 AbstractProvider abstractProvider = new VideoProvider(MainActivity.this);
                 List<VideoBean> list = (List<VideoBean>) abstractProvider.getList();

                 if (list == null || list.size() == 0) {
                     Message message = new Message();
                     message.what = NO_VIDEO;
                     myHandler.sendMessage(message);
                 } else {
                     videoBeanList.clear();
                     for (VideoBean videoBean : list) {
                         videoBeanList.add(videoBean);
                         Log.i(TAG, "######################################");
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
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, TAKE_VIDEO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_VIDEO:
                if (resultCode == RESULT_OK) {
                    initVideo();
                    Toast.makeText(MainActivity.this, "拍摄成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "拍摄失败", Toast.LENGTH_SHORT).show();
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
        View view = getWindow().getDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager
                    = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
