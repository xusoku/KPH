package com.davis.kangpinhui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.MainActivity;
import com.davis.kangpinhui.Model.UserInfo;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.util.CommonManager;
import com.davis.kangpinhui.util.RegexUtils;
import com.davis.kangpinhui.util.ToastUitl;

import de.greenrobot.event.EventBus;
import retrofit2.Call;

public class LoginActivity extends BaseActivity {

    private AutoCompleteTextView login_phone;
    private EditText login_code;
    private EditText login_password;
    private TextView login_btn;
    private TextView login_last_text;
    private TextView login_register_send_code;
    private RelativeLayout login_code_relative;
    private RelativeLayout login_forget_relative;

    private boolean isLogin = true;
    public  int count = 60;
    public Handler handler = new Handler();
    public  Runnable runnable;
    private ProgressDialog progressDialog;

    public static void jumpLoginActivity(Context conx){
        Intent it=new Intent(conx, LoginActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        conx.startActivity(it);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initVariable() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);

    }

    @Override
    protected void findViews() {

        login_btn = $(R.id.login_btn);
        login_phone = $(R.id.login_phone);
        login_phone.setText("15222691381");
        login_password = $(R.id.login_password);
        login_code = $(R.id.login_code);
        login_last_text = $(R.id.login_last_text);
        login_code_relative = $(R.id.login_code_relative);
        login_register_send_code = $(R.id.login_register_send_code);
        login_forget_relative = $(R.id.login_forget_relative);

        isLogin = true;
        refreshUI();
    }

    @Override
    protected void initData() {

        runnable = new Runnable() {
            @Override
            public void run() {
                // handler自带方法实现定时
                count--;
                //Logi("123", count + "");
                if (login_register_send_code!=null) {
                    login_register_send_code.setEnabled(false);
                    login_register_send_code.setText("重发("+count + "s)");
                }
                if (count==0) {
                    handler.removeCallbacks(this);
                    count=60;
                    login_register_send_code.setEnabled(true);
                    login_register_send_code.setText("发送验证码");
                }else {
                    handler.postDelayed(this, 1000);
                }
            }
        };

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {

        switch (view.getId()) {
            case R.id.login_register_send_code:

                String mobiles=login_phone.getText().toString().trim();
                if(!TextUtils.isEmpty(mobiles)&&RegexUtils.isMobilePhoneNumber(mobiles)){
                    handler.postDelayed(runnable, 0);
                    ToastUitl.showToast("验证码发送成功");
                    Call<BaseModel> call=ApiInstant.getInstant().sendMsgRegister(AppApplication.apptype,mobiles);
                    call.enqueue(new ApiCallback<BaseModel>() {
                        @Override
                        public void onSucssce(BaseModel baseModel) {

                        }
                        @Override
                        public void onFailure() {
                            count=1;
                        }
                    });

                }else{
                    login_phone.setError("手机号不正确");
                }

                break;
            case R.id.login_forget_password:

                break;
            case R.id.login_register_text:
                isLogin = false;
                refreshUI();
                count=60;
                break;
            case R.id.login_last_text:
                if (isLogin) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    isLogin=true;
                    refreshUI();
                    count=1;
                }
                break;
            case R.id.login_btn:

                CommonManager.dismissSoftInputMethod(this,login_btn.getWindowToken());
                boolean pwdb=false;
                boolean loginb=false;
                boolean codeb=false;
                String code=login_code.getText().toString().trim();
                if(!TextUtils.isEmpty(code)){
                    codeb=true;
                }else{
                    if(!isLogin)
                    ToastUitl.showToast("验证码不能为空");
                }
                String pwd=login_password.getText().toString().trim();
                if(!TextUtils.isEmpty(pwd)&&pwd.length()>=6){
                    pwdb=true;
                }else{
                    login_password.setError("密码必须大于6位");
                }
                String mobile=login_phone.getText().toString().trim();
                if(!TextUtils.isEmpty(mobile)&&RegexUtils.isMobilePhoneNumber(mobile)){
                    loginb=true;
                }else{
                    login_phone.setError("手机号不正确");
                }


                if (isLogin) {
                    if(pwdb&&loginb){
                        progressDialog.show();
                        Call<BaseModel<UserInfo>> call=ApiInstant.getInstant().userLogin(AppApplication.apptype, mobile, pwd);
                        call.enqueue(new ApiCallback<BaseModel<UserInfo>>() {
                            @Override
                            public void onSucssce(BaseModel<UserInfo> userInfoBaseModel) {
                                progressDialog.dismiss();
                                ToastUitl.showToast("登录成功");
                                finish();
                                AppApplication.userInfo=userInfoBaseModel.object;
                                AppApplication.token=userInfoBaseModel.object.token;
                                EventBus.getDefault().post(AppApplication.userInfo);
                            }

                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                            }
                        });

                    }
                } else {
                    if(pwdb&&loginb&&codeb){
                        progressDialog.show();
                        Call<BaseModel> call=ApiInstant.getInstant().userRegister(AppApplication.apptype, mobile, pwd, code);
                        call.enqueue(new ApiCallback<BaseModel>() {
                            @Override
                            public void onSucssce(BaseModel baseModel) {
                                progressDialog.dismiss();
                                ToastUitl.showToast("注册成功");
                                isLogin=true;
                                refreshUI();
                            }
                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
                break;
        }
    }

    private void refreshUI() {
        if (isLogin) {
            login_code_relative.setVisibility(View.GONE);
            login_forget_relative.setVisibility(View.VISIBLE);
            login_btn.setText("登录");
            login_last_text.setText("＜ 随便逛逛");
        } else {
            login_forget_relative.setVisibility(View.GONE);
            login_code_relative.setVisibility(View.VISIBLE);
            login_btn.setText("注册");
            login_last_text.setText("＜ 已有账户，去登录");
        }
        login_phone.setFocusable(true);
        login_phone.setFocusableInTouchMode(true);
    }
}
