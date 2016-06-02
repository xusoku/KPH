package com.davis.kangpinhui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;

public class PayResultActivity extends BaseActivity {

    private LinearLayout pay_result_sucess,pay_result_fail;
    @Override
    protected int setLayoutView() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {

        pay_result_sucess=$(R.id.pay_result_sucess);
        pay_result_fail=$(R.id.pay_result_fail);
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
