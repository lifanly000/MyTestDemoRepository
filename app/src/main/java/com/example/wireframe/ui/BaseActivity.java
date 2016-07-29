package com.example.wireframe.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.eblock.emama.Application;
import com.eblock.emama.R;
import com.example.wireframe.view.TabHostActivity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

public class BaseActivity extends Activity {
	
	protected Application application;
	protected SharedPreferences sp ;
//	protected Editor editor ;
	protected int screen_width = 0; //屏幕宽度
	private static boolean isFirst = true ;
	
	// ImageLoader-------------------------------------/////
		public static final String CACHE_DIR_NAME = "/imageload";
		/** 本地缓存全路径名目录 */
		public static final String CACHE_DIR;
		/** 图片缓存目录 */
		public static final String CACHE_DIR_IMAGE;
		public static final String CACHE_DIR_FILE;

		static {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				// "/sdcard/itvk"
				CACHE_DIR = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + CACHE_DIR_NAME;
			} else {
				// "/itvk"
				CACHE_DIR = Environment.getRootDirectory().getAbsolutePath()
						+ CACHE_DIR_NAME;
			}
			// "/sdcard/itvk/pic/"
			CACHE_DIR_IMAGE = CACHE_DIR + "/pic";
			CACHE_DIR_FILE = CACHE_DIR + "/tmp";
		}

		public ImageLoader imageLoader = ImageLoader.getInstance();
		private DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).build();

		/**
		 * 初始化图片异步加载器
		 */
		private void initImageLoader() {
			// 图片异步加载的全局配置参数
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					this).denyCacheImageMultipleSizesInMemory()
					.memoryCache(new LruMemoryCache(5 * 1024 * 1024))
					.memoryCacheSize(5 * 1024 * 1024)
					.memoryCacheSizePercentage(13)
					// default
					.diskCache(new UnlimitedDiscCache(new File(CACHE_DIR_IMAGE)))
					.diskCacheSize(20 * 1024 * 1024).diskCacheFileCount(64)
					.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
					.build();

			// 初始化图片异步加载器
			ImageLoader.getInstance().init(config);

		}

		// ////////////////////////////////////////////////////////////////
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		application = (Application) this.getApplication();
		if(sp == null){
			sp = getSharedPreferences("config", MODE_PRIVATE);
		}
		if(isFirst){
			initImageLoader();
			if(!sp.contains("hasLogin")){
				sp.edit().putBoolean("hasLogin", false);
				sp.edit().putString("userName", "");
				sp.edit().commit();
			}else if(sp.getBoolean("hasLogin", false)){
				application.isLogin = true ;
				application.userName = sp.getString("userName", "");
			}else{
				application.isLogin = false ;
				application.userName  = "";
			}
			
			isFirst =false ;
		}
		
		Application.getInstance().addActivity(this);
	}
	

	/**
	 * 显示的字体大小不随手机字体大小而变化
	 */
	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		return res;
	}
//	/**
//	 * 禁止系统横竖屏
//	 * @param index 0-禁止；1-不禁止
//	 */
//	public void setHorizonEnable(int index){
//		ContentValues values = new ContentValues();
//		ContentResolver resolver = this.getContentResolver();
//		Uri uri = Uri.parse("content://settings/system");
//		String name="accelerometer_rotation";
//		int value = index;
//
//		values.put("name", name);
//		values.put("value", Integer.toString(value));
//		resolver.insert(uri, values);
//	}

	/**
	 * 获取手机屏幕的相关信息
	 */
	public  int getScreenInfoWidth() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.widthPixels;
	}

private ProgressDialog progressDialog = null;
    
    public void startProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("正在加载中...");
        progressDialog.show();
    }
    
    public void endProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 10005){
			if(resultCode ==102){
				TabHostActivity.mTabHost.setCurrentTab(1);
			}else if(resultCode ==103){
				TabHostActivity.mTabHost.setCurrentTab(2);
			}
		}
	}
    
//    /**
//	 * 分享应用程序
//	 */
//	protected void shareApplication() {
//		// Intent { act=android.intent.action.SEND typ=text/plain flg=0x3000000 cmp=com.android.mms/.ui.ComposeMessageActivity (has extras) } from pid 256
//		Intent intent =new Intent();
//		intent.setAction("android.intent.action.SEND");
//		intent.addCategory("android.intent.category.DEFAULT");
//		intent.setType("text/plain");
//		intent.putExtra(Intent.EXTRA_TEXT, "分享...");
//		startActivity(intent);
//	}
}
