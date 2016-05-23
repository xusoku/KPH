package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;

public class ProductDetailActivity extends BaseActivity {


    public static void jumpProductDetailActivity(Context conx,String id ){
        Intent it=new Intent(conx,ProductDetailActivity.class);
        it.putExtra("id",id);
        conx.startActivity(it);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {

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
