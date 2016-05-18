package com.davis.kangpinhui;

import android.app.Application;

/**
 * Created by davis on 16/5/18.
 */
public class AppApplication extends Application {

    /**
     * 全局context单例
     */
    private static AppApplication instance = null;

    public static AppApplication getApplication()
    {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = (AppApplication) getApplicationContext();
    }
}
