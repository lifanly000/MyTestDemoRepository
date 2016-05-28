package com.example.wireframe.ui.mycenter.buy;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.Application;
import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.Course;
import com.example.wireframe.protocal.protocalProcess.model.MyBuyCourseOfflineResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyBuyCourseOnlineResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.view.XListView;

public class MyBuyCourseActivity extends BaseActivity implements OnClickListener ,ProtocalEngineObserver{
	
	private TextView downCourse,upCourse;
	private XListView listView ;
	private boolean isUpCourse =false ;
	private BaseAdapter adapter ;
	public ArrayList<Course> courses = new ArrayList<Course>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_buy_course);
		downCourse = (TextView) findViewById(R.id.downCourse);
		upCourse = (TextView) findViewById(R.id.upCourse);
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.downCourse).setOnClickListener(this);
		findViewById(R.id.upCourse).setOnClickListener(this);
		
		listView = (XListView) findViewById(R.id.listView);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.setDividerHeight(0);
		
		isUpCourse =false ;
		adapter = new MyUpAdapter();
		listView.setAdapter(adapter);
		startProgress();
		if(isUpCourse){
			startRequest(1);
		}else{
			startRequest(2);
		}
	}
	
	private void startRequest(int index){
		switch (index) {
		case 1:
			//线上
			ProtocalEngine engine = new ProtocalEngine(this);
			engine.setObserver(this);
			engine.startRequest(SchemaDef.MY_BUY_COURSE_ONLINE_LIST, null);
			break;
		case 2:
			//线下
			ProtocalEngine engine2 = new ProtocalEngine(this);
			engine2.setObserver(this);
			engine2.startRequest(SchemaDef.MY_BUY_COURSE_OFFLINE_LIST, null);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.downCourse: // 线下课程
			isUpCourse =false ;
			startRequest(2);
			break;
		case R.id.upCourse:  //线上课程 
			isUpCourse =true ;
			startRequest(1);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 切换
	 */
	private void gotoChangeData() {
		if(isUpCourse){
			downCourse.setTextColor(Color.parseColor("#656565"));
			upCourse.setTextColor(Color.parseColor("#00A5FB"));
			adapter.notifyDataSetChanged();
		}else{
			downCourse.setTextColor(Color.parseColor("#00A5FB"));
			upCourse.setTextColor(Color.parseColor("#656565"));
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof MyBuyCourseOnlineResponseData){
			MyBuyCourseOnlineResponseData countreq=(MyBuyCourseOnlineResponseData)obj;
			if(countreq.commonData.result_code.equals("0") || countreq.commonData.result_code.equals("0000")){
				courses.clear();
				courses.addAll(countreq.courses);
				gotoChangeData();
			}else{
				if("登录Token无效".equals(countreq.commonData.result_msg)){
					Intent intent = new Intent(this,LoginActivity.class);
					startActivityForResult(intent, ByConstants.REQUEST_LOGIN);
				}else{
					Toast.makeText(this, countreq.commonData.result_msg,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		if(obj != null && obj instanceof MyBuyCourseOfflineResponseData){
			MyBuyCourseOfflineResponseData countreq=(MyBuyCourseOfflineResponseData)obj;
			if(countreq.commonData.result_code.equals("0") || countreq.commonData.result_code.equals("0000")){
				courses.clear();
				courses.addAll(countreq.courses);
				gotoChangeData();
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
	
	class MyUpAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return courses.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView ==null)
			{
				holder = new ViewHolder();
				convertView = View.inflate(MyBuyCourseActivity.this, R.layout.my_course_up_item, null);
				holder.up_wholeView = (LinearLayout) convertView.findViewById(R.id.wholeView);
				holder.up_courseType = (TextView) convertView.findViewById(R.id.courseType);
				holder.up_progress = (TextView) convertView.findViewById(R.id.progress);
				holder.up_bottomLine = (TextView) convertView.findViewById(R.id.bottomLine_up);
				convertView.setTag(holder);
			}else
			{
				holder = (ViewHolder)convertView.getTag();
			}
			if(position == courses.size()-1){
				holder.up_bottomLine.setVisibility(View.VISIBLE);
			}else{
				holder.up_bottomLine.setVisibility(View.GONE);
			}
			
			final Course info = courses.get(position);
			holder.up_courseType.setText(info.name);
			holder.up_progress.setText("共"+info.totalSchedule+"课时");
			
			holder.up_wholeView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = null;
					if(isUpCourse){
						intent = new Intent(MyBuyCourseActivity.this,MyBuyUpCourseActivity.class);
						intent.putExtra("courseId", info.courseId);
						MyBuyCourseActivity.this.startActivity(intent);
					}else{
						intent = new Intent(MyBuyCourseActivity.this,MyBuyDownCourseActivity.class);
						intent.putExtra("courseId", info.courseId);
						MyBuyCourseActivity.this.startActivityForResult(intent, 110005);
					}
				}
			});
			
			return convertView;
		}
		
	}
	class ViewHolder {
		LinearLayout up_wholeView ;
		TextView up_courseType ;
		TextView up_progress;
		TextView up_bottomLine;
		
		TextView down_courseType ;
		TextView down_time ;
		TextView down_teacher ;
		Button down_btn ;
		TextView down_wholeCourse ;
		TextView down_restCourse ;
		TextView down_bottonLine ;
		LinearLayout down_wholeView ;
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==110005 && resultCode ==110006){
			setResult(110006);
			finish();
		}
	}
}
