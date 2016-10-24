package com.davis.kangpinhui.util;

import android.widget.ImageView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.R;

/**
 * Created by xushengfu on 16/10/24.
 */
public class Glide {
    public static void setImageUrl(String url,int placeholder,int error,ImageView view){
        com.bumptech.glide.Glide.with(AppApplication.getApplication())
                .load(url)
                .placeholder(placeholder)
                .error(error)
                .into(view);
    }
}
