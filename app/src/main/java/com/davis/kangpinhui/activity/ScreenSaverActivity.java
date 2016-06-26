package com.davis.kangpinhui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;

public class ScreenSaverActivity extends BaseActivity implements View.OnClickListener {

    private ImageView screen_iv;
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
        screen_iv=$(R.id.screen_iv);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        screen_iv.setOnClickListener(this);
    }

    @Override
    public void doClick(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.screen_iv:
                finish();
                break;
        }
    }
}
