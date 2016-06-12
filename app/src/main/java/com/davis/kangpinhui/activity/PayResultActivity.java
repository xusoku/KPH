package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;

public class PayResultActivity extends BaseActivity {

    private LinearLayout pay_result_product, pay_result_chongzhi;

    private boolean flag = false;
    private boolean isYue = false;

    public static void jumpPayResultActivity(Context cot, boolean flag) {
        if (AppApplication.isLogin(cot)) {
            Intent it = new Intent(cot, PayResultActivity.class);
            it.putExtra("flag", flag);
            cot.startActivity(it);
        }
    }

    public static void jumpPayResultActivity(Context cot, boolean flag,boolean isYue) {
        if (AppApplication.isLogin(cot)) {
            Intent it = new Intent(cot, PayResultActivity.class);
            it.putExtra("flag", flag);
            it.putExtra("isyue", isYue);
            cot.startActivity(it);
        }
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void initVariable() {
        flag = getIntent().getBooleanExtra("flag", false);
        isYue = getIntent().getBooleanExtra("isyue", false);
    }

    @Override
    protected void findViews() {

        showTopBar();
        setTitle("支付结果");

        pay_result_product = $(R.id.pay_result_product);
        pay_result_chongzhi = $(R.id.pay_result_chongzhi);
        if (flag) {
            pay_result_product.setVisibility(View.VISIBLE);
        } else {
            pay_result_chongzhi.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {

        if(view.getId()==R.id.pay_result_kefu){
            if(isYue){
                RechargeListActivity.jumpRechargeListActivity(this);
            }else {
                AllOrderActivity.jumpAllOrderActivity(this, 0);
            }
            finish();
        }
        if(view.getId()==R.id.pay_result_kefu1){
            if(isYue){
                RechargeListActivity.jumpRechargeListActivity(this);
            }else {
                AllOrderActivity.jumpAllOrderActivity(this, 0);
            }
            finish();
        }
    }
}
