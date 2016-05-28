package com.example.wireframe.protocal.protocalProcess.model;

import java.util.ArrayList;

public class MyCourseOnlineBuyInfoResponseData {
	public ResponseCommonData commonData = new ResponseCommonData();
	//是否为会员，0-不是，1-是，会员和非会员显示界面有所区别
	public String isMember = "";
	//标题
	public String title = "";
	//发布人头像
	public String icon = "";
	//发布人名字
	public String publisherName = "";
	//发布人身份
	public String publisherType = "";
	//发布时间
	public String publishTime = "";
	//课程详情网页url
	public String detail = "";
}
