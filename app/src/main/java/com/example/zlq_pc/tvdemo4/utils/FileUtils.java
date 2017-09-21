package com.example.zlq_pc.tvdemo4.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.zlq_pc.tvdemo4.MainActivity;
import com.example.zlq_pc.tvdemo4.entity.VideoEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by U
 * <p>
 * on 2017/8/28
 * <p>
 * QQ:1347414707
 * <p>
 * For:
 */
public class FileUtils {

    public static List<VideoEntity> getSpecificTypeOfFile(Context context, String[] extension) {
        //从外存中获取
        Uri fileUri = MediaStore.Files.getContentUri("external");
        //筛选列，这里只筛选了：文件路径和不含后缀的文件名
        String[] projection = new String[]{
                MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE
        };
        //构造筛选语句
        String selection = "";
        for (int i = 0; i < extension.length; i++) {
            if (i != 0) {
                selection = selection + " OR ";
            }
            selection = selection + MediaStore.Files.FileColumns.DATA + " LIKE '%" + extension[i] + "'";
        }
        //按时间递增顺序对结果进行排序;待会从后往前移动游标就可实现时间递减
        String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED;
        //获取内容解析器对象
        ContentResolver resolver = context.getContentResolver();
        //获取游标
        Cursor cursor = resolver.query(fileUri, projection, selection, null, sortOrder);
        if (cursor == null)
            return null;
        //游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
        List<VideoEntity> mList = new ArrayList<>();
        if (cursor.moveToLast()) {
            do {
                //输出文件的完整路径
                String data = cursor.getString(0);
                mList.add(new VideoEntity(data,(long)0));
                Log.d("Mr.U", data);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return mList;
    }

    //通过文件路径获取文件的名字
    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.length());
    }
}