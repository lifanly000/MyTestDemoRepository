package com.example.wireframe.ui.growup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.adapter.GrowUpGetNumHomeAdapter;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.utils.ShareUtil;
import com.example.wireframe.view.XListView;

public class GrowUpNumGetHomePageActivity extends BaseActivity implements OnClickListener,ProtocalEngineObserver {
	
	private TextView growupNum;
	private XListView xlistView;
	private GrowUpGetNumHomeAdapter adapter ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.growup_home_page_activity);
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
		
		growupNum = (TextView) findViewById(R.id.growupNum);
		xlistView = (XListView) findViewById(R.id.xlistView);
		xlistView.setPullLoadEnable(false);
		xlistView.setPullRefreshEnable(false);
		adapter = new GrowUpGetNumHomeAdapter(this);
		xlistView.setAdapter(adapter);
		
		startRequest();
		startProgress();
	}
	
	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		GrowUpNumberRequestData data = new GrowUpNumberRequestData();
		data.onlyAmount = "0";
		engine.startRequest(SchemaDef.GROW_UP_NUMBER, data);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.share:
//			ShareUtil.shareByYoumeng(this, "eMama,易贝乐家校互动平台，帮您分分钟了解孩子学习情况。", title.getText().toString(),videoPageUrl);
			break;

		default:
			break;
		}
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof GrowUpNumberResponseData){
			GrowUpNumberResponseData countreq=(GrowUpNumberResponseData)obj;
			if(countreq.commonData.result_code.equals("0") || countreq.commonData.result_code.equals("0000")){
				growupNum.setText(countreq.amount);
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
		
	}
}
