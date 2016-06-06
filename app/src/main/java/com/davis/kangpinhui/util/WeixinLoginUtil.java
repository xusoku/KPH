package com.davis.kangpinhui.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;


/**
 * Created by davis on 16/6/6.
 */
public class WeixinLoginUtil implements Handler.Callback,PlatformActionListener {
    private static final int MSG_ACTION_CCALLBACK = 2;

    private Context context;
    public WeixinLoginUtil(Context context){
        this.context=context;
    }

    public void startLogin(){
        Platform weixinfd = ShareSDK.getPlatform(Wechat.NAME);
        weixinfd.setPlatformActionListener(this);

        if (weixinfd.isValid()) {
            weixinfd.removeAccount();
        }
        weixinfd.showUser(null);
    }

    // 回调
    @Override
    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;

        if (platform.getName().equals(Wechat.NAME)) {

        }
        UIHandler.sendMessage(msg, this);
        System.out.println(res);
        //获取资料
        platform.getDb().getUserName();//获取用户名字
        platform.getDb().getUserIcon(); //获取用户头像

    }

    @Override
    public void onError(Platform platform, int action, Throwable t) {
        t.printStackTrace();

        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);

        // 分享失败的统计
        ShareSDK.logDemoEvent(4, platform);
    }

    // 回调handleMessage
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.arg1) {
            case 1: {
                // 成功
                Platform platform= (Platform) msg.obj;

                Toast.makeText(context, "成功" + platform.getDb().getUserName(), Toast.LENGTH_SHORT).show();
            }
            break;
            case 2: {
                // 失败
                Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();

                String expName = msg.obj.getClass().getSimpleName();
                if ("WechatClientNotExistException".equals(expName)
                        || "WechatTimelineNotSupportedException".equals(expName)
                        || "WechatFavoriteNotSupportedException".equals(expName)) {
                    Toast.makeText(context, "请安装微信客户端", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case 3: {
                // 取消
                Toast.makeText(context, "取消····", Toast.LENGTH_SHORT)
                        .show();
            }
            break;
        }

        return false;
    }
}
