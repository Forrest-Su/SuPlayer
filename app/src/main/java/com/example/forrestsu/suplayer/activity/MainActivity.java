package com.example.forrestsu.suplayer.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.forrestsu.suplayer.base.BaseActivity;
import com.example.forrestsu.suplayer.R;
import com.example.forrestsu.suplayer.fragment.AboutFragment;
import com.example.forrestsu.suplayer.fragment.InternetVideoFragment;
import com.example.forrestsu.suplayer.fragment.LocalVideoFragment;
import com.example.forrestsu.suplayer.fragment.PlayedHistoryFragment;
import com.example.forrestsu.suplayer.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private Fragment currentFragment, localVideoFragment, internetVideoFragment,
            playedHistoryFragment, settingFragment, aboutFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        requestPermission();
    }

    /*
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_video_menu, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.take_video:
                return false;
            case R.id.refresh:
                return false;
            case R.id.refresh_history:
                return false;
            case R.id.delete_history:
                return false;
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
        //actionBar的具体实现由toolBar完成
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("本地视频");
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
        navigationView.setCheckedItem(R.id.nav_local);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_local:
                if (localVideoFragment == null) {
                    localVideoFragment = new LocalVideoFragment();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), localVideoFragment);
                getSupportActionBar().setTitle("本地视频");
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_http:
                if (internetVideoFragment == null) {
                    internetVideoFragment = new InternetVideoFragment();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), internetVideoFragment);
                getSupportActionBar().setTitle("网络资源");
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_history:
                if (playedHistoryFragment == null) {
                    playedHistoryFragment = new PlayedHistoryFragment();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), playedHistoryFragment);
                getSupportActionBar().setTitle("播放历史");
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_setting:
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), settingFragment);
                getSupportActionBar().setTitle("设置");
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_about:
                if (aboutFragment == null) {
                    aboutFragment = new AboutFragment();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), aboutFragment);
                getSupportActionBar().setTitle("关于");
                drawerLayout.closeDrawers();
                break;
            default:
                break;
        }
        return true;
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
            initFragment();
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
                    initFragment();
                } else {
                    Toast.makeText(MainActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }


    //初始化Fragment，默认显示localFragment
    public void initFragment() {
        if (localVideoFragment == null) {
            localVideoFragment = new LocalVideoFragment();
        }
        if (!localVideoFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.fl_show_fragment, localVideoFragment).commit();
            currentFragment = localVideoFragment;
        }

    }
    /*
     添加或者显示Fragment
      */
    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment) {
        if (currentFragment == fragment)
            return;
        if (!fragment.isAdded()) {
            transaction.hide(currentFragment).add(R.id.fl_show_fragment, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).addToBackStack(null).commit();
        }
        currentFragment = fragment;
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