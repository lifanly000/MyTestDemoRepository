package com.example.wireframe.protocal.protocalProcess.model;

public class RankInfoRequestData {
	//排行版类型 1-全国，2-校内，3-班内，如果没有这个参数，默认按照全国排序
	public String type = "";
	//页面编码，第一页写1，从1开始
	public String pageNo = ""; 
	//页面容量
	public String pageSize = ""; 
}
