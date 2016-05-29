package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;

public class LetterheadActivity extends BaseActivity {


    public static void jumpLetterheadActivity(Context cot) {
        if(AppApplication.isLogin(cot)) {
            Intent it = new Intent(cot, LetterheadActivity.class);
            cot.startActivity(it);
        }
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_letterhead;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {

        showTopBar();
        setTitle("开具发票");
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
