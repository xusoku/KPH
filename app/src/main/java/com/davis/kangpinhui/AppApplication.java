package com.davis.kangpinhui;

import android.app.Application;

import com.bumptech.glide.request.target.ViewTarget;

/**
 * Created by davis on 16/5/18.
 */
public class AppApplication extends Application {

    /**
     * 全局context单例
     */
    private static AppApplication instance = null;

    public static String apptype="android";
    public static String shopid="1";
    public static String token="ED82DDC119CDD9E1F056C46C85C7D7EB";



    public static AppApplication getApplication()
    {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        instance
        instance = (AppApplication) getApplicationContext();
    }
}
