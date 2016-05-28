package com.example.wireframe.ui.exiuxiu;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.ab.view.listener.AbOnItemClickListener;
import com.ab.view.listener.AbOnScrollListener;
import com.ab.view.sliding.AbSlidingPlayView;
import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.adapter.EXiuXiuHomeAdapter;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.Banner;
import com.example.wireframe.protocal.protocalProcess.model.EXiuxiuHomePageResponseData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.VideoPlayActivity;
import com.example.wireframe.ui.growup.GrowUpNumGetHomePageActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ExiuxiuHomePageActivity extends BaseActivity implements
		ProtocalEngineObserver, OnClickListener {

	private LinearLayout ll_container;
	private TextView tv_desc;
	private ImageView titleIcon;
	private TextView growupNum;
	private ListView listView;
	private EXiuXiuHomeAdapter adapter;

	private AbSlidingPlayView mSlidingPlayView = null;
	private RelativeLayout rl_banner;

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
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		titleIcon = (ImageView) findViewById(R.id.titleIcon);
		growupNum = (TextView) findViewById(R.id.growupNum);

		growupNum.setOnClickListener(this);
		adapter = new EXiuXiuHomeAdapter(this);
		listView.setAdapter(adapter);

		rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
		mSlidingPlayView = (AbSlidingPlayView) findViewById(R.id.mAbSlidingPlayView);

		initBanner();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.growupNum:
			intent = new Intent(this, GrowUpNumGetHomePageActivity.class);
			startActivityForResult(intent, 10005);
			break;
		default:
			break;
		}
	}

	private void initBanner() {
		mSlidingPlayView.setPageLineHorizontalGravity(Gravity.LEFT);
		mSlidingPlayView.setOnPageScrolledListener(new AbOnScrollListener() {

			@Override
			public void onScrollToRight() {
			}

			@Override
			public void onScrollToLeft() {
			}

			@Override
			public void onScrollStoped() {
			}

			@Override
			public void onScroll(int position) {
				if (banners != null && banners.size() > position) {
					tv_desc.setText(banners.get(position).title);
				}
				mSlidingPlayView.stopPlay();
				mSlidingPlayView.startPlay();
			}
		});
		mSlidingPlayView.setOnItemClickListener(new AbOnItemClickListener() {

			@Override
			public void onClick(int position) {
			  Banner banner = banners.get(position);
              Intent intent = new Intent(ExiuxiuHomePageActivity.this,VideoPlayActivity.class);
              intent.putExtra("eShowId", banner.eShowId);
              ExiuxiuHomePageActivity.this.startActivity(intent);            
			}
		});
	}

	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		engine.startRequest(SchemaDef.E_XIUXIU_HOMEPAGE, null);
	}

	/**
	 * 成长值
	 */
	private void startGrowUpNumRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		GrowUpNumberRequestData data = new GrowUpNumberRequestData();
		data.onlyAmount = "1";
		engine.startRequest(SchemaDef.GROW_UP_NUMBER, data);
	}

	/**
	 * 填充banner数据
	 */
	private void prepareBannerData() {
		// TODO Auto-generated method stub
		mSlidingPlayView.setVisibility(View.INVISIBLE);
		mSlidingPlayView.removeAllViews();
		if (banners != null && banners.size() > 0) {
			mSlidingPlayView.setVisibility(View.VISIBLE);
			for (Banner item : banners) {
				ImageView img = new ImageView(this);
				img.setScaleType(ScaleType.FIT_XY);
				imageLoader.displayImage(item.image, img, options);
				mSlidingPlayView.addView(img);
			}
			mSlidingPlayView.stopPlay();
			mSlidingPlayView.startPlay();
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
				prepareBannerData();
				adapter.setArticles(data.articles);
			} else {
				if("登录Token无效".equals(data.commonData.result_msg)){
					Intent intent = new Intent(this,LoginActivity.class);
					startActivityForResult(intent, ByConstants.REQUEST_LOGIN);
				}else{
					Toast.makeText(this, data.commonData.result_msg,
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		if (obj != null && obj instanceof GrowUpNumberResponseData) {
			GrowUpNumberResponseData countreq = (GrowUpNumberResponseData) obj;
			if (countreq.commonData.result_code.equals("0")
					|| countreq.commonData.result_code.equals("0000")) {
				growupNum.setText("成长值 "+countreq.amount);
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
		startGrowUpNumRequest();
	}

	// 两秒内按返回键两次退出程序
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                application.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
