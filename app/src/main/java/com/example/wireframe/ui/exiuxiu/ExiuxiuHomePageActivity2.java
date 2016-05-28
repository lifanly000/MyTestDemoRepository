package com.example.wireframe.ui.exiuxiu;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.adapter.EXiuXiuHomeAdapter;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.Banner;
import com.example.wireframe.protocal.protocalProcess.model.EXiuxiuHomePageResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ExiuxiuHomePageActivity2 extends BaseActivity implements
		ProtocalEngineObserver {

	private LinearLayout ll_container;
	private ViewPager viewPager;
	private TextView tv_desc;
	private String[] descriptions;
	private List<View> ivList;
	private int previousPosition = 0;
	private ListView listView;
	private EXiuXiuHomeAdapter adapter;

	private ArrayList<Banner> banners = new ArrayList<Banner>();
	

	// 异步加载图片的显示参数
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisk(true)
			.showImageOnLoading(R.drawable.image_default)
			.showImageForEmptyUri(R.drawable.image_default)
			.showImageOnFail(R.drawable.image_default).build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exiuxiu_home_activity);

		// init();
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		listView = (ListView) findViewById(R.id.listView);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		
		adapter = new EXiuXiuHomeAdapter(this);
		listView.setAdapter(adapter);

		startRequest();
		// timer.schedule(task, 2000,2000);
	}

	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		engine.startRequest(SchemaDef.E_XIUXIU_HOMEPAGE, null);
	}

	/**
	 * banner滚动
	 */
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			Message message = Message.obtain();
			message.what = 1;
			handler2.sendMessage(message);
		}
	};

	private Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int position = viewPager.getCurrentItem();
				viewPager.setCurrentItem(position + 1);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
	}

	private void init() {
		if (banners != null && banners.size() > 0) {
			// 准备数据
			prepareData();

			viewPager.setAdapter(new MyPagerAdapter());
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					int newPosition = position % ivList.size();
					tv_desc.setText(descriptions[newPosition]);
					// 改变点的状态
					// 将前一个被选中的点置为false
					ll_container.getChildAt(previousPosition).setEnabled(false);
					ll_container.getChildAt(newPosition).setEnabled(true);
					previousPosition = newPosition;
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int position) {

				}
			});

			// 初始状态
			tv_desc.setText(descriptions[0]);
			ll_container.getChildAt(0).setEnabled(true);

			// 设置当前viewPager 的初始position位置
			int position = Integer.MAX_VALUE / 2;
			position = position - ((Integer.MAX_VALUE / 2) % 5);
			viewPager.setCurrentItem(position);
		}
	}

	private void prepareData() {
		ivList = new ArrayList<View>();
		descriptions = new String[banners.size()];
		for (int i = 0; i < banners.size(); i++) {
			
			Banner banner = banners.get(i);

			descriptions[i] = banner.title;

			ImageView iv = new ImageView(this);
			imageLoader.displayImage(banner.image, iv, options);
			ivList.add(iv);
			View view = new View(this);
			view.setBackgroundResource(R.drawable.point_selector);
			LayoutParams params = new LayoutParams(5, 5);
			params.leftMargin = 10;
			view.setLayoutParams(params);
			view.setEnabled(false);
			ll_container.addView(view);
		}
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		// 销毁对象
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// 从ViewPager中销毁item对象
			int newPosition = position % ivList.size();
			viewPager.removeView(ivList.get(newPosition));
		}

		// 创建一个Item 对象
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int newPosition = position % ivList.size();
			viewPager.addView(ivList.get(newPosition));
			return ivList.get(newPosition);
		}

	}

	@Override
	public void OnProtocalFinished(Object obj) {
		if (obj != null && obj instanceof EXiuxiuHomePageResponseData) {
			EXiuxiuHomePageResponseData data = (EXiuxiuHomePageResponseData) obj;
			if (data.commonData.result_code.equals("0")
					|| data.commonData.result_code.equals("0000")) {
				banners.clear();
				banners.addAll(data.banners);
				adapter.setArticles(data.articles);
				init();
			} else {
				Toast.makeText(this, data.commonData.result_msg,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void OnProtocalError(int errorCode) {
		Toast.makeText(this, "请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnProtocalProcess(Object obj) {

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		startRequest();
	}

}
