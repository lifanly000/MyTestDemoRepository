package com.example.wireframe.ui.growup;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.adapter.PlayVideoAdapter;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeDetailResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class GrowUpDetailSingleActivity extends BaseActivity implements OnClickListener, ProtocalEngineObserver {
	
	private TextView title,userName ,userType,time ;
	private ImageView userIcon;
	
	
	private LinearLayout ll_whole ;
	private ListView listView;
	
	private String workId = "" ;
	
	private PlayVideoAdapter adapter ;
	
	
	private WebView webview ;
	
	
	//异步加载图片的显示参数
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.showImageOnLoading(R.drawable.user_center_user_icon)
				.showImageForEmptyUri(R.drawable.user_center_user_icon)
				.showImageOnFail(R.drawable.user_center_user_icon)
				.build();
	private MediaPlayer player = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
		setContentView(R.layout.grow_up_details_single_activity);
		
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		userName = (TextView) findViewById(R.id.userName);
		userType = (TextView) findViewById(R.id.userType);
		time = (TextView) findViewById(R.id.time);
		userIcon = (ImageView) findViewById(R.id.userIcon);
		ll_whole = (LinearLayout) findViewById(R.id.ll_whole);
//		listView = (ListView) findViewById(R.id.listView);
		
		
		
		workId = getIntent().getStringExtra("reportId");
		
//		adapter = new PlayVideoAdapter(this);
//		listView.setAdapter(adapter);
		
		webview = (WebView) findViewById(R.id.webView);
		
		 webview.getSettings().setJavaScriptEnabled(true);         
        webview.setWebChromeClient(m_chromeClient);
//        webview.loadUrl("www.baidu.com");
//        webview.loadUrl("http://app.eastshore.com.cn/gexu/a.html");
		
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
		GrowUpHomeDetailRequestData data = new GrowUpHomeDetailRequestData();
		data.reportId = workId;
		engine.startRequest(SchemaDef.GROW_UP_HOME_DETAIL, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
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
		if(obj != null && obj instanceof GrowUpHomeDetailResponseData){
			GrowUpHomeDetailResponseData data=(GrowUpHomeDetailResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				ll_whole.setVisibility(View.VISIBLE);
				
				title.setText(data.title);
				userName.setText(data.publisherName);
				userType.setText(data.publisherType);
				time.setText(data.publishTime);
				if(TextUtils.isEmpty(data.icon)){
					imageLoader.displayImage(data.icon,userIcon ,options);
				}
				if(!TextUtils.isEmpty(data.page)){
					webview.loadUrl(data.page);
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
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
	             finish();
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }
}
