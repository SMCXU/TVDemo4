package com.example.zlq_pc.tvdemo4.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by U
 * <p/>
 * on 2017/8/30
 * <p/>
 * QQ:1347414707
 * <p/>
 * For:
 */
@Entity
public class VideoEntity implements Serializable{
    private String uri;
    private Long currentPosition;

    @Keep
    public VideoEntity(String uri, Long currentPosition) {
        this.uri = uri;
        this.currentPosition = currentPosition;
    }

    @Generated(hash = 1984976152)
    public VideoEntity() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Long currentPosition) {
        this.currentPosition = currentPosition;
    }
}