package com.example.wireframe.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eblock.emama.R;
import com.example.wireframe.protocal.protocalProcess.model.Works;
import com.example.wireframe.ui.VideoJournalDetailActivity;
import com.example.wireframe.ui.journal.JournalDetailActivity;

public class JournalHomeAdapter extends BaseAdapter {

	private Context context ;
	private ArrayList<Works> works = null;
	
	public void setWorks(ArrayList<Works> works) {
		this.works = works;
	}

	public JournalHomeAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		if(works !=null && works.size()>0){
			return works.size();
		}else{
			return 0;
		}
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
			convertView = View.inflate(context, R.layout.journal_home_item, null);
			holder = new ViewHolder();  
			holder.wholeView = (RelativeLayout) convertView.findViewById(R.id.wholeView);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.textType = (TextView) convertView.findViewById(R.id.textType);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		final Works workInfo = works.get(position);
		holder.title.setText(workInfo.date+" "+workInfo.title);
		holder.content.setText(workInfo.summary);
		if(workInfo.unread.equals("0")){
			holder.textType.setText("已读");
			holder.title.setTextColor(Color.parseColor("#ababab"));
		}else{
			holder.textType.setText("未读");
			holder.title.setTextColor(Color.parseColor("#1d1d1d"));
		}
		
		holder.wholeView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,VideoJournalDetailActivity.class);
				intent.putExtra("workId", workInfo.workId);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	public static class ViewHolder {
		  TextView title,textType,content;
		  RelativeLayout wholeView;
	}
}
