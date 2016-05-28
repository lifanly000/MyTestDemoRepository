package com.eblock.emama.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.eblock.emama.Application;
import com.eblock.emama.R;
import com.example.wireframe.share.UmengShareUtils;
import com.example.wireframe.ui.mycenter.MyContinueCostActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, UmengShareUtils.WXAppId);
        api.handleIntent(getIntent(), this);
//        Toast.makeText(this,"回调~ ", 1).show();
//        System.out.println("回调了啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp( BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		Application.payFlag = resp.errCode;
//		Intent intent = new Intent(this,MyContinueCostActivity.class);
//		intent.putExtra("resp", resp.errCode);
//		this.startActivity(intent);
		this.finish();

//		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("提示");
//			builder.setMessage("支付结果："+String.valueOf(resp.errCode));
//			builder.show();
//		}
	}
}