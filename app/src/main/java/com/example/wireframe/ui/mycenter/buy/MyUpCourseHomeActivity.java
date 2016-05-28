package com.example.wireframe.ui.mycenter.buy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineNotMemberTryUseRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineNotMemberTryUseResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineNotMemberUseRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineNotMemberUseResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.ui.mycenter.MyContinueCostActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyUpCourseHomeActivity extends BaseActivity implements OnClickListener,ProtocalEngineObserver{
	
	private TextView title,userName ,userType,time ,content1,content2;
	private ImageView userIcon,image1,image2 ;
	
	private WebView webview ;
	private RelativeLayout rlfortest;
	private TextView testTime ;
	
	private String courseId ="";
	
	private boolean isFree = false;
	
	//异步加载图片的显示参数
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.showImageOnLoading(R.drawable.image_default)
				.showImageForEmptyUri(R.drawable.image_default)
				.showImageOnFail(R.drawable.image_default)
				.build();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_up_course_hone_activity);
		
		if(getIntent().hasExtra("courseId")){
			courseId = getIntent().getStringExtra("courseId");
		}
		if(getIntent().hasExtra("isFree")){
			isFree = getIntent().getBooleanExtra("isFree", false);
		}
		
		
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		userName = (TextView) findViewById(R.id.userName);
		userType = (TextView) findViewById(R.id.userType);
		time = (TextView) findViewById(R.id.time);
		content1 = (TextView) findViewById(R.id.content1);
		content2 = (TextView) findViewById(R.id.content2);
		userIcon = (ImageView) findViewById(R.id.userIcon);
		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		webview = (WebView) findViewById(R.id.webView);
		rlfortest = (RelativeLayout) findViewById(R.id.rlfortest);
		testTime = (TextView) findViewById(R.id.text001);
		
		findViewById(R.id.text002).setOnClickListener(this);
		
		if(isFree){
			rlfortest.setVisibility(View.GONE);
			startRequest();
		}else{
			rlfortest.setVisibility(View.VISIBLE);
			startRequestTest();
		}
		startProgress();
	}


    private WebChromeClient m_chromeClient = new WebChromeClient(){
    	 @Override
         public void onShowCustomView(View view, CustomViewCallback callback) {
                if (myCallback != null) {
                      myCallback.onCustomViewHidden();
                      myCallback = null ;
                      return;
               }
               
                long id = Thread.currentThread().getId();
//               WrtLog. v("WidgetChromeClient", "rong debug in showCustomView Ex: " + id);
               
               ViewGroup parent = (ViewGroup) webview.getParent();
               String s = parent.getClass().getName();
//               WrtLog. v("WidgetChromeClient", "rong debug Ex: " + s);
               parent.removeView( webview);
               parent.addView(view);
                myView = view;
                myCallback = callback;
                m_chromeClient = this ;
        }

         private View myView = null;
         private CustomViewCallback myCallback = null;
        
        
         public void onHideCustomView() {
               
                long id = Thread.currentThread().getId();
//               WrtLog. v("WidgetChromeClient", "rong debug in hideCustom Ex: " + id);
               
               
                if (myView != null) {
                     
                      if (myCallback != null) {
                             myCallback.onCustomViewHidden();
                             myCallback = null ;
                     }
                     
                     ViewGroup parent = (ViewGroup) myView.getParent();
                     parent.removeView( myView);
                     parent.addView( webview);
                     myView = null;
               }
         } 
    	};

	
	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		MyCourseOfflineNotMemberTryUseRequestData data = new MyCourseOfflineNotMemberTryUseRequestData();
		data.courseId = courseId;
		engine.startRequest(SchemaDef.MY_COURSE_OFFLINE_NOT_MEMBER_TYR_USE, data);
	}
	
	private void startRequestTest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		MyCourseOfflineNotMemberUseRequestData data = new MyCourseOfflineNotMemberUseRequestData();
		data.courseId = courseId;
		engine.startRequest(SchemaDef.MY_COURSE_OFFLINE_NOT_MEMBER_USE, data);
	}

	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.text002://购买完整版
			intent = new Intent(this,MyContinueCostActivity.class);
			intent.putExtra("type", "2");
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof MyCourseOfflineNotMemberTryUseResponseData){
			MyCourseOfflineNotMemberTryUseResponseData data=(MyCourseOfflineNotMemberTryUseResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				
				title.setText(data.title);
				userName.setText(data.publisherName);
				userType.setText(data.publisherType);
				time.setText(data.publishTime);
				testTime.setText("免费试用"+data.testSchedule+"课时");
				if(TextUtils.isEmpty(data.icon)){
					imageLoader.displayImage(data.icon,userIcon ,options);
				}
				if(!TextUtils.isEmpty(data.detail)){
					webview.loadUrl(data.detail);
				}
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
		if(obj != null && obj instanceof MyCourseOfflineNotMemberUseResponseData){
			MyCourseOfflineNotMemberUseResponseData data=(MyCourseOfflineNotMemberUseResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				Toast.makeText(this, "开始试用", 0).show();
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
}
