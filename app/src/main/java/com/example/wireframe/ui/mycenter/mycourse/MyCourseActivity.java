package com.example.wireframe.ui.mycenter.mycourse;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
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
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.VideoMyCourseUpDetailActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.ui.mycenter.MyContinueCourseDetailActivity;
import com.example.wireframe.ui.mycenter.buy.MyUpCourseActivity;
import com.example.wireframe.view.XListView;

public class MyCourseActivity extends BaseActivity implements OnClickListener ,ProtocalEngineObserver{
	
	private TextView downCourse,upCourse;
	private XListView listView ;
	private boolean isUpCourse =false ;
	private BaseAdapter adapter ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_course);
		downCourse = (TextView) findViewById(R.id.downCourse);
		upCourse = (TextView) findViewById(R.id.upCourse);
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.downCourse).setOnClickListener(this);
		findViewById(R.id.upCourse).setOnClickListener(this);
		
		listView = (XListView) findViewById(R.id.listView);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.setDividerHeight(0);
		
		if(getIntent().hasExtra("type")&&getIntent().getStringExtra("type").equals("2")){
			//支付成功
			Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
			Application.payFlag=100;
		}
		
//		isUpCourse =false ;
		adapter = new MyUpAdapter();
		listView.setAdapter(adapter);
//		if(isUpCourse){
//			startRequest(1);
//		}else{
//			startRequest(2);
//		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isUpCourse =false ;
		if(isUpCourse){
			startRequest(1);
		}else{
			startRequest(2);
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
			startProgress();
			startRequest(2);
			break;
		case R.id.upCourse:  //线上课程 
			isUpCourse =true ;
			startProgress();
			startRequest(1);
			break;

		default:
			break;
		}
	}
	private void startRequest(int index) {
		switch (index) {
		case 1://线上请求
			ProtocalEngine engine = new ProtocalEngine(this);
			engine.setObserver(this);
			engine.startRequest(SchemaDef.MY_COURSE_ONLINE, null);
			break;
		case 2://线下请求
			ProtocalEngine engine2 = new ProtocalEngine(this);
			engine2.setObserver(this);
			engine2.startRequest(SchemaDef.MY_COURSE_OFFLINE, null);
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
			adapter = new MyUpAdapter();
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}else{
			downCourse.setTextColor(Color.parseColor("#00A5FB"));
			upCourse.setTextColor(Color.parseColor("#656565"));
			adapter = new MyDownAdapter();
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	private MyCourseOnlineResponseData onLineData = null;
	private MyCourseOfflineResponseData offLineData = null;
	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof MyCourseOnlineResponseData){
			MyCourseOnlineResponseData data=(MyCourseOnlineResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				onLineData = data;
				gotoChangeData();
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
		if(obj != null && obj instanceof MyCourseOfflineResponseData){
			MyCourseOfflineResponseData data=(MyCourseOfflineResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				offLineData = data;
				gotoChangeData();
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
			if(onLineData !=null && onLineData.courses !=null  && onLineData.courses.size()>0){
				return onLineData.courses.size();
			}
			return 0;
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
				convertView = View.inflate(MyCourseActivity.this, R.layout.my_course_up_item, null);
				holder.up_wholeView = (LinearLayout) convertView.findViewById(R.id.wholeView);
				holder.up_courseType = (TextView) convertView.findViewById(R.id.courseType);
				holder.up_progress = (TextView) convertView.findViewById(R.id.progress);
				holder.up_bottomLine = (TextView) convertView.findViewById(R.id.bottomLine_up);
				convertView.setTag(holder);
			}else
			{
				holder = (ViewHolder)convertView.getTag();
			}
			if(position == onLineData.courses.size()-1){
				holder.up_bottomLine.setVisibility(View.VISIBLE);
			}else{
				holder.up_bottomLine.setVisibility(View.GONE);
			}
			final Course course = onLineData.courses.get(position);
			holder.up_courseType.setText(course.name);
			holder.up_progress.setText(course.schedule+"%");
			
			holder.up_wholeView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MyCourseActivity.this,VideoMyCourseUpDetailActivity.class);
					intent.putExtra("courseId", course.courseId);
					MyCourseActivity.this.startActivity(intent);
				}
			});
			return convertView;
		}
		
	}
	class MyDownAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			if(offLineData !=null && offLineData.courses !=null  && offLineData.courses.size()>0){
				return offLineData.courses.size();
			}
			return 0;
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
				convertView = View.inflate(MyCourseActivity.this, R.layout.my_course_dowm_item, null);
				holder.down_wholeView = (LinearLayout) convertView.findViewById(R.id.wholeView);
				holder.down_courseType = (TextView) convertView.findViewById(R.id.text1);
				holder.down_time = (TextView) convertView.findViewById(R.id.text2);
				holder.down_teacher = (TextView) convertView.findViewById(R.id.text3);
				holder.down_btn = (Button) convertView.findViewById(R.id.btn);
				holder.down_wholeCourse = (TextView) convertView.findViewById(R.id.text5);
				holder.down_restCourse = (TextView) convertView.findViewById(R.id.text6);
				holder.down_bottonLine = (TextView) convertView.findViewById(R.id.bottomLine);
				holder.quanBg = (LinearLayout) convertView.findViewById(R.id.quanBg);
				convertView.setTag(holder);
			}else
			{
				holder = (ViewHolder)convertView.getTag();
			}
			
			if(position == offLineData.courses.size()-1){
				holder.down_bottonLine.setVisibility(View.VISIBLE);
			}else{
				holder.down_bottonLine.setVisibility(View.GONE);
			}
			final Course course = offLineData.courses.get(position);
			
			holder.down_courseType.setText(course.name);
			holder.down_time.setText(course.time+" 购买");
			holder.down_teacher.setText("主讲老师："+course.teacher);
			String totalStr ="共"+"<font size=1 color =#00A5FB>"+course.totalSchedule+"</font>"+"课时";
			String remianStr ="剩"+"<font size=1 color =#00A5FB>"+course.remainSchedule+"</font>"+"课时";
			holder.down_wholeCourse.setText(Html.fromHtml(totalStr));
			holder.down_restCourse.setText(Html.fromHtml(remianStr));
			
			setQuanBackground(course.totalSchedule,course.remainSchedule,holder.quanBg);
			initBtn(holder.down_btn,position,course);
			
