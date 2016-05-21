package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;

public class SettingActivity extends BaseActivity {


    public static void jumpSettingActivity(Context cot){
        Intent it=new Intent(cot,SettingActivity.class);
        cot.startActivity(it);
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {

        showTopBar();
        setTitle("设置");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {

    }
}
