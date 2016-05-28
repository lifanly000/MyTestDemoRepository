package com.example.wireframe.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eblock.emama.R;
import com.example.wireframe.protocal.protocalProcess.model.ReportDetail;
import com.example.wireframe.ui.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PlayVideoAdapter extends BaseAdapter {
	
	private Context context ;
	private ArrayList<ReportDetail> details = new ArrayList<ReportDetail>();
	
	public void setDetails(ArrayList<ReportDetail> details) {
		this.details = details;
	}

	// 异步加载图片的显示参数
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisk(true)
			.showImageOnLoading(R.drawable.image_default)
			.showImageForEmptyUri(R.drawable.image_default)
			.showImageOnFail(R.drawable.image_default).build();
	
	public PlayVideoAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		return details.size();
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
		View view = View.inflate(context, R.layout.play_video_item, null);
		WebView mWebView = (WebView) view.findViewById(R.id.webView);
		TextView content = (TextView) view.findViewById(R.id.content);
		TextView title = (TextView) view.findViewById(R.id.title);
		ImageView image = (ImageView) view.findViewById(R.id.image);
		RelativeLayout videoRL = (RelativeLayout) view.findViewById(R.id.videoRL);
		ImageView imgBtn = (ImageView) view.findViewById(R.id.imgBtn);
		
		WebSettings websetting = mWebView.getSettings();
		websetting.setBuiltInZoomControls(true);
		websetting.setBlockNetworkImage(false);
		websetting.setJavaScriptEnabled(true);
		WebChromeClient webChromeClient = new WebChromeClient();
		WebViewClient webViewClient = new WebViewClient();
		mWebView.setWebChromeClient(webChromeClient);
		mWebView.setWebViewClient(webViewClient);
		mWebView.setInitialScale(1);
		
		
		ReportDetail info = details.get(position);
		content.setText(info.content);
		if(info.type.equals("1")){
			image.setVisibility(View.VISIBLE);
			videoRL.setVisibility(View.INVISIBLE);
			((BaseActivity)context).imageLoader.displayImage(info.imageUrl, image, options);
		}else if(info.type.equals("2")){
			image.setVisibility(View.INVISIBLE);
			videoRL.setVisibility(View.VISIBLE);
			title.setText(info.videoWord);
			mWebView.loadUrl(info.videoUrl);
		}
		return view;
	}
}
