package com.example.wireframe.ui.mycenter.mycourse;

import android.content.Intent;
import android.graphics.Color;
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
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineBuyInfoRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineBuyInfoResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.ui.mycenter.MyContinueCostActivity;

public class MyDownCourseActivity extends BaseActivity implements OnClickListener,ProtocalEngineObserver {
	
	private TextView title ,totalCourse ;
	
	private TextView teacherName ,locationName,schoolLocation,studentName,studentTelphone;
	
	private TextView courseTime1,courseTime2,courseTime3;
	
	private TextView cost ,validTime ;
	
	private int index = 3 ;//1-  2   3  课时选择
	
	private String courseId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_down_course_activity);
		
		courseId = getIntent().getStringExtra("courseId");
		
		title = (TextView) findViewById(R.id.title);
		totalCourse = (TextView) findViewById(R.id.arrow2);
		teacherName = (TextView) findViewById(R.id.teacherName);
		locationName = (TextView) findViewById(R.id.locationName);
		schoolLocation = (TextView) findViewById(R.id.schoolLocation);
		studentName = (TextView) findViewById(R.id.studentName);
		studentTelphone = (TextView) findViewById(R.id.studentTelphone);
		
		courseTime1 = (TextView) findViewById(R.id.courseTime1);
		courseTime2 = (TextView) findViewById(R.id.courseTime2);
		courseTime3 = (TextView) findViewById(R.id.courseTime3);
		
		courseTime3.setBackgroundResource(R.drawable.v4_gradient_box_whole_blue);
		courseTime3.setTextColor(Color.parseColor("#FFFFFF"));
		
		cost = (TextView) findViewById(R.id.cost);
		validTime = (TextView) findViewById(R.id.validTime);
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.btn1).setOnClickListener(this);
		findViewById(R.id.btn2).setOnClickListener(this);
		findViewById(R.id.courseIntroduce).setOnClickListener(this);
		
		findViewById(R.id.courseTime1).setOnClickListener(this);
		findViewById(R.id.courseTime2).setOnClickListener(this);
		findViewById(R.id.courseTime3).setOnClickListener(this);
		
		startProgress();
		startRequest();
	}
	
	private void selectBtn(){
		courseTime1.setBackgroundResource(R.drawable.v4_gradient_box_white);
		courseTime1.setTextColor(Color.parseColor("#818181"));
		courseTime2.setBackgroundResource(R.drawable.v4_gradient_box_white);
		courseTime2.setTextColor(Color.parseColor("#818181"));
		courseTime3.setBackgroundResource(R.drawable.v4_gradient_box_white);
		courseTime3.setTextColor(Color.parseColor("#818181"));
		switch (index) {
		case 1:
			courseTime1.setBackgroundResource(R.drawable.v4_gradient_box_whole_blue);
			courseTime1.setTextColor(Color.parseColor("#FFFFFF"));
			break;
		case 2:
			courseTime2.setBackgroundResource(R.drawable.v4_gradient_box_whole_blue);
			courseTime2.setTextColor(Color.parseColor("#FFFFFF"));
			break;
		case 3:
			courseTime3.setBackgroundResource(R.drawable.v4_gradient_box_whole_blue);
			courseTime3.setTextColor(Color.parseColor("#FFFFFF"));
			break;

		default:
			break;
		}
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
		case R.id.btn2://确认续费
			intent = new Intent(this,MyContinueCostActivity.class);
			intent.putExtra("type", "1");
			startActivity(intent);
			break;
		case R.id.courseIntroduce://课程简介
			
			break;
		case R.id.courseTime1://课时选择1
			index = 1;
			selectBtn();
			break;
		case R.id.courseTime2://课时选择2
			index = 2;
			selectBtn();
			break;
		case R.id.courseTime3://课时选择3
			index = 3;
			selectBtn();
			break;

		default:
			break;
		}
	}
	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		MyCourseOfflineBuyInfoRequestData data = new MyCourseOfflineBuyInfoRequestData();
		data.courseId = courseId;
		engine.startRequest(SchemaDef.MY_COURSE_OFFLINE_BUY_INFO, data);
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof MyCourseOfflineBuyInfoResponseData){
			MyCourseOfflineBuyInfoResponseData data=(MyCourseOfflineBuyInfoResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				
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
		// TODO Auto-generated method stub
		
	}
}
