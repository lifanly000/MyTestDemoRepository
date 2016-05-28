package com.example.wireframe.adapter;

import java.util.ArrayList;

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
import com.example.wireframe.protocal.protocalProcess.model.Article;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.VideoPlayActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EXiuXiuHomeAdapter extends BaseAdapter {

	private Context context ;
	
	private ArrayList<Article> articles = new ArrayList<Article>();
	

	// 异步加载图片的显示参数
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisk(true)
			.showImageOnLoading(R.drawable.item_image_default)
			.showImageForEmptyUri(R.drawable.item_image_default)
			.showImageOnFail(R.drawable.item_image_default).build();
	
	public void setArticles(ArrayList<Article> articles) {
		this.articles = articles;
	}

	public EXiuXiuHomeAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		return articles.size();
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
			convertView = View.inflate(context, R.layout.exiuxiu_home_listview_item, null);
			holder = new ViewHolder();  
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.wholeView = (LinearLayout) convertView.findViewById(R.id.wholeView);
			holder.zanIcon = (ImageView) convertView.findViewById(R.id.zanIcon);
			holder.commentIcon = (ImageView) convertView.findViewById(R.id.commentIcon);
			holder.zanText = (TextView) convertView.findViewById(R.id.zanText);
			holder.commentText = (TextView) convertView.findViewById(R.id.commentText);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		final Article info = articles.get(position);
		if(!TextUtils.isEmpty(info.icon)){
			((BaseActivity)context).imageLoader.displayImage(info.icon, holder.icon, options);
		}
		holder.title.setText(info.title);
		holder.zanText.setText("赞 "+info.praiseCount);
		holder.commentText.setText("评论 "+info.commentCount);
		
		holder.wholeView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,VideoPlayActivity.class);
				intent.putExtra("eShowId", info.eShowId);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	public static class ViewHolder {
		LinearLayout wholeView ;
		ImageView icon,zanIcon,commentIcon;
		TextView title,zanText,commentText;
	}
}
