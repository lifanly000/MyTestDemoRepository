package com.example.wireframe.protocal.protocalProcess.model;

import java.util.ArrayList;

public class MyCourseOfflineBuyInfoResponseData {
	public ResponseCommonData commonData = new ResponseCommonData();
	//是否购买过，0-不是，1-是，购买过和非购买过显示界面有所区别
	public String isPurchased = "";
	//课程名
	public String name = "";
	//老师名字
	public String teacher = "";
	//总课时
	public String totalSchedule = "";
	//地区名字
	public String area = "";
	//校区名字
	public String school = "";
	//学员名字
	public String student = "";
	//学员联系方式
	public String studentTel = "";
	//课程信息
	public ArrayList<CourseTimeInfo> scheduleList = new ArrayList<CourseTimeInfo>();
	//学费
	public String tuition = "";
	//有效日期
	public String date = "";
	//客服电话
	public String serviceTel = "";
}
