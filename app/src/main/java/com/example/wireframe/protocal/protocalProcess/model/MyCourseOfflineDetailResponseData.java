package com.example.wireframe.protocal.protocalProcess.model;

import java.util.ArrayList;


public class MyCourseOfflineDetailResponseData {
	public ResponseCommonData commonData = new ResponseCommonData();
		//标题
		public String title = "";
		//总学时
		public String totalSchedule = "";
		//课程简介URL地址
		public String introduce = "";
		//主讲老师
		public String teacher = "";
		//地区
		public String region = "";
		//学区
		public String school = "";
		//学员名字
		public String student = "";
		//学员联系方式-电话
		public String studentTel = "";
		//客服电话
		public String serviceTel = "";
		//课程
		public ArrayList<ScheduleDetail> schedules = new ArrayList<ScheduleDetail>();
}
