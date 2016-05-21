package com.davis.kangpinhui;

import android.app.Application;

import com.bumptech.glide.request.target.ViewTarget;
import com.davis.kangpinhui.Model.Category;

import java.util.ArrayList;

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

    public static ArrayList<Category> classiclist=new ArrayList<>();



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
