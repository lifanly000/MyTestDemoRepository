package com.example.wireframe.ui.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.UserLoginRequestData;
import com.example.wireframe.protocal.protocalProcess.model.UserLoginResponseData;
import com.example.wireframe.ui.BaseActivity;

public class LoginActivity extends BaseActivity implements OnClickListener,ProtocalEngineObserver {
	private EditText userNameEdit,passwordEdit;
	private String userNameStr ,passwordStr ;
	private boolean isFirstTime = false ;

	private CheckBox checkBox ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_login);
		
		if(getIntent().hasExtra("isFirstTime")){
			isFirstTime = getIntent().getBooleanExtra("isFirstTime", false);
		}
		
		userNameEdit = (EditText) findViewById(R.id.login_username);
		passwordEdit = (EditText) findViewById(R.id.login_password);
		checkBox = (CheckBox) findViewById(R.id.checkBox);

		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.forgetPassword).setOnClickListener(this);
		findViewById(R.id.submit).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			if(isFirstTime){
				return ;
			}
			finish();
			break;
		case R.id.forgetPassword: //找回密码
			intent = new Intent(this,FindPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.submit: //登录
			gotoLogin();
			break;
			
		case R.id.register: //注册
			intent = new Intent(this,RegisterActivity.class);
			startActivityForResult(intent, 8888);
			break;

		default:
			break;
		}
	}
	
	// 两秒内按返回键两次退出程序
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
            	setResult(ByConstants.RESULTCODE_LOGIN);
            	finish();
            	application.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if(resultCode==100){
    		Intent intent = new Intent();
			setResult(ByConstants.RESULTCODE_LOGIN, intent);
			finish();
    	}
    }
    
	/**
	 * 登录
	 */
	private void gotoLogin() {
		userNameStr = userNameEdit.getText().toString();
		passwordStr = passwordEdit.getText().toString();
		if(TextUtils.isEmpty(userNameStr)){
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(passwordStr)){
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		UserLoginRequestData data = new UserLoginRequestData();
		data.accountName = userNameStr ;
		data.pwd = passwordStr ;
		engine.startRequest(SchemaDef.USER_LOGIN, data);
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		if(obj != null && obj instanceof UserLoginResponseData){
			UserLoginResponseData countreq=(UserLoginResponseData)obj;
			if(countreq.commonData.result_code.equals("0") || countreq.commonData.result_code.equals("0000")){
				editor.putBoolean("hasLogin", checkBox.isChecked());
				editor.putString("userName", userNameStr);
				editor.commit();

				application.isLogin = true;
				application.userName = userNameStr;
				 //告诉调用登录的界面，登录成功
				Intent intent = new Intent();
				setResult(ByConstants.RESULTCODE_LOGIN, intent);
				finish();
			}else{
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
