package com.example.wireframe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eblock.emama.R;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeDetailResponseData;

public class GrowUpDetailAdapter extends BaseAdapter {

	private Context context ;
	private GrowUpHomeDetailResponseData data = null ;
	
	
	public void setData(GrowUpHomeDetailResponseData data) {
		this.data = data;
	}

	public GrowUpDetailAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
//		if(data != null ){
//			
//		}
		return 5;
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
			convertView = View.inflate(context, R.layout.exiuxiu_item_detail_listview_item, null);
			holder = new ViewHolder();  
			holder.userIcon = (ImageView) convertView.findViewById(R.id.userIcon);
			holder.userName = (TextView) convertView.findViewById(R.id.userName);
			holder.userType = (TextView) convertView.findViewById(R.id.userType);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.content1 = (TextView) convertView.findViewById(R.id.content1);
			holder.content2 = (TextView) convertView.findViewById(R.id.content2);
//			holder.videoView = (VideoView) convertView.findViewById(R.id.videoView);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.zanIcon = (ImageView) convertView.findViewById(R.id.zanIcon);
			holder.commentIcon = (ImageView) convertView.findViewById(R.id.commentIcon);
			holder.zanText = (TextView) convertView.findViewById(R.id.zanText);
			holder.commentText = (TextView) convertView.findViewById(R.id.commentText);
			holder.bottomLine = (ImageView) convertView.findViewById(R.id.bottomLine);
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		
		return convertView;
	}
	
	public static class ViewHolder {
		TextView userName,userType,time;
		TextView content1,content2;
//		VideoView videoView;
		ImageView image;
		ImageView userIcon,zanIcon,commentIcon;
		TextView title,zanText,commentText;
		ImageView bottomLine;
	}
}
