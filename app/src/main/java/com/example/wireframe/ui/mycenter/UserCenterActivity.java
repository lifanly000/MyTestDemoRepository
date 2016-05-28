package com.example.wireframe.ui.mycenter;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberResponseData;
import com.example.wireframe.protocal.protocalProcess.model.UpdateRequestData;
import com.example.wireframe.protocal.protocalProcess.model.UpdateResponseData;
import com.example.wireframe.protocal.protocalProcess.model.UserInfoResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.growup.GrowUpNumGetHomePageActivity;
import com.example.wireframe.ui.mycenter.buy.MyBuyCourseActivity;
import com.example.wireframe.ui.mycenter.mycourse.MyCourseActivity;
import com.example.wireframe.view.TabHostActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserCenterActivity extends BaseActivity implements OnClickListener, ProtocalEngineObserver {
	
	private TextView growupNum ,myGrowupNum ,version;
	private ImageView userIcon,titleIcon;
	private TextView userName ,userDetail1,userDetail2;
	private PackageManager pm;

	//异步加载图片的显示参数
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.showImageOnLoading(R.drawable.image_default)
				.showImageForEmptyUri(R.drawable.image_default)
				.showImageOnFail(R.drawable.image_default)
				.build();
	private PackageInfo packInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_center_activity);
		
		pm = getPackageManager();
		try {
			packInfo = pm.getPackageInfo(this.getPackageName(),0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		growupNum = (TextView) findViewById(R.id.growupNum);
		myGrowupNum = (TextView) findViewById(R.id.mygrowupNum);
		userName = (TextView) findViewById(R.id.userName);
		userDetail1 = (TextView) findViewById(R.id.userDetail1);
		userDetail2 = (TextView) findViewById(R.id.userDetail2);
		version = (TextView) findViewById(R.id.versionText);
		userIcon = (ImageView) findViewById(R.id.userIcon);
		titleIcon = (ImageView) findViewById(R.id.titleIcon);
		
		findViewById(R.id.item1).setOnClickListener(this);
		findViewById(R.id.item2).setOnClickListener(this);
		findViewById(R.id.item3).setOnClickListener(this);
		findViewById(R.id.item4).setOnClickListener(this);
		findViewById(R.id.item5).setOnClickListener(this);
		findViewById(R.id.item6).setOnClickListener(this);
		findViewById(R.id.item9).setOnClickListener(this);
		findViewById(R.id.growupNum).setOnClickListener(this);
		findViewById(R.id.changeIcon).setOnClickListener(this);
		
		if(packInfo!=null){
			version.setText("v"+packInfo.versionName);
		}else{
			version.setVisibility(View.INVISIBLE);
		}
		
		startProgress();
		doVersionUpdata();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null ;
		switch (v.getId()) {
		case R.id.growupNum:
		case R.id.item1: // 我的成长值
			intent = new Intent(this,GrowUpNumGetHomePageActivity.class);
			startActivityForResult(intent, 10005);
			break;
		case R.id.item2: // 我的课程
			intent = new Intent(this,MyCourseActivity.class);
			startActivity(intent);
			break;
		case R.id.item3: // 购买课程
			intent = new Intent(this,MyBuyCourseActivity.class);
			startActivityForResult(intent, 110005);
			break;
		case R.id.item4: // 用户反馈
			intent = new Intent(this,MyAdviceActivity.class);
			startActivity(intent);
			break;
		case R.id.item5: // 关于我们
			intent = new Intent(this,AboutUsActivity.class);
			startActivity(intent);
			break;
		case R.id.item6: // 检查更新
			updateFlag = false ;
			doVersionUpdata();
			break;
		case R.id.item9: // 排行榜
			intent = new Intent(this,RankListActivity.class);
			startActivity(intent);
			break;
		case R.id.changeIcon: // 用户设置
			intent = new Intent(this,MySetting.class);
			intent.putExtra("iconUrl", iconUrl);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==110005 && resultCode ==110006){
			Intent intent = new Intent(this,MyCourseActivity.class);
			intent.putExtra("type", "2");
			startActivity(intent);
		}
	}
	// 获取账户信息
	private  void startRequestUserBalanceandRed() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		engine.startRequest(SchemaDef.USER_INFO, null);
	}
	/**
	 * 成长值
	 */
	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		GrowUpNumberRequestData data = new GrowUpNumberRequestData();
		data.onlyAmount = "1";
		engine.startRequest(SchemaDef.GROW_UP_NUMBER, data);
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
			data.appName = getString(R.string.app_name);
			 engine.startRequest(SchemaDef.UPDATE_INFO, data);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private UpdateResponseData vesionData = null;
	private String versionStr = "";
	private String iconUrl = "";
	/**
	 * 进来请求更新的标志位
	 */
	private boolean updateFlag = true;
	
	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof UserInfoResponseData){
			UserInfoResponseData data=(UserInfoResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				userName.setText(data.name);
				userDetail1.setText(data.studentNumber);
				userDetail2.setText(data.school);
				if(!TextUtils.isEmpty(data.icon)){
					iconUrl = data.icon;
					imageLoader.displayImage(data.icon, userIcon, options);
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
		
		if(obj != null && obj instanceof GrowUpNumberResponseData){
			GrowUpNumberResponseData countreq=(GrowUpNumberResponseData)obj;
			if(countreq.commonData.result_code.equals("0") || countreq.commonData.result_code.equals("0000")){
				growupNum.setText("成长值 "+countreq.amount);
				myGrowupNum.setText(countreq.amount);
			}
		}
		if (obj != null && obj instanceof UpdateResponseData) {
			vesionData = (UpdateResponseData) obj;
			if (vesionData.commonData.result_code.equals("0")
					|| vesionData.commonData.result_code.equals("0000")) {
				versionStr = vesionData.version;
			}
			showUpdateDialog();
		}
	}

	@Override
	public void OnProtocalError(int errorCode) {
		
	}

	@Override
	public void OnProtocalProcess(Object obj) {
		// TODO Auto-generated method stub
		
	}
	
	private void showUpdateDialog(){
		if(TextUtils.isEmpty(versionStr)){
			//为空
			if(updateFlag){
				return ;
			}
			final Dialog dialog = new Dialog(this , R.style.MyDialog);
			View myView = View.inflate(this, R.layout.v4_update_dialog, null);
			 dialog .setContentView(myView);
			 TextView title = (TextView) myView.findViewById(R.id.dialogTitle);
			 TextView desc = (TextView) myView.findViewById(R.id.dialogDesc);
			 TextView dialogOk = (TextView) myView.findViewById(R.id.dialogOk);
			 TextView dialogCancel = (TextView) myView.findViewById(R.id.dialogCancel);
			 TextView dialogPadding = (TextView) myView.findViewById(R.id.dialogPadding);
			 
			 title.setText("当前为最新版本！");
			 dialogPadding.setVisibility(View.VISIBLE);
			 desc.setVisibility(View.GONE);
			 dialogCancel.setVisibility(View.GONE);
			 dialogOk.setText("确定");
			 dialogOk.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			 dialog .setCanceledOnTouchOutside(false);
			 dialog .show(); 
			 
		}else{
			showV4UpdateDialog();
		}
	}
	
	/**
	 * 显示升级对话框
	 */
	private void showV4UpdateDialog() {

		final Dialog dialog = new Dialog(this, R.style.MyDialog);
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

	@Override
	protected void onResume() {
		super.onResume();
		startRequestUserBalanceandRed();
		startRequest();
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
