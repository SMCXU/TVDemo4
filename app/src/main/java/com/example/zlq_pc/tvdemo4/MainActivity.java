package com.example.zlq_pc.tvdemo4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.zlq_pc.tvdemo4.entity.DaoMaster;
import com.example.zlq_pc.tvdemo4.entity.DaoSession;
import com.example.zlq_pc.tvdemo4.entity.VideoEntity;
import com.example.zlq_pc.tvdemo4.entity.VideoEntityDao;
import com.example.zlq_pc.tvdemo4.utils.FileUtils;
import com.example.zlq_pc.tvdemo4.utils.PreferencesUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //文件路径
    private List<VideoEntity> mList;
    //文件名
    private List<String> mNameList;
    private ArrayAdapter<String> adapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferencesUtils.init("TVDemo4");
        mListView = (ListView) findViewById(R.id.mListView);
        initView();
        // 扫描功能
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请CAMERA权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        } else {
            scanMediaFile();

        }


    }


    private void initView() {
        mList = new ArrayList<VideoEntity>();
        mNameList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,mNameList);
        mListView.setAdapter(adapter);
        //点击列表跳转到指定视频
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this,VideoActivity.class);
                intent.putExtra("mList",(Serializable)mList);
                intent.putExtra("index",position);
                startActivity(intent);
            }
        });
    }

    //扫描获取数据
    private void scanMediaFile() {
        String[] args = {"mp4", "wmv", "rmvb","mkv","avi","flv"};
        mList.clear();
        mList = FileUtils.getSpecificTypeOfFile(this, args);
        for (int i = 0; i < mList.size(); i++) {
            mNameList.add(FileUtils.getFileName(mList.get(i).getUri()));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (3 == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 授权,开始扫描文件,这里应该是异步,暂时
                scanMediaFile();
            } else {
                // 未授权
            }
        }
    }
}
