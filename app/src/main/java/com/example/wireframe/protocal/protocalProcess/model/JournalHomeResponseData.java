package com.example.wireframe.protocal.protocalProcess.model;

import java.util.ArrayList;

public class JournalHomeResponseData {
	public ResponseCommonData commonData = new ResponseCommonData();
	//年月
	public String yearMonth = "";
	//日列表
	public ArrayList<JournalDayInfo> daysInfo = new ArrayList<JournalDayInfo>();
}
