package com.davis.kangpinhui.util;

import android.content.Context;

import com.davis.kangpinhui.model.WeixinInfo;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by davis on 16/6/1.
 */
public class ThridPayUtil {

    private Context context;
    public ThridPayUtil(Context context){
        this.context=context;
    }
    /**
     * 微信支付
     */
    public void wxpay(WeixinInfo payData){
        IWXAPI wxApi= WXAPIFactory.createWXAPI(context, "wx805154ce4d985929");
//        IWXAPI wxApi=QBaoApplication.getApplication().wxApi;
        if(wxApi!=null){
            if(null != payData  ){
                PayReq req = new PayReq();
                req.appId			= payData.appid;
                req.partnerId		= payData.partnerid;
                req.prepayId		= payData.prepayid;
                req.nonceStr		= payData.noncestr;
                req.timeStamp		= payData.timestamp;
                req.packageValue	= payData.packageValue;
                req.sign			= payData.sign;
                req.extData			= "app data"; // optional
                ToastUitl.showToast("正常调起支付");
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                wxApi.sendReq(req);
            }else{
                ToastUitl.showToast("返回错误");
            }
        }else{
            ToastUitl.showToast("服务器请求错误");
        }

    }
}
