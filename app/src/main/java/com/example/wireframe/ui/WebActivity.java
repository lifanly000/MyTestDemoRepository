package com.example.wireframe.ui;

import com.eblock.emama.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WebActivity extends Activity {

	private WebView webView;

	private String detailUrl = "";
	private Handler handler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win888_protocal_html2);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

		initTitle();
		init();
	}

	private void initTitle() {
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (webView != null) {
					webView.stopLoading();
					webView = null;
				}
				if(handler!=null){
					handler=null;
				}
				finish();
			}
		});
		TextView title = (TextView) findViewById(R.id.title);
		RelativeLayout rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		rl_title.setVisibility(View.VISIBLE);
		String type = getIntent().getStringExtra("type");
		// 打开浏览器
		if (type.equals("1")){
			// 课程简介
			if(getIntent().hasExtra("introduce"))
				detailUrl = getIntent().getStringExtra("introduce");
			title.setText("课程简介");
		} else if(type.equals("2")){
			//支付宝充值
			if(getIntent().hasExtra("payUrl"))
				detailUrl = getIntent().getStringExtra("payUrl");
			title.setText("支付宝充值");
		}
	}

	private void init() {
		webView = (WebView) findViewById(R.id.webview_wap);
		WebSettings websetting = webView.getSettings();
		websetting.setBuiltInZoomControls(true);
		websetting.setBlockNetworkImage(false);
		websetting.setJavaScriptEnabled(true);
		websetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// WebView加载web资源
		webView.loadUrl(detailUrl);
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
//		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
		startProgress();
		handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				webView.setVisibility(View.VISIBLE);
				handler = null;
			}

		}, 2000);
	}

//	final class DemoJavaScriptInterface {
//        DemoJavaScriptInterface() {
//        }
//        @JavascriptInterface
//        public void clickOnAndroid(String result) {
//        	if("0000".equals(result))
//        		WebActivity.this.finish();
//        }
//    }
	
	@Override
	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private ProgressDialog progressDialog = null;

	private void startProgress() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
		}

		progressDialog.show();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				endProgress();
			}

		}, 1000);
	}

	public void endProgress() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (webView != null) {
			webView.stopLoading();
			webView = null;
		}
		if(handler!=null){
			handler=null;
		}
		super.onDestroy();
	}
}
