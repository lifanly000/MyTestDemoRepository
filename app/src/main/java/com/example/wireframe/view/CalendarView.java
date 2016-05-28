package com.example.wireframe.view;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eblock.emama.R;
import com.example.wireframe.adapter.JournalHomeAdapter;
import com.example.wireframe.protocal.protocalProcess.model.JournalDayInfo;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeResponseData;
import com.example.wireframe.ui.journal.JournalHomePageActivity;

/**
 * 日历gridview中的每一个item显示的textview
 * @author willm zhang
 *
 */
public class CalendarView extends BaseAdapter  {



//    private ScheduleDAO dao = null;
    private boolean isLeapyear = false;  //是否为闰年
    private int daysOfMonth = 0;      //某月的天数
    private int dayOfWeek = 0;        //具体某一天是星期几
    private int lastDaysOfMonth = 0;  //上一个月的总天数
    private Context context;
    private String[] dayNumber = new String[42];  //一个gridview中的日期存入此数组中
    private SpecialCalendar sc = null;
    
    private String currentYear = "";
    private String currentMonth = "";
    private String currentDay = "";
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    private int currentFlag = -1;     //用于标记当天
    private int[] schDateTagFlag = null;  //存储当月所有的日程日期
    
    private String showYear = "";   //用于在头部显示的年份
    private String showMonth = "";  //用于在头部显示的月份
    private String animalsYear = ""; 
    private String leapMonth = "";   //闰哪一个月
    private String cyclical = "";   //天干地支
    //系统当前时间
    private String sysDate = "";  
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";
    
    //日程时间(需要标记的日程日期)
    private String sch_year = "";
    private String sch_month = "";
    private String sch_day = "";
    
    SpecialCalendar spe = new SpecialCalendar();
    
    private JournalHomeResponseData data ;
    
    
    
    
    public void setData(JournalHomeResponseData data) {
		this.data = data;
	}

	public CalendarView(){
        Date date = new Date();
        sysDate = sdf.format(date);  //当期日期
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];
    	// 实例化收拾监听器
    }

    public CalendarView(Context context,Resources rs,int jumpMonth,int jumpYear,int year_c,int month_c,int day_c){
        this();
        this.context= context;
        sc = new SpecialCalendar();
        int stepYear = year_c+jumpYear;
        int stepMonth = month_c+jumpMonth ;
        if(stepMonth > 0){
            //往下一个月跳转
            if(stepMonth%12 == 0){
                stepYear = year_c + stepMonth/12 -1;
                stepMonth = 12;
            }else{
                stepYear = year_c + stepMonth/12;
                stepMonth = stepMonth%12;
            }
        }else{
            //往上一个月跳转
            stepYear = year_c - 1 + stepMonth/12;
            stepMonth = stepMonth%12 + 12;
            if(stepMonth%12 == 0){
                
            }
        }
        
        currentYear = String.valueOf(stepYear);;  //得到当前的年份
        currentMonth = String.valueOf(stepMonth);  //得到本月 （jumpMonth为跳动的次数，每滑动一次就增加一月或减一月）
        currentDay = String.valueOf(day_c);  //得到当前日期是哪天
        
        getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));
        
    }
    
    public CalendarView(Context context,Resources rs,int year, int month, int day){
        this();
        this.context= context;
        sc = new SpecialCalendar();
        currentYear = String.valueOf(year);;  //得到跳转到的年份
        currentMonth = String.valueOf(month);  //得到跳转到的月份
        currentDay = String.valueOf(day);  //得到跳转到的天
        getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));
    }
    
    @Override
    public int getCount() {
        return 42;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHoler holder = new ViewHoler();
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.calendarview, null);
            holder.tView = (TextView) convertView.findViewById(R.id.day);
            holder.main = (RelativeLayout) convertView.findViewById(R.id.main);
            holder.icon = (TextView) convertView.findViewById(R.id.imageView1);
            holder.dot = (ImageView)convertView.findViewById(R.id.dot);
            holder.backgroundImage = (ImageView)convertView.findViewById(R.id.backgroundImage);
            convertView.setTag(holder);
         }else{
        	 holder = (ViewHoler) convertView.getTag();
 			resetViewHolder(holder);
         }
        
        String d = dayNumber[position].split("\\.")[0];
        SpannableString sp = new SpannableString(d);
        holder.tView.setText(sp);
        if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
            // 当前月信息显示
        	holder.tView.setTextColor(Color.parseColor("#3e3e3e"));// 当月
        }
        if(position == 0 || position == 6 || position ==7 || position == 13 || position == 14 || position ==20 ||
        		position == 21 || position == 27 || position ==28 ||position == 34 || position == 35 || position ==41 ){
        	//周六日
        	holder.tView.setTextColor(Color.parseColor("#bdbdbd"));
        }
        holder.icon.setVisibility(View.INVISIBLE);
        holder.dot.setVisibility(View.INVISIBLE);
        
        if(data !=null && data.daysInfo !=null && data.daysInfo.size()>0&& (position < daysOfMonth + dayOfWeek && position >= dayOfWeek)){
	        for(JournalDayInfo journalDayInfo :data.daysInfo){
	        	
	        	if(journalDayInfo.day.equals(holder.tView.getText().toString())){
	        		if(journalDayInfo.unread.equals("1")){
	            		//未读
	            		holder.icon.setVisibility(View.VISIBLE);
	            		holder.icon.setText(String.valueOf(journalDayInfo.works.size()));
	            	}
	            	if(journalDayInfo.actStatus.equals("1")){
	            		//未参加 红杠
	            		holder.dot.setVisibility(View.VISIBLE);
	            		holder.dot.setBackgroundColor(Color.RED);
	            	}else if(journalDayInfo.actStatus.equals("2")){
	            		//2-已参加（蓝杠）
	            		holder.dot.setVisibility(View.VISIBLE);
	            		holder.dot.setBackgroundColor(Color.BLUE);
	            	}else if(journalDayInfo.actStatus.equals("3")){
	            		//3-未来有活动（空心蓝杠）
	            		holder.dot.setVisibility(View.VISIBLE);
	            		holder.dot.setBackgroundResource(R.drawable.home_page_blank_white);
	            	}
	        		
	        	}
	        }
        }
        
        
