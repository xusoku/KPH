package com.davis.kangpinhui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;

public class ScreenSaverActivity extends BaseActivity {

    @Override
    protected int setLayoutView() {
        return R.layout.activity_screen_saver;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {
        setTranslucentStatusBarGone();
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
