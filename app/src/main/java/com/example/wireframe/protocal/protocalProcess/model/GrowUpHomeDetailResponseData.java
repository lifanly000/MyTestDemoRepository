package com.example.wireframe.protocal.protocalProcess.model;

import java.util.ArrayList;

public class GrowUpHomeDetailResponseData {
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
		//详情
		public ArrayList<ReportDetail> details = new ArrayList<ReportDetail>();
		//// 【String】内嵌网页URL地址，details暂时废弃
		public String page = "";
		//评论列表网页地址
		public String cmtPage = "";
}
