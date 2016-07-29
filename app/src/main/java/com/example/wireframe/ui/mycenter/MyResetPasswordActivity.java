package com.example.wireframe.ui.mycenter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.ResetPasswordRequestData;
import com.example.wireframe.protocal.protocalProcess.model.ResetPasswordResponseData;
import com.example.wireframe.ui.BaseActivity;

public class MyResetPasswordActivity extends BaseActivity implements
		OnClickListener, ProtocalEngineObserver {
	private EditText oldpassword, newpassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_resetpassword);

		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.submit).setOnClickListener(this);

		oldpassword = (EditText) findViewById(R.id.oldpassword);
		newpassword = (EditText) findViewById(R.id.newpassword);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.submit:// 确认修改
			gotoResetPassword();
			break;

		default:
			break;
		}
	}

	/**
	 * 修改密码
	 */
	private void gotoResetPassword() {
		String oldpasswordStr = oldpassword.getText().toString();
		String newpasswordStr = newpassword.getText().toString();
		if (newpasswordStr.length() < 6) {
			Toast.makeText(this, R.string.error_pwd_lengthMin,
					Toast.LENGTH_SHORT).show();
			newpassword.setFocusable(true);
			newpassword.requestFocus();
			return;
		}
		if (newpasswordStr.length() > 20) {
			Toast.makeText(this, R.string.error_pwd_lengthMax,
					Toast.LENGTH_SHORT).show();
			newpassword.setFocusable(true);
			newpassword.requestFocus();
			return;
		}
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		ResetPasswordRequestData data = new ResetPasswordRequestData();
		data.pwdOld = oldpasswordStr;
		data.pwdNew = newpasswordStr;
		engine.startRequest(SchemaDef.RESET_PASSWORD, data);
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		if(obj != null && obj instanceof ResetPasswordResponseData){
			ResetPasswordResponseData data = (ResetPasswordResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				Toast.makeText(this, "修改密码成功。", 0).show();
				finish();
			}
		}
	}

	@Override
	public void OnProtocalError(int errorCode) {
		Toast.makeText(this, "请求失败，请检查网络设置，稍后重试！", 0).show();
	}

	@Override
	public void OnProtocalProcess(Object obj) {
		
	}
}
