package com.example.wireframe.ui.exiuxiu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.adapter.EXiuXiuItemDetailAdapter;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.EXiuxiuHomePageDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.EXiuxiuHomePageDetailResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.view.XListView;

public class EXiuXiuItemActivity extends BaseActivity implements OnClickListener,ProtocalEngineObserver {
	
	private XListView xListView ;
	private EXiuXiuItemDetailAdapter adapter ;
	private String eShowId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exiuxiu_item_activity);
		
		eShowId = getIntent().getStringExtra("eShowId");
		
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		adapter = new EXiuXiuItemDetailAdapter(this);
		xListView.setAdapter(adapter);
		
		startRequestData();
	}
	
	private void startRequestData() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		EXiuxiuHomePageDetailRequestData data = new EXiuxiuHomePageDetailRequestData();
		data.eShowId = eShowId;
		engine.startRequest(SchemaDef.E_XIUXIU_HOMEPAGE_DETAIL, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back://返回
			finish();
			break;
		case R.id.share://分享
			break;

		default:
			break;
		}
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof EXiuxiuHomePageDetailResponseData){
			EXiuxiuHomePageDetailResponseData countreq=(EXiuxiuHomePageDetailResponseData)obj;
			if(countreq.commonData.result_code.equals("0") || countreq.commonData.result_code.equals("0000")){
				adapter.setData(countreq);
				adapter.notifyDataSetChanged();
			}else{
				if("登录Token无效".equals(countreq.commonData.result_msg)){
					Intent intent = new Intent(this,LoginActivity.class);
					startActivityForResult(intent, ByConstants.REQUEST_LOGIN);
				}else{
					Toast.makeText(this, countreq.commonData.result_msg,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	}

	@Override
	public void OnProtocalError(int errorCode) {
		endProgress();
		Toast.makeText(this, "请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void OnProtocalProcess(Object obj) {
		// TODO Auto-generated method stub
		
	}
}
