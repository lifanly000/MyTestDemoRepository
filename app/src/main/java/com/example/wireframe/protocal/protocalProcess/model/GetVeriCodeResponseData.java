package com.example.wireframe.protocal.protocalProcess.model;

import java.util.ArrayList;

public class GetVeriCodeResponseData {
	public ResponseCommonData commonData = new ResponseCommonData();
	//手机短信验证码，用于客户端本地直接校验
	public String code = "";
}
