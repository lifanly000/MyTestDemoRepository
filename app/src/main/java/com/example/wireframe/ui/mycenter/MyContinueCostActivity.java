package com.example.wireframe.ui.mycenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.Application;
import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.PayRequestData;
import com.example.wireframe.protocal.protocalProcess.model.PayResponseData;
import com.example.wireframe.share.UmengShareUtils;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.utils.DataCleanManager;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MyContinueCostActivity extends BaseActivity implements OnClickListener, ProtocalEngineObserver {
	
	private TextView title ;
	private String scheduleId = "" ;
	
	private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_continue_cost);
		DataCleanManager.cleanInternalCache(this);
		DataCleanManager.cleanExternalCache(this);
		
		api = WXAPIFactory.createWXAPI(this, UmengShareUtils.WXAppId);
		api.registerApp("wxe44ca3f84ceb1fb5");
		
		title = (TextView) findViewById(R.id.title);
		
		if(getIntent().hasExtra("type")){
			if(getIntent().getStringExtra("type").equals("1")){
				//续费
				title.setText("续费");
			}else if(getIntent().getStringExtra("type").equals("2")){
				//购买
				title.setText("购买课程");
			}
		}
		if(getIntent().hasExtra("scheduleId")){
			scheduleId = getIntent().getStringExtra("scheduleId");
		}
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.item1).setOnClickListener(this);
		findViewById(R.id.item2).setOnClickListener(this);
		findViewById(R.id.item3).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.item1://支付宝
			startProgress();
			startRequest("2");
			break;
		case R.id.item2://微信
			startProgress();
			startRequest("1");
			break;
		case R.id.item3://银联
			
			break;

		default:
			break;
		}
	}
	
	private void startRequest(String payType) {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		PayRequestData data = new PayRequestData();
		data.courseId = scheduleId;
		data.payType = payType;
		engine.startRequest(SchemaDef.PAY_INFO, data);
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof PayResponseData){
			PayResponseData data=(PayResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				if(data.payType.equals("1")){
					//微信支付
					gotoWeiPay(data);
				}else{
					//支付宝
//					Intent intent = new Intent(this,WebActivity.class);
//					intent.putExtra("type", "2");
//					intent.putExtra("payUrl", data.payUrl);
//					startActivity(intent);
					Intent intent= new Intent();          
	              	intent.setAction("android.intent.action.VIEW");      
	              	Uri content_url = Uri.parse(data.payUrl);     
	              	intent.setData(content_url);    
	              	startActivity(intent);  
				}
			}else{
				if("登录Token无效".equals(data.commonData.result_msg)){
					Intent intent = new Intent(this,LoginActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(this, data.commonData.result_msg,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	}

	private void gotoWeiPay(PayResponseData data) {
		try {
			PayReq req = new PayReq();
			req.appId			= data.appid;
			req.partnerId		= data.partnerid;
			req.prepayId		= data.prepay_id;
			req.nonceStr		= data.noncestr;
			req.timeStamp		= data.timestamp;
			req.packageValue	= data.myPackage;
			req.sign			= data.sign;
//			req.extData			= "app data"; 
			// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
			api.registerApp(req.appId);
			api.sendReq(req);
		} catch (Exception e) {
			Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	@Override
	public void OnProtocalError(int errorCode) {
		endProgress();
		Toast.makeText(this, "请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnProtocalProcess(Object obj) {
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(Application.payFlag==0){
//			Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
//			Application.payFlag=100;
			setResult(110006);
			finish();
		}else if(Application.payFlag==-1){
			Toast.makeText(this, "支付失败,请稍后再试", Toast.LENGTH_SHORT).show();
			Application.payFlag=100;
		}else if(Application.payFlag==-2){
			Toast.makeText(this, "支付取消", Toast.LENGTH_SHORT).show();
			Application.payFlag=100;
		}
	}
}
