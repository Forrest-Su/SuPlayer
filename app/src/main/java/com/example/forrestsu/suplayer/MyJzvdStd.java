package com.example.forrestsu.suplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.jzvd.JzvdStd;

public class MyJzvdStd extends JzvdStd {

    public interface StateListener {
        void onStatePlaying();
    }

    private StateListener stateListener;
    public void setStateListener(StateListener stateListener) {
        this.stateListener = stateListener;
    }

    public MyJzvdStd(Context context) {
        super(context);
    }

    public MyJzvdStd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        /*
        int i = v.getId();
        if (i == cn.jzvd.R.id.fullscreen) {
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                //click quit fullscreen
            } else {
                //click goto fullscreen
            }
        }
        */
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_standard;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    public void startVideo() {
        super.startVideo();
    }

    //onState 代表了播放器引擎的回调，播放视频各个过程的状态的回调
    //普通状态，通常指setUp之后
    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    //准备播放状态
    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        stateListener.onStatePlaying();
    }

    //播放状态
    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
    }

    //暂停状态
    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    //错误状态
    @Override
    public void onStateError() {
        super.onStateError();
    }

    //自动播放完成状态
    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
    }

    //changeUiTo 真能能修改ui的方法
    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
    }

    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();
    }

    @Override
    public void changeUiToComplete() {
        super.changeUiToComplete();
    }

    @Override
    public void changeUiToError() {
        super.changeUiToError();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }

    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
    }

    @Override
    public void startWindowTiny() {
        super.startWindowTiny();
    }

}
