package com.example.zlq_pc.tvdemo4;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zlq_pc.tvdemo4.entity.VideoEntity;
import com.example.zlq_pc.tvdemo4.utils.DateUtils;
import com.example.zlq_pc.tvdemo4.utils.DeviceUtils;
import com.example.zlq_pc.tvdemo4.utils.PreferencesUtils;
import com.example.zlq_pc.tvdemo4.vitamio.MyMediaController;
import com.example.zlq_pc.tvdemo4.weight.ProgressTextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    //本地视频列表下标
    private int position;
    //视频类
    private VideoView mVideoView;
    //视频控制器
    private MyMediaController mMediaController;
    //本地视频列表
    private List<VideoEntity> mList;
    private String uri;
    private long currentPosition;//视频当前播放位置
    private long duration;//视频总长
    private static final int TIME = 0;
    private boolean mTimeTag = false;
    //重复次数,用来区分短按和长按
    private int repeatCount;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME:
                    mMediaController.setTime(msg.obj.toString());
                    break;
            }
        }
    };
    //uri类型,0是本地,1是网络,默认0
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;

        setContentView(R.layout.activity_video);
        initView();
        initVideo();
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 0);
        position = getIntent().getIntExtra("index", 0);
        mList = (List<VideoEntity>) getIntent().getSerializableExtra("mList");
    }

    private void initVideo() {
//        if (type == 0) {
        Log.d("Mr.U", "initVideo: mList.size()" + mList.size());
        Log.d("Mr.U", "initVideo: position" + position);
        uri = mList.get(position).getUri();

//        } else {
//            //网络获取uri
//        }
        if ("".equals(uri.trim())) {
            return;

        }
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVideoView.setVideoPath(uri);//设置播放地址
        Log.d("Mr.U", "initVideo: " + uri);
        mMediaController = new MyMediaController(this, mVideoView, VideoActivity.this);//实例化控制器
        mMediaController.show();//控制器显示3s后默认自动隐藏
        mVideoView.setMediaController(mMediaController);//绑定控制器
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//设置播放画质 高画质
        //跳转至上次播放的进度,如果未播放,进度默认为0
        currentPosition = PreferencesUtils.getLong(getApplicationContext(), uri);
        mVideoView.seekTo(currentPosition);
        //注册一个回调函数，视频播放完继续播放下一个,或者播放第一个。
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mList.size() <= position) {
                    position = 0;
                } else {
                    position++;
                }
                PreferencesUtils.putLong(getApplicationContext(), uri, 0);
                initVideo();
            }
        });
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaController.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        TextView tvSeekBar = mMediaController.getTvSeekBar();
                        SeekBar seekBar_distance = mMediaController.getSeekBar();
                        tvSeekBar.setText(DateUtils.toMS(mVideoView.getCurrentPosition()) + "");
                        int position1 = seekBar_distance.getProgress(); //seekbar当前进度
                        float x = seekBar.getWidth();//seekbar的当前位置
                        float seekbarWidth = seekBar_distance.getX(); //seekbar的宽度
                        float width = (position1 * x) / 1000 + seekbarWidth - tvSeekBar.getWidth() / 2; //seekbar当前位置的宽度
                        tvSeekBar.setX(width);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        });
        //注册一个回调函数，在网络视频流缓冲变化时调用
        mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {

            }
        });
        mVideoView.requestFocus();//取得焦点
        //动态设置时间,关闭activity时,线程结束
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mTimeTag) {
                    //时间读取线程
                    String str = getTime();
                    Message msg = new Message();
                    msg.obj = str;
                    msg.what = TIME;
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    //遥控器短按监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mMediaController.show();
        duration = mVideoView.getDuration();
        TextView mTvSeekBar = mMediaController.getTvSeekBar();
        if (repeatCount == 0) {
            currentPosition = mVideoView.getCurrentPosition();
        } else if (repeatCount == 1) {
            //长按获取初始进度
            currentPosition = mVideoView.getCurrentPosition();
//            Log.d("Mr.U", "onKeyDown:Strat " + currentPosition);
        }
        //如果是短按,返回0,否则返回长按期间指令发射的次数
        repeatCount = event.getRepeatCount();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP://上键
                DeviceUtils.adjustStreamVolume(0, VideoActivity.this);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN://下
                DeviceUtils.adjustStreamVolume(1, VideoActivity.this);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT://左 单击快退10s 双击按次数1s
                if (repeatCount == 0) {
                    currentPosition = (currentPosition > 10000) ? (currentPosition - 10000) : 0;
                } else {
                    currentPosition = (currentPosition > 1000) ? (currentPosition - 1000) : 0;
//                    Log.d("Mr.U", "onKeyDown: " + currentPosition);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT://右 单击快进10s 双击按次数1s
                if (repeatCount == 0) {
                    currentPosition = ((currentPosition + 10000) <= duration) ? (currentPosition + 10000) : duration;
                } else {
                    currentPosition = ((currentPosition + 1000) <= duration) ? (currentPosition + 1000) : duration;
//                    Log.d("Mr.U", "onKeyDown: " + currentPosition);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER://确定
                mMediaController.playOrPause();
                break;
            case KeyEvent.KEYCODE_BACK://返回 退出播放,并将进度缓存进sharepreference
                finish();
                break;
            case KeyEvent.KEYCODE_HOME://home
                break;
            default:

                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        Log.d("Mr.U", "onKeyUp: " + repeatCount);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mVideoView.seekTo(currentPosition);
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        PreferencesUtils.putLong(getApplicationContext(), uri, currentPosition);
        //停止播放视频,并释放资源
        mVideoView.stopPlayback();
        mTimeTag = true;
        super.onDestroy();
    }

    //获取当前时间 格式HH:mm
    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date());
    }

}
