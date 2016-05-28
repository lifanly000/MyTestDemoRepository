package com.example.wireframe.protocal.protocalProcess.model;

import java.util.ArrayList;

public class GrowUpNumberResponseData {
	public ResponseCommonData commonData = new ResponseCommonData();
		//成长值
		public String amount = "";
		//任务列表,onlyAmount为1时不返回
		public ArrayList<Task> tasks = new ArrayList<Task>();
}
