package com.example.wireframe.ui.mycenter.mycourse;

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

import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineNotMemberUseResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineBuyInfoRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineBuyInfoResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.ui.mycenter.MyContinueCostActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyCourseUpDetailActivity extends BaseActivity implements OnClickListener,ProtocalEngineObserver{
	
	private TextView title,userName ,userType,time ,content1,content2;
	private ImageView userIcon,image1,image2 ;
	
	private WebView webview ;
	private RelativeLayout rlfortest;
	private TextView testTime ;
	
	private String courseId ="";
	
	
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
		
		rlfortest.setVisibility(View.GONE);
		startProgress();
		startRequest();
		
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
		MyCourseOnlineBuyInfoRequestData data = new MyCourseOnlineBuyInfoRequestData();
		data.courseId = courseId;
		engine.startRequest(SchemaDef.MY_COURSE_ONLINE_BUY_INFO, data);
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
	protected void onPause() {
		super.onPause();
		webview.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		webview.onResume();
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof MyCourseOnlineBuyInfoResponseData){
			MyCourseOnlineBuyInfoResponseData data=(MyCourseOnlineBuyInfoResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				
				title.setText(data.title);
				userName.setText(data.publisherName);
				userType.setText(data.publisherType);
				time.setText(data.publishTime);
				if(TextUtils.isEmpty(data.icon)){
					imageLoader.displayImage(data.icon,userIcon ,options);
				}
				if(!TextUtils.isEmpty(data.detail)){
					webview.loadUrl(data.detail);
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
}
