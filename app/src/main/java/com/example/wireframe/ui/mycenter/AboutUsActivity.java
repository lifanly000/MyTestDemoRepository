package com.example.wireframe.ui.mycenter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.eblock.emama.R;
import com.example.wireframe.ui.BaseActivity;

public class AboutUsActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us_activity);
		
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
