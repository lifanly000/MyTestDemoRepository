package com.example.wireframe.protocal.protocalProcess.model;

import java.util.ArrayList;


public class UpdateResponseData {
	public ResponseCommonData commonData = new ResponseCommonData();
	
	//版本号，如果没有更新，字段为""
	public String version ="";
	//包体积，如果没有更新，字段为""
	public String size ="";
	//更新名字，如果没有更新，字段为""
	public String name ="";
	//更新描述，如果没有更新，字段为""
	public String downloadPrompt ="";
	//下载链接，如果没有更新，字段为""
	public String downloadUrl ="";
	
}
