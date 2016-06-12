package com.example.wireframe.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.adapter.JournalCNDYAdapter;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.ProtocalParser;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeDetailResponseData;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeWorkResponseData;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.utils.ShareUtil;
import com.example.wireframe.view.ListViewInScroll;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class VideoJournalDetailActivity extends BaseActivity implements ProtocalEngineObserver, OnClickListener {

    private FrameLayout videoview;// 全屏时视频加载view
    private WebView videowebview;
    private View xCustomView;
    private xWebChromeClient xwebchromeclient;
    private String url = "http://app.eastshore.com.cn/gexu/a.html";
    private WebChromeClient.CustomViewCallback xCustomViewCallback;
    //计算点击的次数 
    private int count = 0;
    //第一次点击的时间 long型 
    private long firClick, secClick;
    //最后一次点击的时间 
    private long lastClick;
    //第一次点击的button的id 
    private int firstId;
    private onDoubleClick listClick = new onDoubleClick();
    private GestureDetector gestureScanner;

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

    private String workId = "";

    // 音频文件  
    private File soundFile;
    private MediaRecorder mediaRecorder;

    private MediaPlayer player = null;
    private MediaPlayer player2 = null;


    protected ImageLoader imageLoader = ImageLoader.getInstance();
    // 异步加载图片的显示参数
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .showImageOnLoading(R.drawable.image_default)
            .showImageForEmptyUri(R.drawable.image_default)
            .showImageOnFail(R.drawable.image_default).build();

    private ListViewInScroll cndy_listView;

    /**
     * android 6.0 申请权限
     */
    private void requestLocalPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.CAMERA},
                    0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉应用标题
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.jorunal_item_activity);
//		setHorizonEnable(0);
        requestLocalPermission();

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        userName = (TextView) findViewById(R.id.userName);
        userType = (TextView) findViewById(R.id.userType);
        time = (TextView) findViewById(R.id.time);
        userIcon = (ImageView) findViewById(R.id.userIcon);
        ll_whole = (LinearLayout) findViewById(R.id.ll_whole);
        LL_mySendHomeWork = (LinearLayout) findViewById(R.id.LL_mySendHomeWork);

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
        cndy_listView = (ListViewInScroll) findViewById(R.id.cndy_listView);

        homeWorkImage1.setOnClickListener(this);
        homeWorkImage2.setOnClickListener(this);
        sendMyHomework.setOnClickListener(this);

        workId = getIntent().getStringExtra("workId");

//		if(sp.contains("workId"+workId)  && sp.getBoolean("workId"+workId, false)){
//			myHomeWork.setVisibility(View.VISIBLE);
//			LL_mySendHomeWork.setVisibility(View.VISIBLE);
//			myHomeWork.setEnabled(true);
//			soundFile = new File(sp.getString("workId"+workId+"sound", ""));
//		}else{
        myHomeWork.setVisibility(View.INVISIBLE);
        LL_mySendHomeWork.setVisibility(View.INVISIBLE);
//		}

        cndy_adapter = new JournalCNDYAdapter(this);
        cndy_listView.setAdapter(cndy_adapter);

        listener();

        startProgress();
        startRequest();

        initwidget();
