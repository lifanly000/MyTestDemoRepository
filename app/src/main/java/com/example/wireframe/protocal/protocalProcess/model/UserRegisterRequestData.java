package com.example.wireframe.protocal.protocalProcess.model;

public class UserRegisterRequestData {
	//账户名，可为空
	public String accountName = ""; 
	//账户密码
	public String pwd = ""; 
	//手机号，不可为空
	public String phone = ""; 
	//短信验证码
	public String code = ""; 
	//所在地区编码，精确到区
	public String cityCode = ""; 
}
