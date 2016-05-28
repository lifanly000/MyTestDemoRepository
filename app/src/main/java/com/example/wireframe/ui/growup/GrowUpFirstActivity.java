package com.example.wireframe.ui.growup;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.R;
import com.eblock.emama.R.id;
import com.eblock.emama.R.layout;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberResponseData;
import com.example.wireframe.ui.BaseActivity;

public class GrowUpFirstActivity extends BaseActivity implements OnClickListener,ProtocalEngineObserver{

	private TextView  growupNum;
	private RelativeLayout ebaby;
	private RelativeLayout eblocks;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grow_up_first);
		
		growupNum = (TextView) findViewById(R.id.growupNum);
		ebaby = (RelativeLayout) findViewById(R.id.ebaby);
		eblocks = (RelativeLayout) findViewById(R.id.eblocks);
		
		ebaby.setOnClickListener(this);
		eblocks.setOnClickListener(this);
		growupNum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GrowUpFirstActivity.this,GrowUpNumGetHomePageActivity.class);
				startActivityForResult(intent, 10005);
			}
		});
		
		startProgress();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		startGrowUpNumRequest();
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
	public void OnProtocalFinished(Object obj) {
		endProgress();
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
		endProgress();
		Toast.makeText(this, "请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnProtocalProcess(Object obj) {
		
	}

	@Override
	public void onClick(View v) {
		Intent intent  = new Intent(this,GrowUpHomePageActivity.class);
		switch (v.getId()) {
		case R.id.ebaby:
			intent.putExtra("reportType", "2");
			break;
		case R.id.eblocks:
			intent.putExtra("reportType", "1");
			break;
			
		default:
			break;
		}
		startActivity(intent);
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
