package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.model.Extendedinfo;
import com.davis.kangpinhui.model.UserInfo;
import com.davis.kangpinhui.util.SharePreferenceUtils;
import com.davis.kangpinhui.util.ToastUitl;

import de.greenrobot.event.EventBus;

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

        if(view.getId()==R.id.setting_logout){

            AppApplication.token = "";
            AppApplication.userInfo=new UserInfo();
            AppApplication.extendedinfo=null;
            SharePreferenceUtils.getSharedPreferences().putString("token","");
            LoginActivity.jumpLoginActivity(this);
            EventBus.getDefault().post("loginout");
            finish();
        }else if(view.getId()==R.id.setting_about){
        }
    }
}
