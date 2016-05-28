package com.example.wireframe.ui.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.FindPasswordRequestData;
import com.example.wireframe.protocal.protocalProcess.model.FindPasswordResponseData;
import com.example.wireframe.protocal.protocalProcess.model.GetVeriCodeRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GetVeriCodeResponseData;
import com.example.wireframe.ui.BaseActivity;

public class FindPasswordActivity extends BaseActivity implements OnClickListener,ProtocalEngineObserver {
	private EditText phoneNumberEdit,veriCodeEdit,newPassword,newPassword2;
	private String  veriCodeStr,newPasswordStr,newPasswordStr2,phoneStr ;
	private Button getVeriCode ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_find_password);
		
		phoneNumberEdit = (EditText) findViewById(R.id.phoneNumber);
		veriCodeEdit = (EditText) findViewById(R.id.veriCode);
		newPassword = (EditText) findViewById(R.id.newPassword);
		newPassword2 = (EditText) findViewById(R.id.newPassword2);
		
		getVeriCode = (Button) findViewById(R.id.getVeriCode);
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.getVeriCode).setOnClickListener(this);
		findViewById(R.id.submit).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.getVeriCode: //获取验证码
			if(checkPhoneNumber()){
				getCertificateCode();
				startRequestData(1);
			}
			break;
		case R.id.submit: //修改密码
			gotoSubmit();
			break;
		default:
			break;
		}
	}
	/**
	 * 验证手机号
	 */
	private boolean checkPhoneNumber() {
		phoneStr = phoneNumberEdit.getText().toString().trim();
		if(TextUtils.isEmpty(phoneStr)){
			Toast.makeText(this, "手机号不能为空！", 0).show();
			return false ;
		}
		if(phoneStr.length() != 11){
			Toast.makeText(this, "手机号必须为11位数字。", 0).show();
			return false ;
		}
		return true ;
	}

	/**
	 * 请求
	 * @param index
	 */
	private void startRequestData(int index) {
		switch (index) {
		case 1://获取验证码
			ProtocalEngine engine = new ProtocalEngine(this);
			engine.setObserver(this);
			GetVeriCodeRequestData smsRequest=new GetVeriCodeRequestData();
			smsRequest.phone=phoneStr;
			smsRequest.type="2";
			engine.startRequest(SchemaDef.GET_VERI_CODE, smsRequest);
			break;
		case 2://
			ProtocalEngine engine2 = new ProtocalEngine(this);
			engine2.setObserver(this);
			FindPasswordRequestData data=new FindPasswordRequestData();
			data.pwdNew=newPasswordStr;
			data.code =veriCodeStr;
			engine2.startRequest(SchemaDef.FIND_PASSWORD, data);
			break;

		default:
			break;
		}
	}

	private void gotoSubmit() {
		phoneStr = phoneNumberEdit.getText().toString();
		String veriCodeStrLocal = veriCodeEdit.getText().toString();
		newPasswordStr = newPassword.getText().toString();
		newPasswordStr2 = newPassword2.getText().toString();
		if(!newPasswordStr.equals(newPasswordStr2)){
			Toast.makeText(this,"两次密码输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
			return;
		}
        if (newPasswordStr.length() < 6) {
            Toast.makeText(this,R.string.error_pwd_lengthMin, Toast.LENGTH_SHORT).show();
            newPassword.setFocusable(true);
            newPassword.requestFocus();
            return;
        }
        if (newPasswordStr.length() > 20) {
            Toast.makeText(this,R.string.error_pwd_lengthMax, Toast.LENGTH_SHORT).show();
            newPassword.setFocusable(true);
            newPassword.requestFocus();
            return;
        }
        if(!veriCodeStr.equals(veriCodeStrLocal)){
        	 Toast.makeText(this, "验证码不正确，请重新输入。", Toast.LENGTH_SHORT).show();
        	 return ;
        }
        startRequestData(2);
	}
	
	private int count = 61;
	/**
	 * 验证码倒计时
	 */
	private void getCertificateCode(){
		
		getVeriCode.setClickable(false);
			count =61;
			new Thread(){
				public void run() {
					while(count-->=0){
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								getVeriCode.setText(count+"s");
								if(count<=0){
									getVeriCode.setClickable(true);
									getVeriCode.setText("重新发送");
								}
								
							}
						});
						SystemClock.sleep(1000);
					}
				};
			}.start();
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		if(obj != null && obj instanceof GetVeriCodeResponseData ){
			GetVeriCodeResponseData smsResponse=(GetVeriCodeResponseData)obj;
			if(smsResponse.commonData.result_code.equals("0") || smsResponse.commonData.result_code.equals("0000")){
				veriCodeStr=smsResponse.code;
				Toast.makeText(this,  "请注意查收短信验证码！", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, 
					smsResponse.commonData.result_msg, Toast.LENGTH_SHORT).show();
			}
		}
		
		if(obj != null && obj instanceof FindPasswordResponseData ){
			FindPasswordResponseData smsResponse=(FindPasswordResponseData)obj;
			if(smsResponse.commonData.result_code.equals("0") || smsResponse.commonData.result_code.equals("0000")){
				Toast.makeText(this,  "找回密码成功！", Toast.LENGTH_SHORT).show();
				finish();
				
			}else{
				Toast.makeText(this, 
						smsResponse.commonData.result_msg, Toast.LENGTH_SHORT).show();
			}
		}
		
		
	}

	@Override
	public void OnProtocalError(int errorCode) {
		Toast.makeText(this,  "请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnProtocalProcess(Object obj) {
		
	}
}
