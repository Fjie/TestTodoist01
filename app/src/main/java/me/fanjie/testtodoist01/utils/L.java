package me.fanjie.testtodoist01.utils;

import android.util.Log;

/**
 * Created by fanjie on 2016/5/20.
 * 打印log
 */
public class L {

    public static String TAG = "TAG_MLOG";

    public static void e(String string){
        Log.e(TAG,string);
    }

}
