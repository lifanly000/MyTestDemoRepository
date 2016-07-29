package com.example.wireframe.ui.mycenter;

import android.content.Intent;
import android.graphics.Color;
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
import com.example.wireframe.protocal.protocalProcess.model.GetVeriCodeRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GetVeriCodeResponseData;
import com.example.wireframe.protocal.protocalProcess.model.UserRegisterRequestData;
import com.example.wireframe.protocal.protocalProcess.model.UserRegisterResponseData;
import com.example.wireframe.ui.BaseActivity;

public class RegisterActivity extends BaseActivity implements OnClickListener,ProtocalEngineObserver {
	private EditText phoneNumberEdit,veriCodeEdit,locationEdit,userNameEdit,passwordEdit;
	private String userNameStr ,passwordStr,locationStr,phoneStr ;
	private String veriCodeStr = "";
	private Button getVeriCode ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_register);
		
		userNameEdit = (EditText) findViewById(R.id.userName);
		passwordEdit = (EditText) findViewById(R.id.password);
		phoneNumberEdit = (EditText) findViewById(R.id.phoneNumber);
		veriCodeEdit = (EditText) findViewById(R.id.veriCode);
		locationEdit = (EditText) findViewById(R.id.location);
		
		getVeriCode = (Button) findViewById(R.id.getVeriCode);
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.getVeriCode).setOnClickListener(this);
		findViewById(R.id.submit).setOnClickListener(this);
		findViewById(R.id.selectLocation).setOnClickListener(this);
		findViewById(R.id.prototalText).setOnClickListener(this);
		findViewById(R.id.login).setOnClickListener(this);
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
		case R.id.selectLocation: //选择地区
			intent = new Intent(this,MyLocationSelectActivity.class);
			startActivityForResult(intent, 10010);
			break;
		case R.id.prototalText: //用户协议
			intent = new Intent(this,UserProtocalActivity.class);
			startActivity(intent);
			break;
		case R.id.submit: //注册
			gotoRegister();
			break;
		case R.id.login: //登录
			finish();
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
			smsRequest.type="1";
			engine.startRequest(SchemaDef.GET_VERI_CODE, smsRequest);
			break;
		case 2://注册
			ProtocalEngine engine2 = new ProtocalEngine(this);
			engine2.setObserver(this);
			UserRegisterRequestData data=new UserRegisterRequestData();
			data.accountName=userNameStr;
			data.phone  = phoneStr;
			data.pwd=passwordStr;
			data.code =veriCodeStr;
			data.cityCode = locationCode;
			engine2.startRequest(SchemaDef.USER_REGISTER, data);
			break;

		default:
			break;
		}
	}
	private  String locationCode = "";
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode ==10010){
			if(resultCode==10086){
				locationCode = data.getStringExtra("locationCode");
				locationEdit.setText(data.getStringExtra("locationName"));
				locationEdit.setTextColor(Color.BLACK);
			}
		}
	}
	
	/**
	 * 注册
	 */
	private void gotoRegister() {
//		userNameStr = userNameEdit.getText().toString();
		passwordStr = passwordEdit.getText().toString();
		phoneStr = phoneNumberEdit.getText().toString();
		String veriCodeStrLocal = veriCodeEdit.getText().toString();
		locationStr = locationEdit.getText().toString();
//        if (userNameStr.length() < 3) {
//            Toast.makeText(this, R.string.error_name_lengthMin, Toast.LENGTH_SHORT).show();
//            userNameEdit.setFocusable(true);
//            userNameEdit.requestFocus();
//            return;
//        }
//        if (userNameStr.length() > 12) {
//            Toast.makeText(this, R.string.error_name_lengthMax, Toast.LENGTH_SHORT).show();
//            userNameEdit.setFocusable(true);
//            userNameEdit.requestFocus();
//            return;
//        }
//        if(!userNameStr.matches("^[a-zA-Z0-9\u4e00-\u9fa5]{3,12}$")){
//        	 Toast.makeText(this, "用户名必须为3-12位英文数字或汉字", Toast.LENGTH_SHORT).show();
//             userNameEdit.setFocusable(true);
//             userNameEdit.requestFocus();
//             return;
//        }
        if (passwordStr.length() < 6) {
            Toast.makeText(this,R.string.error_pwd_lengthMin, Toast.LENGTH_SHORT).show();
            passwordEdit.setFocusable(true);
            passwordEdit.requestFocus();
            return;
        }
        if (passwordStr.length() > 20) {
            Toast.makeText(this,R.string.error_pwd_lengthMax, Toast.LENGTH_SHORT).show();
            passwordEdit.setFocusable(true);
            passwordEdit.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(veriCodeStrLocal)){
        	Toast.makeText(this, "验证码不能为空。", Toast.LENGTH_SHORT).show();
       	 return ;
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
		
		if(obj != null && obj instanceof UserRegisterResponseData ){
			UserRegisterResponseData smsResponse=(UserRegisterResponseData)obj;
			if(smsResponse.commonData.result_code.equals("0") || smsResponse.commonData.result_code.equals("0000")){
				Toast.makeText(this,  "注册成功！", Toast.LENGTH_SHORT).show();
				sp.edit().putBoolean("hasLogin", true);
				sp.edit().putString("userName", userNameStr);
				sp.edit().commit();
				application.isLogin = true ;
				application.userName = userNameStr ;
				setResult(100);
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
