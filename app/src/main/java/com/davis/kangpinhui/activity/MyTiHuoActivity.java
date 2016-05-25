package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;

public class MyTiHuoActivity extends BaseActivity {

    private EditText ti_huo_et;
    private TextView ti_huo_tv;



    public static void jumpMyTiHuoActivity(Context cot){
        Intent it=new Intent(cot,MyTiHuoActivity.class);
        cot.startActivity(it);
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_my_ti_huo;
    }

    @Override
    protected void initVariable() {


    }

    @Override
    protected void findViews() {

        showTopBar();
        setTitle("我的订单");

        ti_huo_et=$(R.id.ti_huo_et);
        ti_huo_tv=$(R.id.ti_huo_tv);
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