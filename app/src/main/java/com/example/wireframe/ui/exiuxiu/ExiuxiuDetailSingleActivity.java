package com.example.wireframe.ui.exiuxiu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.Comment;
import com.example.wireframe.protocal.protocalProcess.model.EXiuxiuHomePageDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.EXiuxiuHomePageDetailResponseData;
import com.example.wireframe.protocal.protocalProcess.model.ZanAndCommentRequestData;
import com.example.wireframe.protocal.protocalProcess.model.ZanAndCommentResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.exiuxiu.ExiuxiuDetailSingleActivity;
import com.example.wireframe.utils.ShareUtil;
import com.example.wireframe.view.ListViewInScroll;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author tanwenwei
 * 
 */
public class ExiuxiuDetailSingleActivity extends BaseActivity implements ProtocalEngineObserver, OnClickListener{
	private FrameLayout videoview;// 全屏时视频加载view
//	private Button videolandport;
	private WebView videowebview;
	private Boolean islandport = true;//true表示此时是竖屏，false表示此时横屏。
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
    //网页加载出错进显示页面
//    private TextView videoerror;
    //网页加载出错正在刷新
//    private TextView videorefresh;
    private onDoubleClick listClick = new onDoubleClick();
    private GestureDetector gestureScanner;
    
    
    
    private TextView title,userName ,userType,time ;
	private ImageView userIcon;
	
	
	private LinearLayout ll_whole ;
//	private ListView listView;
	
	private String workId = "" ;
	
//	private PlayVideoAdapter adapter ;
	
	
//	private WebView webview ;
	
	private LinearLayout zan_comment_ll ;
	private LinearLayout zanLL ;
	private LinearLayout commentLL ;
	private ImageView zanIcon;
	private ImageView commentIcon;
	private TextView zanText;
	private TextView commentText;
	
	private ListViewInScroll lv_comment;
	private View in_bottom;
	private Button btn_send;
	private EditText et_msg;
	
	private ScrollView scrollView ;
	
	private ImageView imgBtn;
	private RelativeLayout videoRL;
	
	
	private String videoPageUrl = "";
	
	
	//异步加载图片的显示参数
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.showImageOnLoading(R.drawable.image_default)
				.showImageForEmptyUri(R.drawable.image_default)
				.showImageOnFail(R.drawable.image_default)
				.build();
	private MediaPlayer player = null;
	
	private MyCommentAdapter adapter ;
	ArrayList<Comment> comments = new ArrayList<Comment>();
	
	private void fillCommentData() {
		lv_comment.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();
	}
	
	
	class MyCommentAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return comments.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Comment info = comments.get(position);
			TextView tv = new TextView(ExiuxiuDetailSingleActivity.this);
			String htmlStr = "";
			if(TextUtils.isEmpty(info.to)){
				htmlStr = "<font size=1 color =#0681EB>"+info.by+"</font>"+":"+info.comment;
			}else{
				htmlStr = "<font size=1 color =#0681EB>"+info.by+"</font>"+"回复"+"<font size=1 color =#0681EB>"+info.to+"</font>"+":"+info.comment;
			}
			tv.setPadding(20, 3, 20, 3);
			tv.setText(Html.fromHtml(htmlStr));
			tv.setTextSize(12);
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					toSomeBody = info.by;
					showComment();
				}
			});
			return tv;
		}
		
	}
	
	private void startZanAndCommentRequest(String mode,String to,String content) {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		ZanAndCommentRequestData data = new ZanAndCommentRequestData();
		data.eShowId = workId;
		data.mode = mode;
		data.to = to;
		data.content = content;
		engine.startRequest(SchemaDef.ZAN_COMMENT, data);
	}
	

	private boolean hasZan =false;
	private boolean hasComment = false;
	private String toSomeBody = "";
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.zanLL:
			if(hasZan){
				Toast.makeText(this, "您已经点过赞了~", 0).show();
				return ;
			}
			hasZan = true;
			startZanAndCommentRequest("1","","");
			break;
		case R.id.commentLL:
			showComment();
			break;
		case R.id.btn_send:
			if(hasComment){
				return;
			}
			String contentStr = et_msg.getText().toString();
			if(TextUtils.isEmpty(contentStr)){
				Toast.makeText(this, "评论不能为空~", 0).show();
				return ;
			}
			startZanAndCommentRequest("2",toSomeBody,contentStr);
			hasComment = true;
			break;
		case R.id.imgBtn:
			intent = new Intent(this,ExiuxiuDetailSingleActivity.class);
			intent.putExtra("url", videoPageUrl);
			startActivity(intent);
			break;
		case R.id.share:
			ShareUtil.shareByYoumeng(this, "eMama,易贝乐家校互动平台，帮您分分钟了解孩子学习情况。", title.getText().toString(),videoPageUrl);
			break;
		default:
			break;
		}
	}
	/**
	 * 显示评论
	 */
	private void showComment(){
		in_bottom.setVisibility(View.VISIBLE);
		et_msg.setText("");
		et_msg.setFocusableInTouchMode(true);
		et_msg.requestFocus();
		InputMethodManager inputManager =(InputMethodManager)et_msg.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(et_msg, 0);
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

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉应用标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.exiuxiu_details_single_activity);
		initwidget();
		initListener();
//		url = getIntent().getStringExtra("url");
//		videowebview.loadUrl(url);
		
		
		
