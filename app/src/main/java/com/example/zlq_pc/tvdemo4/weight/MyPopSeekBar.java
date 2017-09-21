package com.example.zlq_pc.tvdemo4.weight;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.zlq_pc.tvdemo4.R;

/**
 * Created by U
 * <p/>
 * on 2017/9/1
 * <p/>
 * QQ:1347414707
 * <p/>
 * For:运用popupwindow的方式动态显示进度
 */
public class MyPopSeekBar extends SeekBar {

    //定义变量
    private PopupWindow pupWindow;
    private LayoutInflater layoutInflater;
    private View mView;
    //用来表示该组件在整个屏幕内的绝对坐标，其中 mPosition[0] 代表X坐标,mPosition[1] 代表Y坐标。
    private int[] mPosition;
    //SeekBar上的Thumb的宽度，即那个托动的小黄点的宽度
    private final int mThumbWidth = 25;
    private TextView mTvProgress;

    //构造函数，初始化操作
    public MyPopSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        //获得父视图对象
        layoutInflater = LayoutInflater.from(context);
        //获得插入父节点的view对象
        mView = layoutInflater.inflate(R.layout.seekbar_pop, null);
        mTvProgress = (TextView) mView.findViewById(R.id.myPop);
        pupWindow = new PopupWindow(mView, mView.getWidth(), mView.getHeight(), true);
        mPosition = new int[2];
    }

    public void setSeekBarText(String str) {
        mTvProgress.setText(str);
    }

//    //重写触发按下监听事件
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        // TODO Auto-generated method stub
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                this.getLocationOnScreen(mPosition);
//                //控件显示位置
//                pupWindow.showAsDropDown(this, (int) event.getX(), -400);
//                break;
//            case MotionEvent.ACTION_UP:
//                pupWindow.dismiss();
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    //按键监听
    /**
     *
     * ?????????????????????????????????????????
     *
     *
     *
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.getLocationOnScreen(mPosition);
        //控件显示位置验证
        //TODO getProgress()获取参数
        pupWindow.showAsDropDown(this,getProgress(), -400);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        pupWindow.dismiss();
        return super.onKeyUp(keyCode, event);
    }

    //获得控件的宽度
    private int getViewWidth(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredWidth();
    }

    //获得控件的高度
    private int getViewHeight(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredHeight();
    }

    //重写draw方法
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        int thumb_x = this.getProgress() * (this.getWidth() - mThumbWidth) / this.getMax();
        //表示popupwindow在进度条所在y坐标减去popupwindow的高度，再减去他们直接的距离，我设置为5个dip；
        int middle = mPosition[1] - getViewHeight(mView);
        super.onDraw(canvas);
        if (pupWindow != null) {
            try {
                this.getLocationOnScreen(mPosition);
                pupWindow.update(thumb_x + mPosition[0] - getViewWidth(mView) / 2 + mThumbWidth / 2,
                        middle, getViewWidth(mView), getViewHeight(mView));

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }
}