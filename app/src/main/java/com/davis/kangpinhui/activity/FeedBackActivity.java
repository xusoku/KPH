package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.api.ApiInstant;

public class FeedBackActivity extends BaseActivity {

    private EditText feedback_et;
    private TextView feedback_tv;


    public static void jumpFeedBackActivity(Context cot){
        Intent it=new Intent(cot,FeedBackActivity.class);
        cot.startActivity(it);
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initVariable() {


    }

    @Override
    protected void findViews() {

        showTopBar();
        setTitle("问题反馈");
        feedback_et=$(R.id.feedback_et);
        feedback_tv=$(R.id.feedback_tv);
    }

    @Override
    protected void initData() {

//        Call<BaseModel<>> call= ApiInstant.getInstant().feedback();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {

    }
}
