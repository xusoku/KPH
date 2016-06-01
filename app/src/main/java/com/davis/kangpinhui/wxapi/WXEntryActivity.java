package com.davis.kangpinhui.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.davis.kangpinhui.AppApplication;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private static String APP_ID = "wx5a9e0008073841f8";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppApplication.getApplication().wxApi.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			Toast.makeText(this, "COMMAND_GETMESSAGE_FROM_WX", Toast.LENGTH_LONG).show();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			Toast.makeText(this, "COMMAND_SHOWMESSAGE_FROM_WX", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
		this.finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		String result = "";

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "分享成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "取消分享";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "分享失败";
			break;
		default:
			result = "分享失败";
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		// TODO 微信分享 成功之后调用接口
		this.finish();
	}

}
