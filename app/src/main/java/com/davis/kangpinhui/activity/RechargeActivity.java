package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;

public class RechargeActivity extends BaseActivity {


    public static void jumpRechangeActivity(Context cot) {
        if(AppApplication.isLogin(cot)) {
            Intent it = new Intent(cot, RechargeActivity.class);
            cot.startActivity(it);
        }
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_rechange;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {
        showTopBar();
        setTitle("账户充值");

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
