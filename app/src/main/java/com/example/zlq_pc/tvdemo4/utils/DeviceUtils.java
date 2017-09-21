package com.example.zlq_pc.tvdemo4.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by U
 * <p>
 * on 2017/8/28
 * <p>
 * QQ:1347414707
 * <p>
 * For:
 */
public class DeviceUtils {

    /**
     * 更改系统音量 0 增长;
     *              其它 降低;
     * */
    public static void adjustStreamVolume(int type,Context mContext){
        //初始化音频管理器
        AudioManager mAudioManager = (AudioManager)mContext .getSystemService(mContext.AUDIO_SERVICE);
        //获取系统最大音量
        int maxVolume=mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (0==type){
            //增加电量
            mAudioManager.adjustStreamVolume (AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,AudioManager.FX_FOCUS_NAVIGATION_UP);
        }else {
            //减少音量
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FX_FOCUS_NAVIGATION_UP);
        }


    }

}