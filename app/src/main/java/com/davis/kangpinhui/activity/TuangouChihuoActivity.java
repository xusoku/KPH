package com.davis.kangpinhui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.util.ToastUitl;
import com.davis.kangpinhui.views.XWebView;

public class TuangouChihuoActivity extends BaseActivity {

    private XWebView tuangou_web;

    public static void jumpTuangouChihuoActivity(Context cot) {
            Intent it = new Intent(cot, TuangouChihuoActivity.class);
            cot.startActivity(it);
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_tuangou_chihuo;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {
        tuangou_web=$(R.id.tuangou_web);


    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initData() {
        tuangou_web.loadUrl("http://m2.kangpinhui.com:8089/weixin/tuanforapp.jsp?token=1491E1A06ACD10BF62D5A01706246560&apptype=android");
        tuangou_web.addJavascriptInterface(new DemoJavaScriptInterface(), "android");

    }



    final class DemoJavaScriptInterface {
        DemoJavaScriptInterface() {
        }
        @JavascriptInterface
        public void goAndroidPay(String s) {        // 注意这里的名称。它为clickOnAndroid(),注意，注意，严重注意
            ToastUitl.showToast("aa"+s);
        }
    }
    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {

    }
}
