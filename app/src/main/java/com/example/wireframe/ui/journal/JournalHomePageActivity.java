package com.example.wireframe.ui.journal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.ByConstants;
import com.eblock.emama.R;
import com.example.wireframe.adapter.JournalHomeAdapter;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberResponseData;
import com.example.wireframe.protocal.protocalProcess.model.JournalDayInfo;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeRequestData;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.growup.GrowUpNumGetHomePageActivity;
import com.example.wireframe.ui.mycenter.LoginActivity;
import com.example.wireframe.view.CalendarView;
import com.example.wireframe.view.XListView;
import com.example.wireframe.view.XListView.IXListViewListener;

public class JournalHomePageActivity extends BaseActivity implements
		ProtocalEngineObserver, OnClickListener {
	
	private ImageView userIcon ;
	private TextView dayTitle,growUpNum;
	public XListView xListView ;
	public JournalHomeAdapter adapter ;
//	private KCalendar calendar ;
	
	private CalendarView calV = null;
	private GridView gridView = null;
	private TextView topText = null;
	private static int jumpMonth = 0; // 每次点击按钮，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0; // 点击超过一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	
	 //系统当前时间
    private String sysDate = "";  
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";
    
    private String local_year = "";
    private String local_month = "";
    private String currentDay = "";
    
    private GestureDetector mGestureDetector;
	
	SimpleDateFormat format1 = new SimpleDateFormat("yyyy MM");
	SimpleDateFormat format2 = new SimpleDateFormat("MMM yyyy", Locale.US);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

	public JournalHomePageActivity() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date);
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
	}
	
//	// 上一月和下一月的按钮
//	private	Button previous;
//	private	Button next;
//	private LinearLayout main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.journal_home_activity);
		
		mGestureDetector = new GestureDetector(this, new MyOnGestureListener());
		
		startProgress();
		initView();
		initData();