//			holder.down_wholeView.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(MyCourseActivity.this,MyDownCourseActivity.class);
//					intent.putExtra("courseId", course.courseId);
//					MyCourseActivity.this.startActivity(intent);
//				}
//			});
			return convertView;
		}

		private void initBtn(Button down_btn, final int position, final Course course) {
			down_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(course.append.equals("1")){
						Intent intent = new Intent(MyCourseActivity.this,MyContinueCourseDetailActivity.class);
						intent.putExtra("courseId", course.courseId);
						MyCourseActivity.this.startActivityForResult(intent,110005);
					}else{
						Toast.makeText(MyCourseActivity.this, "该课程暂时不能续费", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}
	
	private void setQuanBackground(String totalSchedule,
			String remainSchedule, LinearLayout quanBg) {
		try {
			double total = Double.parseDouble(totalSchedule);
			double remain = Double.parseDouble(remainSchedule);
			if(total ==0 || remain == 0){
				quanBg.setBackgroundResource(R.drawable.quan11);
			}else{
				double ratio = remain/total;
				if(ratio <0.05){
					quanBg.setBackgroundResource(R.drawable.quan1);
				}else if(ratio <0.15){
					quanBg.setBackgroundResource(R.drawable.quan2);
				}else if(ratio <0.25){
					quanBg.setBackgroundResource(R.drawable.quan3);
				}else if(ratio <0.35){
					quanBg.setBackgroundResource(R.drawable.quan4);
				}else if(ratio <0.45){
					quanBg.setBackgroundResource(R.drawable.quan5);
				}else if(ratio <0.55){
					quanBg.setBackgroundResource(R.drawable.quan6);
				}else if(ratio <0.65){
					quanBg.setBackgroundResource(R.drawable.quan7);
				}else if(ratio <0.75){
					quanBg.setBackgroundResource(R.drawable.quan8);
				}else if(ratio <0.85){
					quanBg.setBackgroundResource(R.drawable.quan9);
				}else if(ratio <0.95){
					quanBg.setBackgroundResource(R.drawable.quan10);
				}else{
					quanBg.setBackgroundResource(R.drawable.quan11);
				}
			}
			
		} catch (NumberFormatException e) {
			quanBg.setBackgroundResource(R.drawable.quan11);
			e.printStackTrace();
		}
	}
	
	class ViewHolder {
		LinearLayout up_wholeView ;
		LinearLayout quanBg ;
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
			Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
			Application.payFlag=100;
		}
	}
}