//        if(data !=null && data.daysInfo !=null && data.daysInfo.size()>position){
//        	//TODO
//        	JournalDayInfo journalDayInfo = data.daysInfo.get(position);
//        	if(journalDayInfo.unread.equals("1")){
//        		//未读
//        		holder.icon.setVisibility(View.VISIBLE);
//        		holder.icon.setText("2");
//        	}
//        	if(journalDayInfo.actStatus.equals("1")){
//        		//未参加 红杠
//        		holder.dot.setBackgroundColor(Color.RED);
//        	}else if(journalDayInfo.actStatus.equals("2")){
//        		//2-已参加（蓝杠）
//        		holder.dot.setBackgroundColor(Color.BLUE);
//        	}else{
//        		//3-未来有活动（空心蓝杠）
//        		holder.dot.setBackgroundResource(R.drawable.home_page_blank_white);
//        	}
//        }
        
        if(currentFlag == position){ 
        	//设置当天的背景
        	holder.backgroundImage.setVisibility(View.VISIBLE);
        	holder.backgroundImage.setBackgroundResource(R.drawable.calendar_date_focused);
        	holder.tView.setTextColor(Color.WHITE);
        	holder.dot.setVisibility(View.INVISIBLE);
        	holder.icon.setVisibility(View.INVISIBLE);
        	
        }else{
        	holder.backgroundImage.setVisibility(View.INVISIBLE);
        	holder.tView.setTextColor(Color.parseColor("#3e3e3e"));
        	holder.main.setBackgroundColor(context.getResources().getColor(R.color.calendar_bg2));
        }
        if(position < dayOfWeek || position >= daysOfMonth + dayOfWeek){
        	//设置上一月和下一月的背景
        	holder.tView.setTextColor(Color.parseColor("#bdbdbd"));
        }
        
        return convertView;
    }
    
    //得到某年的某月的天数且这月的第一天是星期几
    public void getCalendar(int year, int month){
        isLeapyear = sc.isLeapYear(year);              //是否为闰年
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month);  //某月的总天数
        dayOfWeek = sc.getWeekdayOfMonth(year, month);      //某月第一天为星期几
        lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month-1);  //上一个月的总天数
        getweek(year,month);
    }
    private ArrayList<ScheduleDateTag> getTagDate(int year, int month){
        ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>(){
            private static final long serialVersionUID = -5976649074350323408L;};
        int i = 0;
        while(i < 10){
            int tagID = i;
            int year1 = 2012;
            int month1 = 11;
            int day = 2*(i);
            int scheduleID = i;
            ScheduleDateTag dateTag = new ScheduleDateTag(tagID,year1,month1,day,scheduleID);
            dateTagList.add(dateTag);
            i++;
            }
        if(dateTagList != null && dateTagList.size() > 0){
            return dateTagList;
        }
        return null;
    }
    
    //将一个月中的每一天的值添加入数组dayNuber中
    private void getweek(int year, int month) {
        int j = 1;
        int flag = 0;
        
        //得到当前月的所有日程日期(这些日期需要标记)
//        dao = new ScheduleDAO(context);
        ArrayList<ScheduleDateTag> dateTagList = this.getTagDate(year,month);
        if(dateTagList != null && dateTagList.size() > 0){
            schDateTagFlag = new int[dateTagList.size()];
        }
        
        for (int i = 0; i < dayNumber.length; i++) {
            // 周一
            if(i < dayOfWeek){  //前一个月
                int temp = lastDaysOfMonth - dayOfWeek+1;
                dayNumber[i] = (temp + i)+".";
            }else if(i < daysOfMonth + dayOfWeek){   //本月
                String day = String.valueOf(i-dayOfWeek+1);   //得到的日期
                dayNumber[i] = i-dayOfWeek+1+".";
                //对于当前月才去标记当前日期
                if(sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)){
                    //笔记当前日期
                    currentFlag = i;
                }
                //标记日程日期
                if(dateTagList != null && dateTagList.size() > 0){
                    for(int m = 0; m < dateTagList.size(); m++){
                        ScheduleDateTag dateTag = dateTagList.get(m);
                        int matchYear = dateTag.getYear();
                        int matchMonth = dateTag.getMonth();
                        int matchDay = dateTag.getDay();
                        if(matchYear == year && matchMonth == month && matchDay == Integer.parseInt(day)){
                            schDateTagFlag[flag] = i;
                            flag++;
                        }
                    }
                    
                }
                setShowYear(String.valueOf(year));
                setShowMonth(String.valueOf(month));
            }else{   //下一个月
                dayNumber[i] = j+".";
                j++;
            }
        }
        
        
