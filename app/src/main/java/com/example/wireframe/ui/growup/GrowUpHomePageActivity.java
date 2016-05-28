package com.example.wireframe.ui.growup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.adapter.GrowUpHomeAdapter;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeResponseData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.view.XListView;

public class GrowUpHomePageActivity extends BaseActivity implements
		OnClickListener, ProtocalEngineObserver {

	private TextView growupNum;
	private XListView xListView;
	private GrowUpHomeAdapter adapter;
	private String reportType = "";
	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.growup_home_activity);

		if (getIntent().hasExtra("reportType"))
			reportType = getIntent().getStringExtra("reportType");

		title = (TextView) findViewById(R.id.title);
		growupNum = (TextView) findViewById(R.id.growupNum);
		xListView = (XListView) findViewById(R.id.listView);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		adapter = new GrowUpHomeAdapter(this, reportType);
		xListView.setAdapter(adapter);

		if (reportType.equals("1")) {
			title.setText("成长报告");
		} else {
			title.setText("eBaby活动");
		}

		findViewById(R.id.back).setOnClickListener(this);
		growupNum.setOnClickListener(this);

		startProgress();

	}

	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		GrowUpHomeRequestData data = new GrowUpHomeRequestData();
		data.reportType = reportType;
		engine.startRequest(SchemaDef.GROW_UP_HOME, data);
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

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.growupNum: // 成长值
			intent = new Intent(this, GrowUpNumGetHomePageActivity.class);
			startActivityForResult(intent, 10005);
			break;
		case R.id.back:
			finish();
		default:
			break;
		}
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if (obj != null && obj instanceof GrowUpHomeResponseData) {
			GrowUpHomeResponseData countreq = (GrowUpHomeResponseData) obj;
			if (countreq.commonData.result_code.equals("0")
					|| countreq.commonData.result_code.equals("0000")) {
				adapter.setData(countreq);
				adapter.notifyDataSetChanged();
			} else {
				if ("登录Token无效".equals(countreq.commonData.result_msg)) {
					Intent intent = new Intent(this, LoginActivity.class);
					startActivityForResult(intent, ByConstants.REQUEST_LOGIN);
				} else {
					Toast.makeText(this, countreq.commonData.result_msg,
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		if (obj != null && obj instanceof GrowUpNumberResponseData) {
			GrowUpNumberResponseData countreq = (GrowUpNumberResponseData) obj;
			if (countreq.commonData.result_code.equals("0")
					|| countreq.commonData.result_code.equals("0000")) {
				growupNum.setText("成长值 " + countreq.amount);
			}
		}
	}

	@Override
	public void OnProtocalError(int errorCode) {
		endProgress();
//		Toast.makeText(this, "请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
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

}
