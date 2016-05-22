package com.davis.kangpinhui;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.request.target.ViewTarget;
import com.davis.kangpinhui.Model.Category;
import com.davis.kangpinhui.Model.UserInfo;
import com.davis.kangpinhui.activity.LoginActivity;

import java.util.ArrayList;

/**
 * Created by davis on 16/5/18.
 */
public class AppApplication extends Application {

    /**
     * 全局context单例
     */
    private static AppApplication instance = null;

    public static String apptype = "android";
    public static String shopid = "1";
    public static String token = "";
    public static UserInfo userInfo;

    public static ArrayList<Category> classiclist = new ArrayList<>();


    public static AppApplication getApplication() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        instance
        instance = (AppApplication) getApplicationContext();
    }

    public static boolean isLogin(Context context) {
        if (TextUtils.isEmpty(token)) {
            LoginActivity.jumpLoginActivity(instance);
            return false;
        } else {
            return  true;
        }
    }

}
