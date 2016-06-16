package com.example.wireframe.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeDetailResponseData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpResultDetailResponseData;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.utils.ShareUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class VideoGrowUpDetailActivity extends BaseActivity implements ProtocalEngineObserver, OnClickListener {

	private FrameLayout videoview;// 全屏时视频加载view
	private WebView videowebview;
	private View xCustomView;
	private xWebChromeClient xwebchromeclient;
	private String url = "http://app.eastshore.com.cn/gexu/a.html";
	private WebChromeClient.CustomViewCallback 	xCustomViewCallback;
    //计算点击的次数 
    private int count = 0; 
    //第一次点击的时间 long型 
    private long firClick,secClick; 
    //最后一次点击的时间 
    private long lastClick; 
    //第一次点击的button的id 
    private int firstId; 
    private onDoubleClick listClick = new onDoubleClick();
    private GestureDetector gestureScanner;
    
	private TextView title;
	private TextView titleHead;
	private TextView userName, userType, time;
	private ImageView userIcon;

	private LinearLayout ll_whole;
	private ListView listView;

	private String workId = "";
	
	private String reportType = "" ;

//	private PlayVideoAdapter adapter;

	// 异步加载图片的显示参数
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisk(true)
			.showImageOnLoading(R.drawable.image_default)
			.showImageForEmptyUri(R.drawable.image_default)
			.showImageOnFail(R.drawable.image_default).build();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉应用标题
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		
		setContentView(R.layout.grow_up_details_single_activity);
		
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		titleHead = (TextView) findViewById(R.id.titleHead);
		userName = (TextView) findViewById(R.id.userName);
		userType = (TextView) findViewById(R.id.userType);
		time = (TextView) findViewById(R.id.time);
		userIcon = (ImageView) findViewById(R.id.userIcon);
		ll_whole = (LinearLayout) findViewById(R.id.ll_whole);
		// listView = (ListView) findViewById(R.id.listView);
		if(getIntent().hasExtra("reportId"))
			workId = getIntent().getStringExtra("reportId");
		if(getIntent().hasExtra("reportType"))
			reportType = getIntent().getStringExtra("reportType");
		
//		if (reportType.equals("1")) {
//			title.setText("报告内容");
//		} else {
//			title.setText("活动内容");
//		}

		// adapter = new PlayVideoAdapter(this);
		// listView.setAdapter(adapter);
		findViewById(R.id.share).setOnClickListener(this);

		startProgress();
		startRequest();
		
		initwidget();
//		videowebview.loadUrl(url);
	}
	
	private void startRequest() {
//		if(reportType.equals("1")){
			ProtocalEngine engine = new ProtocalEngine(this);
			engine.setObserver(this);
			GrowUpHomeDetailRequestData data = new GrowUpHomeDetailRequestData();
			data.reportId = workId;
			data.reportType = reportType;
			engine.startRequest(SchemaDef.GROW_UP_HOME_DETAIL, data);
//		}else{
//			ProtocalEngine engine = new ProtocalEngine(this);
//			engine.setObserver(this);
//			GrowUpResultDetailRequestData data = new GrowUpResultDetailRequestData();
//			data.reportId = workId;
//			engine.startRequest(SchemaDef.GROW_UP_RESULT_DETAIL, data);
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.share:
			ShareUtil.shareByYoumeng(this, "eMama,易贝乐家校互动平台，帮您分分钟了解孩子学习情况。", title.getText().toString(),videoPageUrl);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		videowebview.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		videowebview.onResume();
	}

	private boolean isFirst = true;
	private String videoPageUrl = "" ;

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if (obj != null && obj instanceof GrowUpHomeDetailResponseData) {
			GrowUpHomeDetailResponseData data = (GrowUpHomeDetailResponseData) obj;
			if (data.commonData.result_code.equals("0")
					|| data.commonData.result_code.equals("0000")) {
				ll_whole.setVisibility(View.VISIBLE);

				title.setText(data.title);
				titleHead.setText(data.title);
				userName.setText(data.publisherName);
				userType.setText(data.publisherType);
				time.setText(data.publishTime);
				if (!TextUtils.isEmpty(data.icon)) {
					imageLoader.displayImage(data.icon, userIcon, options);
				}
				if (!TextUtils.isEmpty(data.cmtPage)) {
					webView2.setVisibility(View.VISIBLE);
					fillComments(data.cmtPage);
				}
				videoPageUrl = data.page;
				if(isFirst){
					isFirst = false ;
					if(!TextUtils.isEmpty(data.page)){
						//TODO
						videowebview.loadUrl(data.page);
//						videowebview.loadUrl(data.cmtPage);
					}
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
		
		if (obj != null && obj instanceof GrowUpResultDetailResponseData) {
			GrowUpResultDetailResponseData data = (GrowUpResultDetailResponseData) obj;
			if (data.commonData.result_code.equals("0")
					|| data.commonData.result_code.equals("0000")) {
				ll_whole.setVisibility(View.VISIBLE);
				
				title.setText(data.title);
				titleHead.setText(data.title);
				userName.setText(data.publisherName);
				userType.setText(data.publisherType);
				time.setText(data.publishTime);
				if (TextUtils.isEmpty(data.icon)) {
					imageLoader.displayImage(data.icon, userIcon, options);
				}
				videoPageUrl = data.page;
				if(isFirst){
					isFirst = false ;
					if(!TextUtils.isEmpty(data.page)){
						videowebview.loadUrl(data.page);
					}
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
	/**
	 * 填充评论页
	 * @param cmtPage
	 */
	private void fillComments(String cmtPage) {
		WebSettings ws = webView2.getSettings();
		/**
		 * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
		 * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
		 * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
		 * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
		 * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
		 * setSupportZoom 设置是否支持变焦
		 * */
		ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
		ws.setUseWideViewPort(true);// 可任意比例缩放
		ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
		ws.setSavePassword(true);
		ws.setSaveFormData(true);// 保存表单数据
		ws.setJavaScriptEnabled(true);
		ws.setGeolocationEnabled(true);// 启用地理定位
		ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
		ws.setDomStorageEnabled(true);
		xWebChromeClient xwebchromeclient2 = new xWebChromeClient();
		webView2.setWebChromeClient(xwebchromeclient2);
		webView2.setWebViewClient(new xWebViewClientent());		
		webView2.loadUrl(cmtPage);
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
	protected void onDestroy() {
		if(webView2 !=null){
			webView2.stopLoading();
			webView2 = null;
		}
		if(videowebview !=null){
			videowebview.stopLoading();
			videowebview = null;
		}
		super.onDestroy();
	}
	
	private WebView webView2;
	
	@SuppressWarnings("deprecation")
	private void initwidget() {
		// TODO Auto-generated method stub
		videoview = (FrameLayout) findViewById(R.id.video_view);
		videoview.setOnTouchListener(listClick);
		videowebview = (WebView) findViewById(R.id.webView);
		webView2 = (WebView) findViewById(R.id.webView2);
		WebSettings ws = videowebview.getSettings();
		/**
		 * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
		 * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
		 * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
		 * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
		 * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
		 * setSupportZoom 设置是否支持变焦
		 * */
		ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
		ws.setUseWideViewPort(true);// 可任意比例缩放
		ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
		ws.setSavePassword(true);
		ws.setSaveFormData(true);// 保存表单数据
		ws.setJavaScriptEnabled(true);
		ws.setGeolocationEnabled(true);// 启用地理定位
		ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
		ws.setDomStorageEnabled(true);
		xwebchromeclient = new xWebChromeClient();
		videowebview.setWebChromeClient(xwebchromeclient);
		videowebview.setWebViewClient(new xWebViewClientent());		
	}

	   @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	if (keyCode == KeyEvent.KEYCODE_BACK) {
	            if (inCustomView()) {
	            	hideCustomView();
	            	return true;
	            }else {
	            	//退出时加载空网址防止退出时还在播放视频
		   			 videowebview.loadUrl("about:blank");
//		       		 mTestWebView.loadData("", "text/html; charset=UTF-8", null);
		   			VideoGrowUpDetailActivity.this.finish();
		       		 Log.i("testwebview", "===>>>2");
					}
	    	}
	    	return true;
	    }
	   /**
	    * 判断是否是全屏
	    * @return
	    */
	    public boolean inCustomView() {
	 		return (xCustomView != null);
	 	}
	     /**
	      * 全屏时按返加键执行退出全屏方法
	      */
	     public void hideCustomView() {
	    	 xwebchromeclient.onHideCustomView();
	 	}
	/**
	 * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
	 * @author
	 */
	public class xWebChromeClient extends WebChromeClient {
		private Bitmap xdefaltvideo;
		private View xprogressvideo;
		@Override
    	//播放网络视频时全屏会被调用的方法
		public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
	        videowebview.setVisibility(View.GONE);
	        //如果一个视图已经存在，那么立刻终止并新建一个
	        if (xCustomView != null) {
	            callback.onCustomViewHidden();
	            return;
	        }
	        
	        videoview.addView(view);
	        xCustomView = view;
	        xCustomViewCallback = callback;
	        videoview.setVisibility(View.VISIBLE);
		}
		
		@Override
		//视频播放退出全屏会被调用的
		public void onHideCustomView() {
			
			if (xCustomView == null || videoview==null)//不是全屏播放状态
				return;	       
			
			// Hide the custom view.
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
			xCustomView.setVisibility(View.GONE);
			
			// Remove the custom view from its container.
			videoview.removeView(xCustomView);
			xCustomView = null;
			videoview.setVisibility(View.GONE);
			xCustomViewCallback.onCustomViewHidden();
			
			videowebview.setVisibility(View.VISIBLE);
			
	        //Log.i(LOGTAG, "set it to webVew");
		}
		//视频加载添加默认图标
		@Override
		public Bitmap getDefaultVideoPoster() {
			//Log.i(LOGTAG, "here in on getDefaultVideoPoster");	
			if (xdefaltvideo == null) {
				xdefaltvideo = BitmapFactory.decodeResource(
						getResources(), R.drawable.videoicon);
		    }
			return xdefaltvideo;
		}
		//视频加载时进程loading
		@Override
		public View getVideoLoadingProgressView() {
			//Log.i(LOGTAG, "here in on getVideoLoadingPregressView");
			
	        if (xprogressvideo == null) {
	            LayoutInflater inflater = LayoutInflater.from(VideoGrowUpDetailActivity.this);
	            xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
	        }
	        return xprogressvideo; 
		}
    	//网页标题
    	 @Override
         public void onReceivedTitle(WebView view, String title) {
            (VideoGrowUpDetailActivity.this).setTitle(title);
         }

//         @Override
//       //当WebView进度改变时更新窗口进度
//         public void onProgressChanged(WebView view, int newProgress) {
//        	 (MainActivity.this).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress*100);
//         }
         

	}

	/**
	 * 处理各种通知、请求等事件
	 * @author
	 */
	public class xWebViewClientent extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("webviewtest", "shouldOverrideUrlLoading: " + url);
			return false;
		}


		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			// 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
			// view.loadUrl(file:///android_asset/error.html );

			Log.i("onPageStarted", "错误");
			if(videowebview !=null)
				videowebview.setVisibility(View.GONE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.i("onPageStarted", "onPage               Started"+url);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			Log.i("onPageFinished", "onPageFinished>>>>>>"+url);
			if(videowebview !=null)
				videowebview.setVisibility(View.VISIBLE);
			super.onPageFinished(view, url);
		}
	}
	/**
	 * 当横竖屏切换时会调用该方法
	 * @author
	 */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	Log.i("testwebview", "=====<<<  onConfigurationChanged  >>>=====");
         super.onConfigurationChanged(newConfig);
         
         if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
             Log.i("webview", "   现在是横屏1");
            }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
             Log.i("webview", "   现在是竖屏1");
            }
    }
    class onDoubleClick implements View.OnTouchListener{  
        @Override  
        public boolean onTouch(View v, MotionEvent event) {  
            if(MotionEvent.ACTION_DOWN == event.getAction()){  
                count++;  
                if(count == 1){  
                    firClick = System.currentTimeMillis();  
                      
                } else if (count == 2){  
                    secClick = System.currentTimeMillis();  
                    if(secClick - firClick < 500){  
                        //双击事件  
                          Toast.makeText(VideoGrowUpDetailActivity.this, "双击了屏幕", Toast.LENGTH_LONG).show();
                    } 
                    else {

					}
                    count = 0;  
                    firClick = 0;  
                    secClick = 0;  
                      
                }  
            }  
            return true;  
        }         
          
    }


}
