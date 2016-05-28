package com.example.wireframe.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eblock.emama.R;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberResponseData;
import com.example.wireframe.protocal.protocalProcess.model.Task;
import com.example.wireframe.ui.growup.GrowUpHomePageActivity;
import com.example.wireframe.ui.growup.GrowUpNumGetHomePageActivity;

public class GrowUpGetNumHomeAdapter extends BaseAdapter {

	private Context context ;
	
	private GrowUpNumberResponseData data ;
	
	public GrowUpGetNumHomeAdapter(Context context) {
		super();
		this.context = context;
	}

	
	public void setData(GrowUpNumberResponseData data) {
		this.data = data;
	}


	@Override
	public int getCount() {
		if(data !=null && data.tasks !=null && data.tasks.size()>0){
			return data.tasks.size();
		}
		return 0;
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
		if(convertView ==null)
		{
			convertView = View.inflate(context, R.layout.grow_up_home_page_item, null);
			holder = new ViewHolder();  
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.detail = (TextView) convertView.findViewById(R.id.detail);
			holder.wholeView = (RelativeLayout) convertView.findViewById(R.id.wholeView);
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Task task = data.tasks.get(position);
		holder.content.setText(task.name+"("+task.score+")");
		if(task.lastComplete.equals("0")){
			//未完成
			holder.detail.setBackgroundColor(Color.parseColor("#FB4700"));
			holder.detail.setTextColor(Color.parseColor("#FFFFFF"));
			holder.detail.setText("任务完成 +"+task.lastCompleteScore);
		}else{
			//完成
			holder.detail.setBackgroundColor(Color.WHITE);
			holder.detail.setTextColor(Color.parseColor("#00a5fb"));
			holder.detail.setText("去做任务 奖"+task.lastCompleteScore);
		}
//		initClick(holder.wholeView,position,task.tag);
		return convertView;
	}
	
	private void initClick(RelativeLayout wholeView, final int position, final String tag) {
		wholeView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if("emamaAudio".equals(tag)){
					//完成Emama语音作业 
					Intent intent = new Intent(context,GrowUpHomePageActivity.class);
					intent.putExtra("reportType", "1");
					context.startActivity(intent);
				}else if("emamaWork".equals(tag)){
					//完成Emama课件作业
					Intent intent = new Intent(context,GrowUpHomePageActivity.class);
					intent.putExtra("reportType", "2");
					context.startActivity(intent);
				}else if("viewReport".equals(tag)){
					//查看线下学习报告
					Intent intent = new Intent(context,GrowUpHomePageActivity.class);
					intent.putExtra("reportType", "1");
					context.startActivity(intent);
				}else if("viewTrace".equals(tag)){
					//查看成长轨迹
//					Intent intent = new Intent(context,TabHostActivity.class);
//					context.startActivity(intent);
					if(context instanceof GrowUpNumGetHomePageActivity){
						((GrowUpNumGetHomePageActivity)context).setResult(102);
						((GrowUpNumGetHomePageActivity)context).finish();
					}
				}else if("viewOnline".equals(tag)){
					//查看线上学习报告
					Intent intent = new Intent(context,GrowUpHomePageActivity.class);
					intent.putExtra("reportType", "2");
					context.startActivity(intent);
				}else if("share".equals(tag) || "approve".equals(tag) || "comment".equals(tag)){
					//信息站外分享
//					Intent intent = new Intent(context,TabHostActivity.class);
//					context.startActivity(intent);
					((GrowUpNumGetHomePageActivity)context).setResult(103);
					((GrowUpNumGetHomePageActivity)context).finish();
				}
//				else if("approve".equals(tag)){
//					//信息点赞
//					Intent intent = new Intent(context,GrowUpFirstActivity.class);
//					context.startActivity(intent);
//				}else if("comment".equals(tag)){
//					//信息评论
//					Intent intent = new Intent(context,GrowUpFirstActivity.class);
//					context.startActivity(intent);
//				}
			}
		});
	}

	public static class ViewHolder {
		TextView content,detail;
		RelativeLayout wholeView;
	}
}