videoview = (FrameLayout) findViewById(R.id.video_view);
		
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		userName = (TextView) findViewById(R.id.userName);
		userType = (TextView) findViewById(R.id.userType);
		time = (TextView) findViewById(R.id.time);
		userIcon = (ImageView) findViewById(R.id.userIcon);
		ll_whole = (LinearLayout) findViewById(R.id.ll_whole);
		
		workId = getIntent().getStringExtra("eShowId");
		
		zan_comment_ll = (LinearLayout) findViewById(R.id.zan_comment_ll);
		zanLL = (LinearLayout) findViewById(R.id.zanLL);
		commentLL = (LinearLayout) findViewById(R.id.commentLL);
		zanIcon = (ImageView) findViewById(R.id.zanIcon);
		commentIcon = (ImageView) findViewById(R.id.commentIcon);
		zanText = (TextView) findViewById(R.id.zanText);
		commentText = (TextView) findViewById(R.id.commentText);
		
		lv_comment = (ListViewInScroll) findViewById(R.id.lv_comment);
		in_bottom = findViewById(R.id.in_bottom);
		btn_send = (Button) findViewById(R.id.btn_send);
		et_msg = (EditText) findViewById(R.id.et_msg);
		
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		
		imgBtn = (ImageView) findViewById(R.id.imgBtn);
		videoRL = (RelativeLayout) findViewById(R.id.videoRL);
		
		zanLL.setOnClickListener(this);
		commentLL.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		imgBtn.setOnClickListener(this);
		
		findViewById(R.id.share).setOnClickListener(this);
		
		initwidget();
		
		startProgress();
		startRequest();
		
		
		adapter = new MyCommentAdapter();
		lv_comment.setAdapter(adapter);
		
		scrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if(event.getAction() == MotionEvent.ACTION_MOVE)
				 {
					 hideCommentItem();
                 }
				return false;
			}
		});
		
	}
	
	/**
	 * 隐藏评论
	 */
	private void hideCommentItem(){
		View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        et_msg.setText("");
        in_bottom.setVisibility(View.GONE);
	}

	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		EXiuxiuHomePageDetailRequestData data = new EXiuxiuHomePageDetailRequestData();
		data.eShowId = workId;
		engine.startRequest(SchemaDef.E_XIUXIU_HOMEPAGE_DETAIL, data);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
//		videolandport.setOnClickListener(new Listener());
	}

	@SuppressWarnings("deprecation")
	private void initwidget() {
		// TODO Auto-generated method stub
		videoview = (FrameLayout) findViewById(R.id.video_view);
		videoview.setOnTouchListener(listClick);
//		videolandport = (Button) findViewById(R.id.video_landport);
		videowebview = (WebView) findViewById(R.id.webView);
//		videoerror = (TextView) findViewById(R.id.video_error);
//		videorefresh = (TextView) findViewById(R.id.video_refresh);
//		videoerror.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//	   			videowebview.loadUrl("about:blank");
//				videowebview.loadUrl(url);
//				videoerror.setVisibility(View.GONE);
//				videorefresh.setVisibility(View.VISIBLE);
//				videowebview.setVisibility(View.GONE);
//			}
//		});
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

	class Listener implements OnClickListener {

		@Override
		public void onClick(View v) {
		}
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
		   			ExiuxiuDetailSingleActivity.this.finish();
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
			if (islandport) {

			}
			else{
				
//				ii = "1";
//				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
			}
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
			
			if (xCustomView == null)//不是全屏播放状态
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
	            LayoutInflater inflater = LayoutInflater.from(ExiuxiuDetailSingleActivity.this);
	            xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
	        }
	        return xprogressvideo; 
		}
    	//网页标题
    	 @Override
         public void onReceivedTitle(WebView view, String title) {
            (ExiuxiuDetailSingleActivity.this).setTitle(title);
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
			videowebview.setVisibility(View.GONE);
//			videoerror.setVisibility(View.VISIBLE);
//			videorefresh.setVisibility(View.GONE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.i("onPageStarted", "onPage               Started"+url);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			Log.i("onPageFinished", "onPageFinished>>>>>>"+url);
			videowebview.setVisibility(View.VISIBLE);
//			videoerror.setVisibility(View.GONE);
//			videorefresh.setVisibility(View.GONE);
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
             islandport = false;
            }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
             Log.i("webview", "   现在是竖屏1");
             islandport = true;
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
                          Toast.makeText(ExiuxiuDetailSingleActivity.this, "双击了屏幕", Toast.LENGTH_LONG).show();
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
    private boolean isFirst = true ;
    
	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof EXiuxiuHomePageDetailResponseData){
			hideCommentItem();
			hasComment = false;
			EXiuxiuHomePageDetailResponseData data=(EXiuxiuHomePageDetailResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				ll_whole.setVisibility(View.VISIBLE);
				
				title.setText(data.title);
				userName.setText(data.publisherName);
				userType.setText(data.publisherType);
				time.setText(data.publishTime);
				if(TextUtils.isEmpty(data.icon)){
					imageLoader.displayImage(data.icon,userIcon ,options);
				}
				if(isFirst){
					isFirst = false ;
					if(!TextUtils.isEmpty(data.page)){
						videowebview.loadUrl(data.page);
					}
				}
				videoPageUrl = data.page;
				
				zanText.setText("赞 "+data.praiseCount);
				commentText.setText("评论 "+data.commentCount);
				comments.clear();
				comments.addAll(data.comments);
				fillCommentData();
			}
		}
		if(obj != null && obj instanceof ZanAndCommentResponseData){
			ZanAndCommentResponseData data=(ZanAndCommentResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
					startRequest();
			}
		}
	}

	@Override
	public void OnProtocalError(int errorCode) {
		endProgress();
		hasComment = false;
		hasZan = false ;
		Toast.makeText(this, "请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnProtocalProcess(Object obj) {
		// TODO Auto-generated method stub
		
	}

}
