package com.example.wireframe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.eblock.emama.R;

public class FirstEnterInActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_enter_in_activity);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(FirstEnterInActivity.this,UiTabHost.class);
				startActivity(intent);
				finish();
			}

		}, 1000);
	}
}
