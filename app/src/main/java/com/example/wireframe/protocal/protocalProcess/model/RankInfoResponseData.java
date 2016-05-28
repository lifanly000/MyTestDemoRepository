package com.example.wireframe.protocal.protocalProcess.model;

import java.util.ArrayList;

public class RankInfoResponseData {
	public ResponseCommonData commonData = new ResponseCommonData();
	//入榜总人数
	public String totolCount = "" ;
	//我的排名
	public String selfRank = "";
	
	public ArrayList<RankItem> items = new ArrayList<RankItem>(3);
}