//		videowebview.loadUrl(url);
    }


    private boolean flag = false;

    private void listener() {
//		doHomeWorkBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				LL_mySendHomeWork.setVisibility(View.VISIBLE);
//				myHomeWork.setVisibility(View.VISIBLE);
//				flag = !flag;
//				if(flag){
//					changeBtnBackground(true);
//					recordingVoice();
//				}else{
//					changeBtnBackground(false);
//					if (soundFile != null && soundFile.exists()) {
////						try {
//							mediaRecorder.setOnErrorListener(null);
//							mediaRecorder.setOnInfoListener(null);
//							mediaRecorder.setPreviewDisplay(null);
//							mediaRecorder.stop(); // **停止录音**
//							mediaRecorder.release(); // **释放资源**
//							mediaRecorder = null;
////						} catch (IllegalStateException e) {
////							e.printStackTrace();
////						}
//				}
//			}
//			}
//		});
        // 开始录音
        doHomeWorkBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LL_mySendHomeWork.setVisibility(View.VISIBLE);
                myHomeWork.setVisibility(View.VISIBLE);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        voiceRecordTime = 0;
                        changeBtnBackground(true);
                        recordingVoice();
                        break;
                    case MotionEvent.ACTION_UP:
                        changeBtnBackground(false);
                        if (soundFile != null && soundFile.exists()) {
						try {
                            mediaRecorder.setOnErrorListener(null);
                            mediaRecorder.setOnInfoListener(null);
                            mediaRecorder.setPreviewDisplay(null);
                            mediaRecorder.stop(); // **停止录音**
                            mediaRecorder.release(); // **释放资源**
                            mediaRecorder = null;
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }


    private void recordingVoice() {
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
            mediaRecorder.reset();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录制的声音的来源
            mediaRecorder.setAudioSamplingRate(32);
            mediaRecorder.setAudioEncodingBitRate(32);
            // recorder.setVideoSource(video_source); //录制视频
            // 录制的声音的输出格式（必须在设置声音的编码格式之前设置）
            mediaRecorder
                    .setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
            // 设置声音的编码格式
            mediaRecorder
                    .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            // 设置声音的保存位置
            mediaRecorder.setOutputFile(soundFile.getAbsolutePath());
            mediaRecorder.prepare(); // **准备录音**
            mediaRecorder.start(); // **开始录音**
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasSended = false;

    private void changeBtnBackground(boolean b) {
        if (b) {
//			doHomeWorkBtn
//					.setBackgroundResource(R.drawable.v4_gradient_box_whole_blue);

            myHomeWork.setVisibility(View.VISIBLE);
            LL_mySendHomeWork.setVisibility(View.VISIBLE);

            voiceIcon.setBackgroundResource(R.drawable.learn_voice_2);
            voiceText.setTextColor(Color.WHITE);
            voiceTime.setBackgroundDrawable(null);
            voiceTime.setVisibility(View.INVISIBLE);
            voiceTimeRight.setVisibility(View.INVISIBLE);
            homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_blank);
            sendMyHomework.setVisibility(View.VISIBLE);
            sendMyHomework.setEnabled(false);
            homeWorkImage2.setEnabled(false);
            sendMyHomework.setBackgroundColor(Color.parseColor("#E8E8E8"));
            messageWhat = 1;
            hasSended = true;
            initTimer();
        } else {
//			doHomeWorkBtn
//					.setBackgroundResource(R.drawable.v4_gradient_box_blue);
            voiceIcon.setBackgroundResource(R.drawable.learn_voice_1);
            voiceText.setTextColor(Color.parseColor("#aa00a5fb"));
            homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_red);
            voiceTime.setVisibility(View.INVISIBLE);
            voiceTimeRight.setVisibility(View.VISIBLE);
            voiceTimeRight.setText(String.valueOf(voiceRecordTime) + "s");
            voiceText.setText("重新录入...");
            sendMyHomework.setEnabled(true);
            homeWorkImage2.setEnabled(true);
            sendMyHomework.setBackgroundResource(R.drawable.blue_btn);
            closeTimer();
        }
    }

    private void closeTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
//		if(player !=null){
//			player.stop();
//			player=null;
//		}
//		if(player2 !=null){
//			player2.stop();
//			player2=null;
//		}
    }

    /**
     * 刷新页面
     */
    private long voiceRecordTime = 0;
    Timer timer = null;
    TimerTask task = null;
    private int messageWhat = 1;

    private void initTimer() {
//		 voiceRecordTime = 0;
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
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

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    voiceTime.setVisibility(View.VISIBLE);
                    voiceTime.setText(String.valueOf(voiceRecordTime) + "s");
                    break;
                case 2:
                    voiceTimeRight.setVisibility(View.VISIBLE);
                    voiceTimeRight.setText(String.valueOf(voiceRecordTime) + "s");
                    break;
                case 3:
                    voiceTimeRight1.setVisibility(View.VISIBLE);
                    voiceTimeRight1.setText(String.valueOf(voiceRecordTime) + "s");
                    break;
                default:
                    break;
            }
        }

        ;
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
                if (player2 != null) {
                    if (hasSended) {
                        homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_red);
                    } else {
                        homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_blue);
                    }
                    voiceTime.setVisibility(View.INVISIBLE);
                    voiceTimeRight.setVisibility(View.INVISIBLE);
                    voiceRecordTime = 0;
                    closeTimer();
                    if (player2 != null) {
                        player2.stop();
                        player2 = null;
                    }
                }
                //TODO
                if (player != null && player.isPlaying()) {
                    player.pause();
                    closeTimer();
//				voiceTimeRight.setVisibility(View.INVISIBLE);
                    return;
                }
                if (player != null) {
//				voiceTimeRight.setVisibility(View.VISIBLE);
                    initTimer();
                    player.start();
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
                            voiceRecordTime = 0;
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
                            voiceRecordTime = 0;
                            closeTimer();
                            if (player != null) {
                                player.stop();
                                player = null;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.homeWorkImage2:
                if (soundFile != null && soundFile.exists()) {
                    if (player != null) {
                        homeWorkImage1.setBackgroundResource(R.drawable.learn_icon_blue);
                        voiceTime1.setVisibility(View.INVISIBLE);
                        voiceTimeRight1.setVisibility(View.INVISIBLE);
                        voiceRecordTime = 0;
                        closeTimer();
                        if (player != null) {
                            player.stop();
                            player = null;
                        }
                    }
                    if (player2 != null && player2.isPlaying()) {
                        player2.pause();
                        closeTimer();
                        return;
                    }
                    if (player2 != null) {
                        initTimer();
                        player2.start();
                        return;
                    }
                    try {
                        player2 = new MediaPlayer();
                        player2.reset();
                        player2.setDataSource(soundFile.getAbsolutePath());
                        player2.prepare();
                        player2.start();
                        if (hasSended) {
                            homeWorkImage2.setBackgroundResource(R.drawable.voice_red_blank);
                        } else {
                            homeWorkImage2.setBackgroundResource(R.drawable.voice_blue_blank);
                        }
                        voiceTime.setVisibility(View.VISIBLE);
                        voiceTime.setText("");
                        voiceTime.setBackgroundResource(R.drawable.learn_playing);
//					voiceTimeRight.setVisibility(View.VISIBLE);
                        messageWhat = 2;
                        voiceRecordTime = 0;
                        initTimer();
                        player2.setOnCompletionListener(new OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                if (hasSended) {
                                    homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_red);
                                } else {
                                    homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_blue);
                                }
                                voiceTime.setVisibility(View.INVISIBLE);
                                voiceTimeRight.setVisibility(View.INVISIBLE);
                                voiceRecordTime = 0;
                                closeTimer();
                                if (player2 != null) {
                                    player2.stop();
                                    player2 = null;
                                }
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
            case R.id.share:
                ShareUtil.shareByYoumeng(this, "eMama,易贝乐家校互动平台，帮您分分钟了解孩子学习情况。", titleStr, pageUrl);
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
                + "&workId=" + workId + "&len=" + String.valueOf(soundFile.length()) + "&token=" + token;

//		String urlStr = "http://101.200.176.75:8080/emama/upload?resType=1"
//				+ "&len="+ String.valueOf(file.length())+"&token="+token;

        try {
            AjaxParams params = new AjaxParams();
            params.put("res", soundFile); // 上传文件

            FinalHttp fh = new FinalHttp();
            fh.post(urlStr, params, new AjaxCallBack() {
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
//						myHomeWork.setVisibility(View.VISIBLE);
//						LL_mySendHomeWork.setVisibility(View.VISIBLE);
//						homeWorkImage2.setVisibility(View.VISIBLE);
//						homeWorkImage2.setBackgroundResource(R.drawable.learn_icon_blue);
//						sendMyHomework.setVisibility(View.INVISIBLE);

                            LL_mySendHomeWork.setVisibility(View.INVISIBLE);
                            myHomeWork.setVisibility(View.INVISIBLE);
                            hasSended = false;

                            editor.putBoolean("workId" + workId, true);
                            editor.putString("workId" + workId + "sound", soundFile.getAbsolutePath());
                            editor.commit();
                            endProgress();
                            //TODO
                            startRequest();
                            Toast.makeText(VideoJournalDetailActivity.this, "发送成功。", Toast.LENGTH_SHORT).show();
                        } else {
                            endProgress();
                            Toast.makeText(VideoJournalDetailActivity.this, "发送失败，请稍后重试。", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(VideoJournalDetailActivity.this, "发送失败，请稍后重试。", Toast.LENGTH_SHORT).show();
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

    private boolean isFirst = true;
    private JournalCNDYAdapter cndy_adapter;

    private String titleStr = "";
    private String pageUrl = "";

    @Override
    public void OnProtocalFinished(Object obj) {
        endProgress();
        if (obj != null && obj instanceof JournalHomeDetailResponseData) {
            JournalHomeDetailResponseData data = (JournalHomeDetailResponseData) obj;
            if (data.commonData.result_code.equals("0")
                    || data.commonData.result_code.equals("0000")) {
                ll_whole.setVisibility(View.VISIBLE);

                titleStr = data.title;
                pageUrl = data.page;

                title.setText(data.date + " " + data.title);
                userName.setText(data.publisherName);
                userType.setText(data.publisherType);
                time.setText(data.publishTime);
                if (!TextUtils.isEmpty(data.dutyDescription)) {
                    homeworkcontent.setVisibility(View.VISIBLE);
                    homeworkcontent.setText(data.dutyDescription);
                }
                if (!TextUtils.isEmpty(data.dutyVoiceUrl)) {
                    homeWorkImage1.setVisibility(View.VISIBLE);
                }
                // TODO 录音
                homeworkurl = data.dutyVoiceUrl;
                if (isFirst) {
                    isFirst = false;
                    if (!TextUtils.isEmpty(data.page)) {
                        videowebview.loadUrl(data.page);
                    }
                }
                if (data.dialogs != null && data.dialogs.size() > 0) {
                    cndy_listView.setVisibility(View.VISIBLE);
                    cndy_adapter.setDialogs(data.dialogs);
                    cndy_adapter.notifyDataSetChanged();
                }
            } else {
                if ("登录Token无效".equals(data.commonData.result_msg)) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, ByConstants.REQUEST_LOGIN);
                } else {
                    Toast.makeText(this, data.commonData.result_msg,
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

    @Override
    protected void onPause() {
        super.onPause();
        videowebview.onPause();
        if (player != null) {
            player.stop();
            player = null;
        }
        if (player2 != null) {
            player2.stop();
            player2 = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        videowebview.onResume();
        if (player != null) {
            player = null;
        }
        if (player2 != null) {
            player2 = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.stop();
            player = null;
        }
        if (player2 != null) {
            player2.stop();
            player2 = null;
        }
        cndy_adapter.closeTimer();
        super.onDestroy();
    }


    @SuppressWarnings("deprecation")
    private void initwidget() {
        // TODO Auto-generated method stub
        videoview = (FrameLayout) findViewById(R.id.video_view);
        videoview.setOnTouchListener(listClick);
        videowebview = (WebView) findViewById(R.id.webView);
        WebSettings ws = videowebview.getSettings();
        /**
         * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
         * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
         * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
         * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
         * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
         * setSupportZoom 设置是否支持变焦
         * */
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        xwebchromeclient = new xWebChromeClient();
        videowebview.setWebChromeClient(xwebchromeclient);
        videowebview.setWebViewClient(new xWebViewClientent());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                hideCustomView();
                return true;
            } else {
                //退出时加载空网址防止退出时还在播放视频
                videowebview.loadUrl("about:blank");
//		       		 mTestWebView.loadData("", "text/html; charset=UTF-8", null);
                VideoJournalDetailActivity.this.finish();
                Log.i("testwebview", "===>>>2");
            }
        }
        return true;
    }

    /**
     * 判断是否是全屏
     *
     * @return
     */
    public boolean inCustomView() {
        return (xCustomView != null);
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
    }

    /**
     * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
     *
     * @author
     */
    public class xWebChromeClient extends WebChromeClient {
        private Bitmap xdefaltvideo;
        private View xprogressvideo;

        @Override
        //播放网络视频时全屏会被调用的方法
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            videowebview.setVisibility(View.GONE);
            //如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            videoview.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            videoview.setVisibility(View.VISIBLE);
        }

        @Override
        //视频播放退出全屏会被调用的
        public void onHideCustomView() {

            if (xCustomView == null || videoview == null)//不是全屏播放状态
                return;

            // Hide the custom view.
//			setHorizonEnable(1);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            videoview.removeView(xCustomView);
            xCustomView = null;
            videoview.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();

            videowebview.setVisibility(View.VISIBLE);

            //Log.i(LOGTAG, "set it to webVew");
        }

        //视频加载添加默认图标
        @Override
        public Bitmap getDefaultVideoPoster() {
            //Log.i(LOGTAG, "here in on getDefaultVideoPoster");
            if (xdefaltvideo == null) {
                xdefaltvideo = BitmapFactory.decodeResource(
                        getResources(), R.drawable.videoicon);
            }
            return xdefaltvideo;
        }

        //视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {
            //Log.i(LOGTAG, "here in on getVideoLoadingPregressView");

            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater.from(VideoJournalDetailActivity.this);
                xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }

        //网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            (VideoJournalDetailActivity.this).setTitle(title);
        }

//         @Override
//       //当WebView进度改变时更新窗口进度
//         public void onProgressChanged(WebView view, int newProgress) {
//        	 (MainActivity.this).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress*100);
//         }


    }

    /**
     * 处理各种通知、请求等事件
     *
     * @author
     */
    public class xWebViewClientent extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("webviewtest", "shouldOverrideUrlLoading: " + url);
            return false;
        }


        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            // 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
            // view.loadUrl(file:///android_asset/error.html );

            Log.i("onPageStarted", "错误");
            if (videowebview != null)
                videowebview.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i("onPageStarted", "onPage               Started" + url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i("onPageFinished", "onPageFinished>>>>>>" + url);
            if (videowebview != null)
                videowebview.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
        }
    }

    /**
     * 当横竖屏切换时会调用该方法
     *
     * @author
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("testwebview", "=====<<<  onConfigurationChanged  >>>=====");
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("webview", "   现在是横屏1");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("webview", "   现在是竖屏1");
        }
    }

    class onDoubleClick implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                count++;
                if (count == 1) {
                    firClick = System.currentTimeMillis();

                } else if (count == 2) {
                    secClick = System.currentTimeMillis();
                    if (secClick - firClick < 500) {
                        //双击事件  
                        Toast.makeText(VideoJournalDetailActivity.this, "双击了屏幕", Toast.LENGTH_LONG).show();
                    } else {

                    }
                    count = 0;
                    firClick = 0;
                    secClick = 0;

                }
            }
            return true;
        }

    }

}
