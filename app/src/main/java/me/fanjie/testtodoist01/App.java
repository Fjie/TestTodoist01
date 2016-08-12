package me.fanjie.testtodoist01;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

import me.fanjie.testtodoist01.core.DataCenter;

/**
 * Created by fanjie on 2016/5/21.
 */
public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        AVOSCloud.initialize(this,"X0FK1VBbaEzQ9pXq7S5WgQQJ-gzGzoHsz","oXO9go7F0YlQeLDMmCbgMh4i");
        DataCenter.init();
    }

    public static App getApplication(){
        return app;
    }
}
