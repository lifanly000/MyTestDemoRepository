package com.example.wireframe.ui.mycenter.buy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineDetailResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.ui.mycenter.MyContinueCostActivity;
import com.example.wireframe.utils.ShowDialog;

public class MyBuyUpCourseActivity extends BaseActivity implements OnClickListener ,ProtocalEngineObserver {
	
	private TextView title ;
	private TextView totalCourse ;
	private TextView teacherName ;
	private TextView content ;
	private TextView cost ;
	
	private TextView btn1;
	private TextView btn2;
	private Button buytotal;
	private String  isFree = "" ;
	private String  isMember = "" ;
	
	private String price = "";
	
	private String courseId = "";
	private String serviceTel = "";
	
	private LinearLayout wholeDetail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_buy_up_course_activity);
		
		courseId = getIntent().getStringExtra("courseId");
		
		title = (TextView) findViewById(R.id.title);
		totalCourse = (TextView) findViewById(R.id.arrow2);
		teacherName = (TextView) findViewById(R.id.teacherName);
		content = (TextView) findViewById(R.id.text3);
		cost = (TextView) findViewById(R.id.cost);
		btn1 = (TextView) findViewById(R.id.btn1);
		btn2 = (TextView) findViewById(R.id.btn2);
		buytotal = (Button) findViewById(R.id.exit);
		wholeDetail = (LinearLayout) findViewById(R.id.wholeDetail);
		
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
		MyCourseOnlineDetailRequestData data = new MyCourseOnlineDetailRequestData();
		data.courseId = courseId;
		engine.startRequest(SchemaDef.MY_COURSE_ONLINE_DETAIL, data);
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof MyCourseOnlineDetailResponseData){
			wholeDetail.setVisibility(View.VISIBLE);
			MyCourseOnlineDetailResponseData data=(MyCourseOnlineDetailResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				title.setText(data.title);
				totalCourse.setText("共"+data.totalSchedule+"课时");
				teacherName.setText(data.teacher);
				content.setText(data.introduction);
				cost.setText(data.price);
				price =data.price;
				isFree = data.isFree;
				isMember = data.isMember;
				serviceTel = data.serviceTel;
				if(data.isMember.equals("1") && data.isFree.equals("1")){
					//是会员并且免费
					btn2.setText("免费学习");
					buytotal.setVisibility(View.GONE);
				}else{
					btn2.setText("试用课程");
					buytotal.setVisibility(View.VISIBLE);
				}
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
			ShowDialog.ShowTelephoneDialog(this, serviceTel);
			break;
		case R.id.btn2://试用课程
			intent = new Intent(this,MyUpCourseHomeActivity.class);
			if(isMember.equals("1") && isFree.equals("1")){
				intent.putExtra("isFree", true);
			}else{
				Toast.makeText(this, "订购成功", 0).show();
			}
			intent.putExtra("courseId", courseId);
			startActivity(intent);
			break;
		case R.id.exit://购买完整课程
			if(!TextUtils.isEmpty(price)){
				intent = new Intent(this,MyContinueCostActivity.class);
				intent.putExtra("price", price);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}
}
