package com.example.wireframe.ui.mycenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.FeedBackRequestData;
import com.example.wireframe.protocal.protocalProcess.model.FeedBackResponseData;
import com.example.wireframe.ui.BaseActivity;

public class MyAdviceActivity extends BaseActivity implements OnClickListener,ProtocalEngineObserver {
	private EditText phoneNumber,email,message;
	private String phoneStr ,emailStr,messageStr ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_advice);
		
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		email = (EditText) findViewById(R.id.email);
		message = (EditText) findViewById(R.id.message);
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.submit).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.submit: //登录
			gotoSubmit();
			break;
			
		default:
			break;
		}
	}
	/**
	 * 登录
	 */
	private void gotoSubmit() {
		phoneStr = phoneNumber.getText().toString();
		emailStr = email.getText().toString();
		messageStr = message.getText().toString();
		if(TextUtils.isEmpty(phoneStr)){
			Toast.makeText(this, "手机号不能为空", 0).show();
			return;
		}
		if(phoneStr.length()!=11){
			Toast.makeText(this, "手机号必须为11位数字", 0).show();
			return;
		}
		if(TextUtils.isEmpty(emailStr)){
			Toast.makeText(this, "邮箱不能为空", 0).show();
			return;
		}
		if(TextUtils.isEmpty(messageStr)){
			Toast.makeText(this, "建议不能为空", 0).show();
			return;
		}
		//TODO
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		FeedBackRequestData data = new FeedBackRequestData();
		data.phone = phoneStr ;
		data.email = emailStr ;
		data.content = messageStr ;
		engine.startRequest(SchemaDef.FEED_BACK_INFO, data);
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		if(obj != null && obj instanceof FeedBackResponseData){
			FeedBackResponseData countreq=(FeedBackResponseData)obj;
			if(countreq.commonData.result_code.equals("0") || countreq.commonData.result_code.equals("0000")){
				Toast.makeText(this, "感谢您提出的宝贵意见，我们会尽快给您回复", 0).show();
				finish();
			}else{
				// 跳转到绑定手机号界面
				Toast.makeText(this, countreq.commonData.result_msg, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void OnProtocalError(int errorCode) {
		Toast.makeText(this, "请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnProtocalProcess(Object obj) {
		
	}
}
