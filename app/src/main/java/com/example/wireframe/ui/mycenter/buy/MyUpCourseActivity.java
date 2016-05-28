package com.example.wireframe.ui.mycenter.buy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineBuyInfoRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineBuyInfoResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;

public class MyUpCourseActivity extends BaseActivity implements OnClickListener ,ProtocalEngineObserver {
	
	private TextView title ;
	private TextView totalCourse ;
	private TextView teacherName ;
	private TextView content ;
	private TextView cost ;
	
	private TextView btn1;
	private TextView btn2;
	private TextView buytotal;
	
	private String courseId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_up_course_activity);
		
		courseId = getIntent().getStringExtra("courseId");
		
		title = (TextView) findViewById(R.id.title);
		totalCourse = (TextView) findViewById(R.id.arrow2);
		teacherName = (TextView) findViewById(R.id.teacherName);
		content = (TextView) findViewById(R.id.text3);
		cost = (TextView) findViewById(R.id.cost);
		btn1 = (TextView) findViewById(R.id.btn1);
		btn2 = (TextView) findViewById(R.id.btn2);
		buytotal = (TextView) findViewById(R.id.exit);
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.btn1).setOnClickListener(this);
		findViewById(R.id.btn2).setOnClickListener(this);
		findViewById(R.id.exit).setOnClickListener(this);
		
		startProgress();
		startRequest();
	}

	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		MyCourseOnlineBuyInfoRequestData data = new MyCourseOnlineBuyInfoRequestData();
		data.courseId = courseId;
		engine.startRequest(SchemaDef.MY_COURSE_ONLINE_BUY_INFO, data);
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof MyCourseOnlineBuyInfoResponseData){
			MyCourseOnlineBuyInfoResponseData data=(MyCourseOnlineBuyInfoResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				title.setText(data.title);
				
			}else{
				if("登录Token无效".equals(data.commonData.result_msg)){
					Intent intent = new Intent(this,LoginActivity.class);
					startActivityForResult(intent, ByConstants.REQUEST_LOGIN);
				}else{
					Toast.makeText(this, data.commonData.result_msg,
							Toast.LENGTH_SHORT).show();
				}
			}
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
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.btn1://联系客服
			
			break;
		case R.id.btn2://试用课程
			intent = new Intent(this,MyUpCourseHomeActivity.class);
			startActivity(intent);
			break;
		case R.id.exit://购买完整课程
			
			break;

		default:
			break;
		}
	}
}
