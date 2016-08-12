package me.fanjie.testtodoist01.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import me.fanjie.testtodoist01.App;

/**
 * Created by fanjie on 2016/5/26.
 */
public class UiUtil {

    private static Context context = App.getApplication();

    public static int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void showInput(final EditText editText) {
        final InputMethodManager manager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               manager.showSoftInput(editText, 0);
                           }
                       },
                200);
    }

    public static void toast(String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
