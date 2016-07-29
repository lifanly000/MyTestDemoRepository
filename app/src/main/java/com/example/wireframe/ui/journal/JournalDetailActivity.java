package com.example.wireframe.ui.journal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.ProtocalParser;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeDetailResponseData;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeWorkResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class JournalDetailActivity extends BaseActivity implements
		OnClickListener, ProtocalEngineObserver {

	private TextView title, userName, userType, time;
	private ImageView userIcon;
	private TextView todayHomeWork, homeworkcontent, myHomeWork;
	private RelativeLayout homeWorkImage1;
	private RelativeLayout homeWorkImage2;
	private Button doHomeWorkBtn;// 长按录音图标
	private TextView sendMyHomework;
	private ImageView voiceIcon;
	private TextView voiceText;
	private TextView voiceTime;
	private TextView voiceTimeRight;
	private TextView voiceTime1;
	private TextView voiceTimeRight1;

	private String homeworkurl = "";

	private LinearLayout ll_whole;
	private LinearLayout LL_mySendHomeWork;
	private WebView webview;

	private String workId = "";

	// 音频文件  
	private File soundFile;
	private MediaRecorder mediaRecorder;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	// 异步加载图片的显示参数
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisk(true)
			.showImageOnLoading(R.drawable.image_default)
			.showImageForEmptyUri(R.drawable.image_default)
			.showImageOnFail(R.drawable.image_default).build();
	private MediaPlayer player = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.jorunal_item_activity);

		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		userName = (TextView) findViewById(R.id.userName);
		userType = (TextView) findViewById(R.id.userType);
		time = (TextView) findViewById(R.id.time);
		userIcon = (ImageView) findViewById(R.id.userIcon);
		ll_whole = (LinearLayout) findViewById(R.id.ll_whole);
		LL_mySendHomeWork = (LinearLayout) findViewById(R.id.LL_mySendHomeWork);
		webview = (WebView) findViewById(R.id.webView);

		todayHomeWork = (TextView) findViewById(R.id.todayHomeWork);
		homeworkcontent = (TextView) findViewById(R.id.homeworkcontent);
		myHomeWork = (TextView) findViewById(R.id.myHomeWork);
		homeWorkImage1 = (RelativeLayout) findViewById(R.id.homeWorkImage1);
		homeWorkImage2 = (RelativeLayout) findViewById(R.id.homeWorkImage2);
		doHomeWorkBtn = (Button) findViewById(R.id.doHomeWorkBtn);
		sendMyHomework = (TextView) findViewById(R.id.sendMyHomework);
		voiceIcon = (ImageView) findViewById(R.id.voiceIcon);
		voiceText = (TextView) findViewById(R.id.voiceText);
		voiceTime = (TextView) findViewById(R.id.voiceTime);
		voiceTimeRight = (TextView) findViewById(R.id.voiceTimeRight);
		voiceTime1 = (TextView) findViewById(R.id.voiceTime1);
		voiceTimeRight1 = (TextView) findViewById(R.id.voiceTimeRight1);

		homeWorkImage1.setOnClickListener(this);
		homeWorkImage2.setOnClickListener(this);
		sendMyHomework.setOnClickListener(this);
		
		workId = getIntent().getStringExtra("workId");
		
		if(sp.contains("workId"+workId)  && sp.getBoolean("workId"+workId, false)){
			myHomeWork.setVisibility(View.VISIBLE);
			LL_mySendHomeWork.setVisibility(View.VISIBLE);
			myHomeWork.setEnabled(true);
			soundFile = new File(sp.getString("workId"+workId+"sound", ""));
		}else{
			myHomeWork.setVisibility(View.INVISIBLE);
			LL_mySendHomeWork.setVisibility(View.INVISIBLE);
		}

		listener();

		startProgress();
		startRequest();

		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebChromeClient(m_chromeClient);
	}

	private WebChromeClient m_chromeClient = new WebChromeClient() {
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			if (myCallback != null) {
				myCallback.onCustomViewHidden();
				myCallback = null;
				return;
			}

			long id = Thread.currentThread().getId();
			// WrtLog. v("WidgetChromeClient",
			// "rong debug in showCustomView Ex: " + id);

			ViewGroup parent = (ViewGroup) webview.getParent();
			String s = parent.getClass().getName();
			// WrtLog. v("WidgetChromeClient", "rong debug Ex: " + s);
			parent.removeView(webview);
			parent.addView(view);
			myView = view;
			myCallback = callback;
			m_chromeClient = this;
		}

		private View myView = null;
		private CustomViewCallback myCallback = null;

		public void onHideCustomView() {

			long id = Thread.currentThread().getId();
			// WrtLog. v("WidgetChromeClient", "rong debug in hideCustom Ex: " +
			// id);

			if (myView != null) {

				if (myCallback != null) {
					myCallback.onCustomViewHidden();
					myCallback = null;
				}

				ViewGroup parent = (ViewGroup) myView.getParent();
				parent.removeView(myView);
				parent.addView(webview);
				myView = null;
			}
		}
	};

	private void listener() {
		// 开始录音
		doHomeWorkBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					changeBtnBackground(true);
					recordingVoice();
					break;
				case MotionEvent.ACTION_UP:
					changeBtnBackground(false);
					if (soundFile != null && soundFile.exists()) {
						mediaRecorder.stop(); // **停止录音**
						mediaRecorder.release(); // **释放资源**
						mediaRecorder = null;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	
	
	private void recordingVoice(){
		try {
			Log.d("lb", Environment.getExternalStorageDirectory()
					.getCanonicalFile().toString());
			 String s = new SimpleDateFormat("yyyyMMdd_hhmmss")
			 .format(new Date(System.currentTimeMillis()));
			// 直接存储到了sdcard中
			soundFile = new File(Environment
					.getExternalStorageDirectory().getCanonicalFile()
					+ "/" + s + ".amr");
			mediaRecorder = new MediaRecorder();
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录制的声音的来源
			mediaRecorder.setAudioSamplingRate(32);
			mediaRecorder.setAudioEncodingBitRate(32);
			// recorder.setVideoSource(video_source); //录制视频
			// 录制的声音的输出格式（必须在设置声音的编码格式之前设置）
			mediaRecorder
					.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			// 设置声音的编码格式
			mediaRecorder
					.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			// 设置声音的保存位置
			mediaRecorder.setOutputFile(soundFile.getAbsolutePath());
			mediaRecorder.prepare(); // **准备录音**
			mediaRecorder.start(); // **开始录音**
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean hasSended = false ;
	
	private void changeBtnBackground(boolean b) {
		if (b) {
//			doHomeWorkBtn
//					.setBackgroundResource(R.drawable.v4_gradient_box_whole_blue);
			
			myHomeWork.setVisibility(View.INVISIBLE);
			LL_mySendHomeWork.setVisibility(View.VISIBLE);
			
			voiceIcon.setBackgroundResource(R.drawable.learn_voice_2);
			voiceText.setTextColor(Color.WHITE);
			voiceTime.setBackgroundDrawable(null);
			voiceTime.setVisibility(View.VISIBLE);
			voiceTimeRight.setVisibility(View.INVISIBLE);
			homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_blank);
			sendMyHomework.setVisibility(View.VISIBLE);
			sendMyHomework.setEnabled(false);
			homeWorkImage2.setEnabled(false);
			sendMyHomework.setBackgroundColor(Color.parseColor("#E8E8E8"));
			messageWhat = 1;
			hasSended =true;
			initTimer();
		} else {
//			doHomeWorkBtn
//					.setBackgroundResource(R.drawable.v4_gradient_box_blue);
			voiceIcon.setBackgroundResource(R.drawable.learn_voice_1);
			voiceText.setTextColor(Color.parseColor("#aa00a5fb"));
			homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_red);
			voiceTime.setVisibility(View.INVISIBLE);
			voiceTimeRight.setVisibility(View.VISIBLE);
			voiceTimeRight.setText(String.valueOf(voiceRecordTime)+"s");
			voiceText.setText("重新录入...");
			sendMyHomework.setEnabled(true);
			homeWorkImage2.setEnabled(true);
			sendMyHomework.setBackgroundResource(R.drawable.blue_btn);
			closeTimer();
		}
	}

	private void closeTimer(){
		if(timer !=null){
			timer.cancel();
			timer =null;
		}
		if(task !=null){
			task.cancel();
			task =null;
		}
	}
	/**
	 *刷新页面
	 */
	private long voiceRecordTime = 0;
	Timer timer = null;
	TimerTask task = null;
	private int messageWhat = 1;
	 
	 private void initTimer(){
		 voiceRecordTime = 0;
		 if(timer ==null){
			 timer = new Timer();
		 }
		 if(task ==null){
			 task = new TimerTask() {  
				  
			        @Override  
			        public void run() {  
			            Message message = Message.obtain();  
			            message.what = messageWhat;  
			            voiceRecordTime++;
			            handler.sendMessage(message);  
			        }  
			 }; 
			 timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行 
		 }
	 }
	 
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				voiceTime.setVisibility(View.VISIBLE);
				voiceTime.setText(String.valueOf(voiceRecordTime)+"s");
				break;
			case 2:
				voiceTimeRight.setVisibility(View.VISIBLE);
				voiceTimeRight.setText(String.valueOf(voiceRecordTime)+"s");
				break;
			case 3:
				voiceTimeRight1.setVisibility(View.VISIBLE);
				voiceTimeRight1.setText(String.valueOf(voiceRecordTime)+"s");
				break;
			default:
				break;
			}
		};
	};
	
	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		JournalHomeDetailRequestData data = new JournalHomeDetailRequestData();
		data.workId = workId;
		engine.startRequest(SchemaDef.JOURNAL_HOME_DETAIL, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.homeWorkImage1: // 作业语音
			if (TextUtils.isEmpty(homeworkurl)) {
				return;
			}
			if (player != null && player.isPlaying()) {
				return;
			}
			try {
				player = new MediaPlayer();
				player.reset();
				player.setDataSource(this, Uri.parse(homeworkurl));
				player.prepareAsync();
				player.setOnPreparedListener(new OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
						homeWorkImage1.setBackgroundResource(R.drawable.voice_blue_blank);
						voiceTime1.setVisibility(View.VISIBLE);
//						voiceTimeRight1.setVisibility(View.VISIBLE);
						messageWhat = 3;
						initTimer();
						mp.start();
					}
				});
				player.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						homeWorkImage1.setBackgroundResource(R.drawable.learn_icon_blue);
						voiceTime1.setVisibility(View.INVISIBLE);
						voiceTimeRight1.setVisibility(View.INVISIBLE);
						closeTimer();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.homeWorkImage2:
			if (soundFile != null && soundFile.exists()) {
				if (player != null && player.isPlaying()) {
					return;
				}
				try {
					player = new MediaPlayer();
					player.reset();
					player.setDataSource(soundFile.getAbsolutePath());
					player.prepare();
					player.start();
					if(hasSended){
						homeWorkImage2.setBackgroundResource(R.drawable.voice_red_blank);
					}else{
						homeWorkImage2.setBackgroundResource(R.drawable.voice_blue_blank);
					}
					voiceTime.setVisibility(View.VISIBLE);
					voiceTime.setText("");
					voiceTime.setBackgroundResource(R.drawable.learn_playing);
//					voiceTimeRight.setVisibility(View.VISIBLE);
					messageWhat = 2;
					initTimer();
					player.setOnCompletionListener(new OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer mp) {
							if(hasSended){
								homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_red);
							}else{
								homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_blue);
							}
							voiceTime.setVisibility(View.INVISIBLE);
							voiceTimeRight.setVisibility(View.INVISIBLE);
							closeTimer();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.sendMyHomework:// 发送作业
			sendHomeWork();
			break;
		default:
			break;
		}
	}

	private JournalHomeWorkResponseData repdata;

	/**
	 * 提交语音作业
	 */
	private void sendHomeWork() {
		startProgress();
//		File file = new File("C://Users//Administrator//Desktop//image//333.png");
		SharedPreferences userPreferences =
				ProtocalParser.getShareUserPreferences(this);
		String token = userPreferences.getString("jsessionId", "");
		String urlStr = "http://101.200.176.75:8080/emama/upload?resType=0"
				+ "&workId=" + workId + "&len="+ String.valueOf(soundFile.length())+"&token="+token;
		
//		String urlStr = "http://101.200.176.75:8080/emama/upload?resType=1"
//				+ "&len="+ String.valueOf(file.length())+"&token="+token;
		
		try {
			AjaxParams params = new AjaxParams();
			  params.put("res", soundFile); // 上传文件
			 
			  FinalHttp fh = new FinalHttp();
			  fh.post(urlStr, params, new AjaxCallBack(){
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				try {
					JSONObject jsonRoot = new JSONObject(t.toString());
					repdata = new JournalHomeWorkResponseData();

					if (jsonRoot.has("result")) {
						JSONObject jResult = jsonRoot.getJSONObject("result");
						repdata.commonData.result_code = jResult
								.getString("code");
						repdata.commonData.result_msg = jResult
								.getString("msg");
					}
					if (jsonRoot.has("responseData")) {
						JSONObject jResData = jsonRoot
								.getJSONObject("responseData");
						repdata.solutionId = jResData.getString("solutionId");
					}
					if (repdata.commonData.result_code.equals("0")) {
						myHomeWork.setVisibility(View.VISIBLE);
						LL_mySendHomeWork.setVisibility(View.VISIBLE);
						homeWorkImage2.setVisibility(View.VISIBLE);
						homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_blue);
						sendMyHomework.setVisibility(View.INVISIBLE);
						
						hasSended =false;

						sp.edit().putBoolean("workId"+workId, true);
						sp.edit().putString("workId"+workId+"sound", soundFile.getAbsolutePath());
						sp.edit().commit();
						endProgress();
						Toast.makeText(JournalDetailActivity.this, "发送成功。", 0).show();
					}else{
						endProgress();
						Toast.makeText(JournalDetailActivity.this, "发送失败，请稍后重试。", 0).show();
					}
				} catch (JSONException e) {
					endProgress();
					e.printStackTrace();
				}
			}
			
			@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					endProgress();
					Toast.makeText(JournalDetailActivity.this, "发送失败，请稍后重试。", 0).show();
				}
			 
			  });
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private String getBytesFromSoundFile() {
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream is = new FileInputStream(soundFile);
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				sb.append(new String(b, 0, len));
			}
			is.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if (obj != null && obj instanceof JournalHomeDetailResponseData) {
			JournalHomeDetailResponseData data = (JournalHomeDetailResponseData) obj;
			if (data.commonData.result_code.equals("0")
					|| data.commonData.result_code.equals("0000")) {
				ll_whole.setVisibility(View.VISIBLE);

				title.setText(data.date + " " + data.title);
				userName.setText(data.publisherName);
				userType.setText(data.publisherType);
				time.setText(data.publishTime);
				homeworkcontent.setText(data.dutyDescription);
				// TODO 录音
				homeworkurl = data.dutyVoiceUrl;
				if (!TextUtils.isEmpty(data.page)) {
					webview.loadUrl(data.page);
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
	
	@Override
	protected void onPause() {
		super.onPause();
		webview.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		webview.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(player !=null){
			player =null;
		}
	}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
	             finish();
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }
}