//        dayList = new ArrayList<String>();
//        dayList.clear();
//        String abc = "";
//        for(int i = 0; i < dayNumber.length; i++){
//             abc = abc+dayNumber[i]+":";
//             dayList.add(dayNumber[i]);
//        }
        
//        Log.d("hef",abc);


    }
    
    
    class ViewHoler{
    	RelativeLayout main;
    	TextView tView;
    	TextView icon;
    	ImageView dot;
    	ImageView backgroundImage;
    }
    private void resetViewHolder(ViewHoler vh) {
//		vh.icon.setImageDrawable(null);
//		vh.icon.setImageDrawable(null);
	}
//    /**
//     * 得到所有的日程信息
//     */
//    public void getScheduleAll(){
////        schList = dao.getAllSchedule();
//        if(schList != null){
//            for (ScheduleVO vo : schList) {
//                String content = vo.getScheduleContent();
//                int startLine = content.indexOf("\n");
//                if(startLine > 0){
//                    content = content.substring(0, startLine)+"...";
//                }else if(content.length() > 30){
//                    content = content.substring(0, 30)+"...";
//                }
//                scheduleID = vo.getScheduleID();
//                createInfotext(scheduleInfo, scheduleID);
//            }
//        }else{
//            scheduleInfo = "没有日程";
//            createInfotext(scheduleInfo,-1);
//        }
//    }
    
    
    public int getCurrentFlag() {
		return currentFlag;
	}

	public void setCurrentFlag(int currentFlag) {
		this.currentFlag = currentFlag;
	}

	public void matchScheduleDate(int year, int month, int day){
        
    }
    
    /**
     * 点击每一个item时返回item中的日期
     * @param position
     * @return
     */
    public String getDateByClickItem(int position){
        return dayNumber[position];
    }
    
    /**
     * 在点击gridView时，得到这个月中第一天的位置
     * @return
     */
    public int getStartPositon(){
        return dayOfWeek;
    }
    
    /**
     * 在点击gridView时，得到这个月中最后一天的位置
     * @return
     */
    public int getEndPosition(){
        return  (dayOfWeek+daysOfMonth)-1;
    }
    
    public String getShowYear() {
        return showYear;
    }

    public void setShowYear(String showYear) {
        this.showYear = showYear;
    }

    public String getShowMonth() {
        return showMonth;
    }

    public void setShowMonth(String showMonth) {
        this.showMonth = showMonth;
    }
    
    public String getAnimalsYear() {
        return animalsYear;
    }

    public void setAnimalsYear(String animalsYear) {
        this.animalsYear = animalsYear;
    }
    
    public String getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(String leapMonth) {
        this.leapMonth = leapMonth;
    }
    
    public String getCyclical() {
        return cyclical;
    }

    public void setCyclical(String cyclical) {
        this.cyclical = cyclical;
    }
}