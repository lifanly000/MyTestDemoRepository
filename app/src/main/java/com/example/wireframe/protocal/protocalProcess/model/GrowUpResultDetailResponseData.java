package com.example.wireframe.protocal.protocalProcess.model;


public class GrowUpResultDetailResponseData {
	public ResponseCommonData commonData = new ResponseCommonData();
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
		//// 【String】内嵌网页URL地址，details暂时废弃
		public String page = "";
}
