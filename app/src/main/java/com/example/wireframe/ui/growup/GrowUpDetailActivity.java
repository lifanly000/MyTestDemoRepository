package com.example.wireframe.ui.growup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.adapter.GrowUpDetailAdapter;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeDetailResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.utils.ShareUtil;
import com.example.wireframe.view.XListView;

public class GrowUpDetailActivity extends BaseActivity implements OnClickListener,ProtocalEngineObserver {
	
	private XListView xListView ;
	private GrowUpDetailAdapter adapter ;
	private String reportId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grow_up_detail_activity);
		
		reportId = getIntent().getStringExtra("reportId");
		
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		adapter = new GrowUpDetailAdapter(this);
		xListView.setAdapter(adapter);
		
		startRequestData();
	}
	
	private void startRequestData() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		GrowUpHomeDetailRequestData data = new GrowUpHomeDetailRequestData();
		data.reportId = reportId;
		engine.startRequest(SchemaDef.GROW_UP_HOME_DETAIL, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back://返回
			finish();
			break;
		case R.id.share://分享
//			ShareUtil.shareByYoumeng(this, "eMama,易贝乐家校互动平台，帮您分分钟了解孩子学习情况。", title.getText().toString(),videoPageUrl);
			break;

		default:
			break;
		}
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof GrowUpHomeDetailResponseData){
			GrowUpHomeDetailResponseData countreq=(GrowUpHomeDetailResponseData)obj;
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