//		startRequest();
	}
	
	 public void initData(){
	        Date date = new Date();
	        sysDate = sdf.format(date);  //当期日期
	        sys_year = sysDate.split("-")[0];
	        sys_month = sysDate.split("-")[1];
	        sys_day = sysDate.split("-")[2];
	        dayTitle.setText(sys_year+"年"+sys_month+"月");
	        currentDay = sys_day ;
	        local_year = sys_year;
	        local_month = sys_month;
	    }
	 
	private void startRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		JournalHomeRequestData data = new JournalHomeRequestData();
		data.yearMonth = sys_year+"年"+sys_month+"月";
		engine.startRequest(SchemaDef.JOURNAL_HOME_PAGE, data);
	}
	
	/**
	 * 初始化布局控件
	 */
	private void initView() {
		userIcon = (ImageView) findViewById(R.id.userIcon);
		dayTitle = (TextView) findViewById(R.id.dayTitle);
		growUpNum = (TextView) findViewById(R.id.growupNum);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView = (XListView) findViewById(R.id.xlistView);

		initGridView();
		
		growUpNum.setOnClickListener(this);
		
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(true);
		adapter = new JournalHomeAdapter(this);
		xListView.setAdapter(adapter);
		xListView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				startRequest();
			}
			
			@Override
			public void onLoadMore() {
				
			}
		});
		
	}
	private void initGridView() {
		calV = new CalendarView(this, getResources(), jumpMonth, jumpYear,
				year_c, month_c, day_c);
		gridView = (GridView) findViewById(R.id.gridView1);
//		gridView.setVerticalSpacing(1);
//		gridView.setHorizontalSpacing(1);
		gridView.setBackgroundResource(R.color.white);
//		gridView.setPadding(1, 1, 1, 1);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				String scheduleDay = calV.getDateByClickItem(position).split(
						"\\.")[0]; // 这一天的阳历
				String titleYear = calV.getShowYear();
				String titleMonth = calV.getShowMonth();
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				
				currentDay = scheduleDay;

				if (position >= startPosition && position <= endPosition) {
//					Toast.makeText(
//							JournalHomePageActivity.this,
//							"被点击的日期 : " + titleYear + "年" + titleMonth + "月"
//									+ scheduleDay + "日", Toast.LENGTH_LONG)
//							.show();
					calV.setCurrentFlag(position);
					calV.notifyDataSetChanged();
					fillItemData(scheduleDay);
				} else if (position < startPosition) {
//					calV.setCurrentFlag(position);
//					calV.notifyDataSetChanged();
					getPreviousMonth();
					
				} else if (position > endPosition) {
//					calV.setCurrentFlag(position);
//					calV.notifyDataSetChanged();
					getNextMonth();
				} else {
					Toast.makeText(JournalHomePageActivity.this, "No", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		gridView.setAdapter(calV);
		System.out.println(calV);
		
		gridView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				// 一定要返回true，不然获取不到完整的事件
                return false;
			}
		});
	}

	/**
	 * 成长值
	 */
	private void startGrowUpNumRequest() {
		ProtocalEngine engine = new ProtocalEngine(this);
		engine.setObserver(this);
		GrowUpNumberRequestData data = new GrowUpNumberRequestData();
		data.onlyAmount = "1";
		engine.startRequest(SchemaDef.GROW_UP_NUMBER, data);
	}

	
	private void getPreviousMonth() {
		//TODO
		if(Integer.parseInt(local_month)-1>0){
			local_month = String.valueOf(Integer.parseInt(local_month)-1);
			dayTitle.setText(local_year+"年"+local_month+"月");
		}else{
			local_month ="12";
			local_year = String.valueOf(Integer.parseInt(local_year)-1);
			dayTitle.setText(local_year+"年"+local_month+"月");
		}
		
		jumpMonth--;
		calV = new CalendarView(this, getResources(), jumpMonth,
				jumpYear, year_c, month_c, day_c);
		gridView.setAdapter(calV);
//		addTextToTopTextView(topText);
		if(local_year.equals(sys_year) && local_month.equals(sys_month)){
			currentDay = sys_day;
			if(homeData !=null){
				calV.setData(homeData);
				calV.notifyDataSetChanged();
				fillItemData(sys_day);
			}
		}else{
			adapter.setWorks(null);
			adapter.notifyDataSetChanged();
		}
	}

	private void getNextMonth() {
		//TODO
		if(Integer.parseInt(local_month)+1<13){
			local_month = String.valueOf(Integer.parseInt(local_month)+1);
			dayTitle.setText(local_year+"年"+local_month+"月");
		}else{
			local_month ="1";
			local_year = String.valueOf(Integer.parseInt(local_year)+1);
			dayTitle.setText(local_year+"年"+local_month+"月");
		}
		
		jumpMonth++;
		calV = new CalendarView(this, getResources(), jumpMonth,
				jumpYear, year_c, month_c, day_c);
		gridView.setAdapter(calV);
//		addTextToTopTextView(topText);
		if(local_year.equals(sys_year) && local_month.equals(sys_month)){
			currentDay = sys_day;
			if(homeData !=null){
				calV.setData(homeData);
				calV.notifyDataSetChanged();
				fillItemData(sys_day);
			}
		}else{
			adapter.setWorks(null);
			adapter.notifyDataSetChanged();
		}
	}

	private void addTextToTopTextView(TextView view) {
		StringBuffer textDate = new StringBuffer();
		String datestr = calV.getShowYear() + " " + calV.getShowMonth();
//		try {
//			textDate.append(format2.format(format1.parse(datestr)));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		view.setText(datestr);
		view.setTextColor(Color.WHITE);
		view.setTypeface(Typeface.DEFAULT_BOLD);

	}
	
	private JournalHomeResponseData homeData = null;
	/**
	 * 填充每日信息
	 */
	private void fillItemData(String day) {
		adapter.setWorks(null);
		adapter.notifyDataSetChanged();
		if(homeData !=null && homeData.daysInfo !=null && homeData.daysInfo.size()>0){
			for(JournalDayInfo info :homeData.daysInfo){
				if(info.day.equals(day)){
					adapter.setWorks(info.works);
					adapter.notifyDataSetChanged();
				}
			}
		}
	}
	@Override
	public void OnProtocalFinished(Object obj) {
		endProgress();
		if(obj != null && obj instanceof JournalHomeResponseData){
			xListView.stopRefresh();
			xListView.setRefreshTime("");
			JournalHomeResponseData data=(JournalHomeResponseData)obj;
			if(data.commonData.result_code.equals("0") || data.commonData.result_code.equals("0000")){
				calV.setData(data);
				calV.notifyDataSetChanged();
				homeData = data;
				fillItemData(currentDay);
			}else{
				if("登录Token无效".equals(data.commonData.result_msg)){
					Intent intent = new Intent(this,LoginActivity.class);
					startActivityForResult(intent, ByConstants.REQUEST_LOGIN);
				}else{
					Toast.makeText(this, data.commonData.result_msg,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		if (obj != null && obj instanceof GrowUpNumberResponseData) {
			GrowUpNumberResponseData countreq = (GrowUpNumberResponseData) obj;
			if (countreq.commonData.result_code.equals("0")
					|| countreq.commonData.result_code.equals("0000")) {
				growUpNum.setText("成长值 "+countreq.amount);
//			}else{
//				if("登录Token无效".equals(countreq.commonData.result_msg)){
//					Intent intent = new Intent(this,LoginActivity.class);
//					startActivity(intent);
//				}else{
//					Toast.makeText(this, countreq.commonData.result_msg,
//							Toast.LENGTH_SHORT).show();
//				}
			}
		}
	}

	@Override
	public void OnProtocalError(int errorCode) {
		endProgress();
		xListView.stopRefresh();
		Toast.makeText(this, "请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnProtocalProcess(Object obj) {

	}
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.growupNum: //成长值
			intent = new Intent(this,GrowUpNumGetHomePageActivity.class);
			startActivityForResult(intent, 10005);
			break;

		default:
			break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ByConstants.REQUEST_LOGIN) {
			if (!application.isLogin) {
				finish();
			} 
		}
	};
	
	// 两秒内按返回键两次退出程序
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                application.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	startRequest();
    	startGrowUpNumRequest();
    }
    
    class MyOnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        	// 向左/上滑动
    		if (e1.getX() - e2.getX() > 20) {
    			getNextMonth();
    		}
    		// 向右/下滑动
    		else if (e1.getX() - e2.getX() < -20) {
    			getPreviousMonth();
    		}
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }
    }
}
