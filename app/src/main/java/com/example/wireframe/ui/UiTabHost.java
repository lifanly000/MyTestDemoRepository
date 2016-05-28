package com.example.wireframe.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.eblock.emama.Application;
import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.UpdateRequestData;
import com.example.wireframe.protocal.protocalProcess.model.UpdateResponseData;
import com.example.wireframe.ui.exiuxiu.ExiuxiuHomePageActivity;
import com.example.wireframe.ui.growup.GrowUpFirstActivity;
import com.example.wireframe.ui.journal.JournalHomePageActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.ui.mycenter.UserCenterActivity;
import com.example.wireframe.view.TabHostActivity;
import com.example.wireframe.view.TabItem;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Javen
 * 
 */
public class UiTabHost extends TabHostActivity implements OnClickListener,
		OnTabChangeListener, ProtocalEngineObserver {

	private static String TAB_INDEX = "日志";
	private static String TAB_CAI = "成长";
	private static String TAB_DISCOVERY = "E-秀秀";
	private static String TAB_ME = "我";

	private Application myApplication;
	List<TabItem> mItems;
	private LayoutInflater mLayoutInflater;

	// private RespLogon respLogon;
	private Intent iIndex, iDiscovery, iMe, iCai;// iNews
	private String menuTag;// 不同tab对应不同的菜单 初始为list

	private SharedPreferences sp;

	/**
	 * 在初始化TabWidget前调用 和TabWidget有关的必须在这里初始化
	 */
	@Override
	protected void prepare() {
		myApplication = (Application) this.getApplication();
		// 自定义title
		iIndex = new Intent(this, JournalHomePageActivity.class);
		iCai = new Intent(this, GrowUpFirstActivity.class);
		iDiscovery = new Intent(this, ExiuxiuHomePageActivity.class);
		iMe = new Intent(this, UserCenterActivity.class);

		TabItem info = new TabItem(TAB_INDEX, R.drawable.tab_bar_index_d,
				iIndex);
		TabItem cai = new TabItem(TAB_CAI, R.drawable.tab_bar_index_cai, iCai);
		TabItem discovery = new TabItem(TAB_DISCOVERY,
				R.drawable.tab_bar_discovery_d, iDiscovery);
		TabItem me = new TabItem(TAB_ME, R.drawable.tab_bar_me_d, iMe);

		mItems = new ArrayList<TabItem>();
		mItems.add(info);
		mItems.add(cai);
		mItems.add(discovery);
		mItems.add(me);

		// 设置分割线
		TabWidget tabWidget = getTabWidget();
		tabWidget.setDividerDrawable(R.drawable.tab_gray);

		mLayoutInflater = getLayoutInflater();
		getTabHost().setOnTabChangedListener(this);

	}

	private PackageManager pm ; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化SP
		
		pm = getPackageManager();

		initImageLoader();
		downLoadDataBase();
//		if (!myApplication.isLogin) {
//			// TODO
//			Intent intent = new Intent(UiTabHost.this, LoginActivity.class);
//			intent.putExtra("isFirstTime", true);
//			startActivityForResult(intent, ByConstants.REQUEST_LOGIN);
//		} else {
			setCurrentTab(0);// 隐藏一个tab，修改 20140724
//		}

		// if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
		// return;

		getTabWidget().getChildAt(1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setCurrentTab(1);// 隐藏一个tab，修改 20140724
			}
		});
		getTabWidget().getChildAt(2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setCurrentTab(2);// 隐藏一个tab，修改 20140724
			}
		});
		// 隐藏一个tab，修改 20140724
		getTabWidget().getChildAt(3).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!myApplication.isLogin) {
					// TODO
					Intent intent = new Intent(UiTabHost.this,
							LoginActivity.class);
					startActivityForResult(intent, ByConstants.REQUEST_LOGIN);
				} else {
					setCurrentTab(3);// 隐藏一个tab，修改 20140724
				}
			}
		});
		menuTag = TAB_INDEX;
		MobclickAgent.updateOnlineConfig(this);
		doVersionUpdata();
	}

	public void doVersionUpdata() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		UpdateRequestData data = new UpdateRequestData();
		
		try {
			PackageInfo info = pm.getPackageInfo(
					"com.eblock.emama", 0);
			data.versionName =  info.versionName;
			data.versionCode =  String.valueOf(info.versionCode);
			data.packageName =  info.packageName;
			data.appName = info.applicationInfo.name;
			 engine.startRequest(SchemaDef.UPDATE_INFO, data);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 加载数据库
	 */
	private void downLoadDataBase() {
		String path = "city.sqlite";
		File file = new File(getFilesDir(), path);
		if (file.exists() && file.length() > 0) {
			return;
		}
		try {
			InputStream is = getAssets().open(path);
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			is.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** tab的title，icon，边距设定等等 */
	@Override
	protected void setTabItemTextView(TextView textView, int position) {
		textView.setPadding(3, 2, 3, 6);
		textView.setGravity(Gravity.CENTER);
		textView.setText(mItems.get(position).getTitle());
		// textView.setBackgroundResource(mItems.get(position).getBg());
		textView.setCompoundDrawablesWithIntrinsicBounds(0, mItems
				.get(position).getIcon(), 0, 0);
	}

	@Override
	protected void setTabImg(LinearLayout tvTabItem, int position) {
		tvTabItem.setBackgroundResource(mItems.get(position).getIcon());
	}

	/** tab唯一的id */
	@Override
	protected String getTabItemId(int position) {
		return mItems.get(position).getTitle(); // 我们使用title来作为id，你也可以自定
	}

	/** 点击tab时触发的事件 */
	@Override
	protected Intent getTabItemIntent(int position) {
		return mItems.get(position).getIntent();
	}

	@Override
	protected int getTabItemCount() {
		return mItems.size();
	}

	@Override
	public void onTabChanged(String tag) {
		menuTag = tag;

		if (TAB_INDEX.equals(tag)) {
			if (tabIndexHandler != null)
				tabIndexHandler
						.sendEmptyMessage(ByConstants.HANDLER_TAB_INDEX_TABINIT);
		} else if (TAB_CAI.equals(tag)) {
			if (tabCaiHandler != null)
				tabCaiHandler
						.sendEmptyMessage(ByConstants.HANDLER_TAB_CAI_TABINIT);
		} else if (TAB_ME.equals(tag)) {
			if (tabMe != null)
				tabMe.sendEmptyMessage(ByConstants.HANDLER_TAB_ME_TABINIT);

		} else if (TAB_DISCOVERY.equals(tag)) {
			if (tabUserHandler != null)
				tabUserHandler
						.sendEmptyMessage(ByConstants.HANDLER_TAB_USER_TABINIT);
		}
	}

	private static int HANDLER_UPDATE = 200;// update
	private Handler tabIndexHandler, tabUserHandler, tabProductHandler,
			tabExamHandler, tabCaiHandler, tabMe;// UiTsklist的Handler
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == ByConstants.HANDLER_TAB_INDEX) {
				tabIndexHandler = (Handler) msg.obj;
			} else if (msg.what == ByConstants.HANDLER_TAB_USER) {
				tabUserHandler = (Handler) msg.obj;
			} else if (msg.what == ByConstants.HANDLER_TAB_EXAM) {
				tabExamHandler = (Handler) msg.obj;
			} else if (msg.what == ByConstants.HANDLER_TAB_ME) {
				tabMe = (Handler) msg.obj;
			} else if (msg.what == ByConstants.HANDLER_TAB_PRODUCT) {
				tabProductHandler = (Handler) msg.obj;
			} else if (msg.what == ByConstants.HANDLER_TAB_CAI) {
				tabCaiHandler = (Handler) msg.obj;
			} else if (msg.what == HANDLER_UPDATE) {
				if (vesionData != null) {
					showV4UpdateDialog();
				}
			}
		}
	};

	/**
	 * 显示升级对话框
	 */
	private void showV4UpdateDialog() {

		final Dialog dialog = new Dialog(UiTabHost.this, R.style.MyDialog);
		View myView = View.inflate(this, R.layout.v4_update_dialog, null);
		dialog.setContentView(myView);
		TextView title = (TextView) myView.findViewById(R.id.dialogTitle);
		TextView desc = (TextView) myView.findViewById(R.id.dialogDesc);
		TextView dialogOk = (TextView) myView.findViewById(R.id.dialogOk);
		TextView dialogCancel = (TextView) myView
				.findViewById(R.id.dialogCancel);

		title.setText("有新版本下载 - " + vesionData.version);
		desc.setText(vesionData.downloadPrompt);
		dialogOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addCategory("android.intent.category.BROWSABLE");
				intent.setData(Uri.parse(vesionData.downloadUrl));
				startActivity(intent);
			}
		});

		dialogCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

	}

	// ---------------------------------------
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

	protected ImageLoader imageLoader = ImageLoader.getInstance();
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ByConstants.REQUEST_LOGIN) {
			if (myApplication.isLogin) {
				setCurrentTab(0);
			} else {
				finish();
			}
		}
	};

	// ------------------------------------------------------ onClick
	@Override
	public void onClick(View v) {

	}

	private UpdateResponseData vesionData = null;

	@Override
	public void OnProtocalFinished(Object obj) {
		if (obj != null && obj instanceof UpdateResponseData) {
			vesionData = (UpdateResponseData) obj;
			if (vesionData.commonData.result_code.equals("0")
					|| vesionData.commonData.result_code.equals("0000")) {
				if (!vesionData.version.equals("")) {
					// 异步启动升级提示对话框
					handler.sendEmptyMessage(HANDLER_UPDATE);
				}
			}
		}
	}

	@Override
	public void OnProtocalError(int errorCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnProtocalProcess(Object obj) {
		// TODO Auto-generated method stub

	}

}