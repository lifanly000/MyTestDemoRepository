package com.example.wireframe.adapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.eblock.emama.R;
import com.example.wireframe.protocal.protocalProcess.model.DialogInfo;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JournalCNDYAdapter extends BaseAdapter {

	private ArrayList<DialogInfo> dialogs = new ArrayList<DialogInfo>();
	private Context context ;
	
	public void setDialogs(ArrayList<DialogInfo> dialogs) {
		this.dialogs = dialogs;
	}

	public JournalCNDYAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		return dialogs.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.cndy_layout, null);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.cndy_teacher = (LinearLayout) convertView.findViewById(R.id.cndy_teacher);
			holder.voice_RL_left = (RelativeLayout) convertView.findViewById(R.id.voice_RL_left);
			holder.voiceTime_left = (TextView) convertView.findViewById(R.id.voiceTime_left);
			holder.voiceTimeRight_left = (TextView) convertView.findViewById(R.id.voiceTimeRight_left);
			holder.textLeft = (TextView) convertView.findViewById(R.id.textLeft);
			
			holder.cndy_student = (LinearLayout) convertView.findViewById(R.id.cndy_student);
			holder.voice_RL_right = (RelativeLayout) convertView.findViewById(R.id.voice_RL_right);
			holder.voiceTime_right = (TextView) convertView.findViewById(R.id.voiceTime_right);
			holder.voiceTimeRight_right = (TextView) convertView.findViewById(R.id.voiceTimeRight_tight);
			holder.textRight = (TextView) convertView.findViewById(R.id.textRight);
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		DialogInfo info = dialogs.get(position);
		holder.time.setText(info.time);
		if(info.from.equals("1")){
			holder.cndy_teacher.setVisibility(View.VISIBLE);
			holder.cndy_student.setVisibility(View.GONE);
			if(info.type.equals("1")){
				holder.voice_RL_left.setVisibility(View.VISIBLE);
				holder.textLeft.setVisibility(View.GONE);
				initVoice(holder.voice_RL_left,holder.voiceTime_left,holder.voiceTimeRight_left,info.urlOrContent);
			}else{
				holder.voice_RL_left.setVisibility(View.GONE);
				holder.textLeft.setVisibility(View.VISIBLE);
				holder.textLeft.setText(info.urlOrContent);
			}
		}else{
			holder.cndy_teacher.setVisibility(View.GONE);
			holder.cndy_student.setVisibility(View.VISIBLE);
			if(info.type.equals("1")){
				holder.voice_RL_right.setVisibility(View.VISIBLE);
				holder.textRight.setVisibility(View.GONE);
				initVoice(holder.voice_RL_right,holder.voiceTime_right,holder.voiceTimeRight_right,info.urlOrContent);
			}else{
				holder.voice_RL_right.setVisibility(View.GONE);
				holder.textRight.setVisibility(View.VISIBLE);
				holder.textRight.setText(info.urlOrContent);
			}
		}
		
		
		return convertView;
	}
	
	private MediaPlayer player ;
	/**
	 * 播放语音
	 * @param voice_RL_left
	 * @param voiceTime_left
	 * @param voiceTimeRight_left
	 * @param textLeft
	 * @param urlOrContent
	 */
	private void initVoice(final RelativeLayout voice_RL_left,
			final TextView voiceTime_left, final TextView voiceTimeRight_left,
			 final String urlOrContent) {
		
		voice_RL_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(urlOrContent)) {
					return;
				}
				if (player != null && player.isPlaying()) {
					player.pause();
					closeTimer();
					return;
				}
				if(player!=null ){
					initTimer();
					player.start();
					return;
				}
				try {
					player = new MediaPlayer();
					player.reset();
					player.setDataSource(context, Uri.parse(urlOrContent));
					player.prepareAsync();
					player.setOnPreparedListener(new OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mp) {
							voice_RL_left.setBackgroundResource(R.drawable.voice_red_blank2);
							voiceTime_left.setVisibility(View.VISIBLE);
//							voiceTimeRight1.setVisibility(View.VISIBLE);
							myBean = new Bean();
							myBean.voiceTime = voiceTime_left;
							myBean.voiceTimeRight = voiceTimeRight_left;
							messageWhat = 1;
							voiceRecordTime = 0;
							initTimer();
							mp.start();
						}
					});
					player.setOnCompletionListener(new OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer mp) {
							voice_RL_left.setBackgroundResource(R.drawable.learn_icon_red2);
							voiceTime_left.setVisibility(View.INVISIBLE);
							voiceTimeRight_left.setVisibility(View.INVISIBLE);
							voiceRecordTime = 0;
							myBean = null;
							closeTimer();
							if(player !=null){
								player.stop();
								player=null;
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Bean myBean;
	
	/**
	 *刷新页面
	 */
	private long voiceRecordTime = 0;
	Timer timer = null;
	TimerTask task = null;
	private int messageWhat = 1;
	 
	 private void initTimer(){
//		 voiceRecordTime = 0;
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
				if(myBean !=null){
					myBean.voiceTime.setVisibility(View.VISIBLE);
					myBean.voiceTimeRight.setText(String.valueOf(voiceRecordTime)+"s");
				}
				break;
			default:
				break;
			}
		};
	};
	
	public void closeTimer(){
		if(timer !=null){
			timer.cancel();
			timer =null;
		}
		if(task !=null){
			task.cancel();
			task =null;
		}
	}
	
	static class ViewHolder{
		TextView time;
		
		LinearLayout cndy_teacher;
		RelativeLayout voice_RL_left;
		TextView voiceTime_left;
		TextView voiceTimeRight_left;
		TextView textLeft;
		
		LinearLayout cndy_student;
		RelativeLayout voice_RL_right;
		TextView voiceTime_right;
		TextView voiceTimeRight_right;
		TextView textRight;
	}
	
	class Bean{
		TextView voiceTime;
		TextView voiceTimeRight;
	}
}
