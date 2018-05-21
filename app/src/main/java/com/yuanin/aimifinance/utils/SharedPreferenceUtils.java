package com.yuanin.aimifinance.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author lingkai
 * @date 2017/9/19
 * @desc
 */

public class SharedPreferenceUtils {

    /**
     * 保存boolean数据至SharedPreferences
     */
    public static void save2SharedPreferences(Context context, String fileName, String dataName, boolean data) {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences = context.getSharedPreferences(fileName,
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(dataName, data);
        editor.apply();
    }

    /**
     * 获取boolean数据从SharedPreferences
     */
    public static boolean getFromSharedPreferences(Context context, String fileName, String dataName) {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        boolean data = sharedPreferences.getBoolean(dataName, true);
        return data;
    }
}
