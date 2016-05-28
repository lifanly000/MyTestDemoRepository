package com.example.wireframe.ui.mycenter;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.RankInfoRequestData;
import com.example.wireframe.protocal.protocalProcess.model.RankInfoResponseData;
import com.example.wireframe.protocal.protocalProcess.model.RankItem;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.utils.ShareUtil;
import com.example.wireframe.view.XListView;

public class RankListActivity extends BaseActivity implements OnClickListener, ProtocalEngineObserver {
	
	private XListView listView ;
	private TextView selfRank;
	private MyAdapter adapter ;
	private LinearLayout bottomLL;
	
	private TextView pre ;
	private TextView next ;
	
	private TextView country;
	private TextView school;
	private TextView classes;
	
	private int index = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rank_list_activity);
		
		listView = (XListView) findViewById(R.id.listView);
		selfRank = (TextView) findViewById(R.id.myRank);
		bottomLL = (LinearLayout) findViewById(R.id.bottomLL);
		pre = (TextView) findViewById(R.id.pre);
		next = (TextView) findViewById(R.id.next);
		country = (TextView) findViewById(R.id.country);
		school = (TextView) findViewById(R.id.school);
		classes = (TextView) findViewById(R.id.classes);
		country.setOnClickListener(this);
		school.setOnClickListener(this);
		classes.setOnClickListener(this);
		
		bottomLL.setVisibility(View.GONE);
		pre.setEnabled(false);
		next.setEnabled(false);
		
		findViewById(R.id.back).setOnClickListener(this);
		pre.setOnClickListener(this);
		next.setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
		
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(false);
		
		listView.setXListViewListener(new XListView.IXListViewListener() {
			
			@Override
			public void onRefresh() {
				itemsAll.clear();
				startRequest();
			}
			
			@Override
			public void onLoadMore() {
				
			}
		});
		
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		
//		setPageNum();
		setBtnView();
		startProgress();
		startRequest();
	}

	private int pageNo = 1;
	private int pageSize = 10;
	
	
	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		RankInfoRequestData data = new RankInfoRequestData();
		data.type = String.valueOf(index);
		data.pageNo = String.valueOf(pageNo);
		data.pageSize =String.valueOf(pageSize);
		engine.startRequest(SchemaDef.RANK_INFO, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.pre:
			pageNo--;
			setPageNum();
			fillItemsData();
			adapter.notifyDataSetChanged();
			break;
		case R.id.next:
			pageNo++;
			startProgress();
			startRequest();
			break;
		case R.id.share:
//			shareApplication();
//			ShareUtil.shareByYoumeng(this,"emama排行榜分享~ ","分享我的排行榜","");
			break;
		case R.id.country:
			index =1;
			setBtnView();
			break;
		case R.id.school:
			index =2;
			setBtnView();
			break;
		case R.id.classes:
			index =3;
			setBtnView();
			break;

		default:
			break;
		}
	}
	/**
	 * 设置index
	 */
	private void setBtnView() {
		itemsAll.clear();
		items.clear();
		pageNo =1;
		switch (index) {
		case 1:
			country.setBackgroundColor(Color.parseColor("#919191"));
			school.setBackgroundColor(Color.parseColor("#eeeeee"));
			classes.setBackgroundColor(Color.parseColor("#eeeeee"));
			break;
		case 2:
			school.setBackgroundColor(Color.parseColor("#919191"));
			country.setBackgroundColor(Color.parseColor("#eeeeee"));
			classes.setBackgroundColor(Color.parseColor("#eeeeee"));
			break;
		case 3:
			classes.setBackgroundColor(Color.parseColor("#919191"));
			school.setBackgroundColor(Color.parseColor("#eeeeee"));
			country.setBackgroundColor(Color.parseColor("#eeeeee"));
			break;

		default:
			break;
		}
		startRequest();
	}

	/**
	 * 填充上一页的数据
	 */
	private void fillItemsData() {
		items.clear();
		for(int i= (pageNo-1)*pageSize ;i<pageNo*pageSize;i++){
			items.add(itemsAll.get(i));
		}
	}

	private ArrayList<RankItem> items = new ArrayList<RankItem>();
	private ArrayList<RankItem> itemsAll = new ArrayList<RankItem>();
	private String totolCount = "";
	
	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof RankInfoResponseData){
			listView.stopRefresh();
			listView.setRefreshTime("");
			RankInfoResponseData data=(RankInfoResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				if(!TextUtils.isEmpty(data.selfRank)){
					selfRank.setText("我的排名："+data.selfRank);
				}
				if(data.items!=null && data.items.size()>0){
					itemsAll.addAll(data.items);
					items.clear();
					items.addAll(data.items);
					bottomLL.setVisibility(View.VISIBLE);
				}
				totolCount = data.totolCount;
				setPageNum();
				listView.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
			}else{
				Toast.makeText(this, "暂无数据，请稍后重试", 0).show();
			}
		}
	}
	
	/**
	 * 设置上下页 按钮是否可用
	 * @param totolCount 
	 */
	private void setPageNum() {
		int count = 0;
		try {
			count = Integer.parseInt(totolCount);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if(count>(pageNo*pageSize)){
			next.setEnabled(true);
			next.setTextColor(Color.parseColor("#333333"));
		}else{
			next.setEnabled(false);
			next.setTextColor(Color.parseColor("#999999"));
		}
		if(pageNo>1){
			listView.setPullRefreshEnable(false);
			pre.setEnabled(true);
			pre.setTextColor(Color.parseColor("#333333"));
		}else{
			listView.setPullRefreshEnable(true);
			pre.setEnabled(false);
			pre.setTextColor(Color.parseColor("#999999"));
		}
	}

	@Override
	public void OnProtocalError(int errorCode) {                     
		Toast.makeText(this, "暂无数据，请稍后重试", 0).show();
		endProgress();
	}

	@Override
	public void OnProtocalProcess(Object obj) {
		
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return items.size();
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
			ViewHolder holder =null;
			if(convertView ==null){
				holder = new ViewHolder();
				convertView =	View.inflate(RankListActivity.this, R.layout.rank_list_item, null);
				holder.rank = (TextView) convertView.findViewById(R.id.rank);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.number = (TextView) convertView.findViewById(R.id.number);
				holder.region = (TextView) convertView.findViewById(R.id.region);
				holder.score = (TextView) convertView.findViewById(R.id.score);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			RankItem info = items.get(position);
			holder.rank.setText(String.valueOf(position+1+(pageNo-1)*pageSize));
			holder.name.setText(info.name);
			holder.number.setText(info.number);
			holder.region.setText(info.region);
			holder.score.setText(info.score);
			
			return convertView;
		}
		class ViewHolder{
			TextView rank;
			TextView name;
			TextView number;
			TextView region;
			TextView score;
		}
	}
	
}
