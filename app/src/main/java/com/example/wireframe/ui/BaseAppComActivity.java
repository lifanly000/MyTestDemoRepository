package com.example.wireframe.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.eblock.emama.Application;
import com.eblock.emama.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

public class BaseAppComActivity extends AppCompatActivity {
	
	protected Application application;
	protected SharedPreferences sp ;
	protected Editor editor ;
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
		if(editor ==null){
			editor = sp.edit();
		}
		if(isFirst){
			initImageLoader();
			if(!sp.contains("hasLogin")){
				editor.putBoolean("hasLogin", false);
				editor.putString("userName", "");
				editor.commit();
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
}
