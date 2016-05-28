package com.example.wireframe.protocal.protocalProcess.model;

import java.util.ArrayList;

public class JournalDayInfo {
	//日（包括本月和上月、下月）
	public String day = "";
	//是否未读,0-已读，1-未读（红圈）
	public String unread = "";
	//是否当天,0-不是，1-是（篮圈）
	public String today = "";
	//1-未参加(红杠),2-已参加（蓝杠），3-未来有活动（空心蓝杠）
	public String actStatus = "";
	//报告概要信息列表，0-2条，最多2条里1条线上1条线下
	public ArrayList<Works> works = new ArrayList<Works>();
}
