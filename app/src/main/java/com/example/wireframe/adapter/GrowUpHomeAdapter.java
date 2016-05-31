package com.example.wireframe.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eblock.emama.R;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeResponseData;
import com.example.wireframe.protocal.protocalProcess.model.ReportInfo;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.VideoGrowUpDetailActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class GrowUpHomeAdapter extends BaseAdapter {

	private Context context ;
	private GrowUpHomeResponseData data = null;
	private String reportType = "" ;
	
	//异步加载图片的显示参数
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.showImageOnLoading(R.drawable.image_default)
				.showImageForEmptyUri(R.drawable.image_default)
				.showImageOnFail(R.drawable.image_default)
				.build();
	
	public GrowUpHomeAdapter(Context context, String reportType) {
		super();
		this.context = context;
		this.reportType = reportType;
	}
	

	public void setData(GrowUpHomeResponseData data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		if(data !=null && data.reports !=null && data.reports.size()>0){
			return data.reports.size();
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
			if("1".equals(reportType)) {
				convertView = View.inflate(context, R.layout.growup_home_item_report, null);
			}else{
				convertView = View.inflate(context, R.layout.growup_home_item, null);
			}
			holder = new ViewHolder();  
			holder.wholeView = (LinearLayout) convertView.findViewById(R.id.wholeView);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		final ReportInfo info = data.reports.get(position);
		holder.title.setText(info.title);
		holder.time.setText(info.publishTime);
		if(!TextUtils.isEmpty(info.imageUrl)){
			((BaseActivity)context).imageLoader.displayImage(info.imageUrl,holder.image, options);
		}
		
		holder.wholeView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,VideoGrowUpDetailActivity.class);
				intent.putExtra("reportId", info.reportId);
				intent.putExtra("reportType", info.reportType);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	public static class ViewHolder {
		  ImageView image;
		  TextView title,time;
		  LinearLayout wholeView;
	}
}
