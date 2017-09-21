package com.example.zlq_pc.tvdemo4.vitamio;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.zlq_pc.tvdemo4.weight.ProgressTextView;


import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by U
 * <p/>
 * on 2017/8/29
 * <p/>
 * QQ:1347414707
 * <p/>
 * For:
 */
public class MyMediaController extends MediaController {
    private ImageButton img_back;
    private TextView textViewTime;
    private SeekBar mSeekbar;
    private ProgressTextView mProgressTextView;
    private VideoView videoView;
    private Activity activity;
    private Context context;
    private int controllerWidth = 0;//设置mediaController高度为了使横屏时top显示在屏幕顶端

    //返回监听
    private View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (activity != null) {
                activity.finish();
            }
        }
    };
    private View v;
    private TextView tvSeekBar;

    //videoview 用于对视频进行控制的等，activity为了退出
    public MyMediaController(Context context, VideoView videoView, Activity activity) {
        super(context);
        this.context = context;
        this.videoView = videoView;
        this.activity = activity;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        controllerWidth = wm.getDefaultDisplay().getWidth();
    }

    @Override
    protected View makeControllerView() {
        //加入布局文件,mymediacontroller布局名称
        v = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(getResources().getIdentifier("mediacontroller", "layout", getContext().getPackageName()), this);
        v.setMinimumHeight(controllerWidth);
        textViewTime = (TextView) v.findViewById(getResources().getIdentifier("mediacontroller_time", "id", context.getPackageName()));
        mSeekbar = (SeekBar) v.findViewById(getResources().getIdentifier("mediacontroller_seekbar", "id", context.getPackageName()));
        mProgressTextView = (ProgressTextView) v.findViewById(getResources().getIdentifier("ptv_open_persentage", "id", context.getPackageName()));
        tvSeekBar = (TextView) v.findViewById(getResources().getIdentifier("tv_seekbar", "id", context.getPackageName()));
        return v;
    }

    public SeekBar getSeekBar() {
        if (mSeekbar == null) {
            mSeekbar = (SeekBar) v.findViewById(getResources().getIdentifier("mediacontroller_seekbar", "id", context.getPackageName()));
        }
        return mSeekbar;
    }

    public ProgressTextView getProgressTextView() {
        if (mProgressTextView == null) {
            mProgressTextView = (ProgressTextView) v.findViewById(getResources().getIdentifier("ptv_open_persentage", "id", context.getPackageName()));
        }
        return mProgressTextView;
    }
    public TextView getTvSeekBar() {
        if (tvSeekBar == null) {
            tvSeekBar = (TextView) v.findViewById(getResources().getIdentifier("tv_seekbar", "id", context.getPackageName()));
        }
        return tvSeekBar;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return true;
    }

    //设置时间
    public void setTime(String time) {
        if (textViewTime != null)
            textViewTime.setText(time);
    }

    //隐藏/显示
    private void toggleMediaControlsVisiblity() {
        if (isShowing()) {
            hide();
        } else {
            show();
        }
    }

    //播放与暂停
    public void playOrPause() {
        if (videoView != null)
            if (videoView.isPlaying()) {
                videoView.pause();
            } else {
                videoView.start();
            }
    }


}