package com.example.wireframe.protocal.protocalProcess.model;

public class ZanAndCommentRequestData {
	//E-秀秀文章ID
	public String eShowId = ""; 
	//操作类型，1-赞，2-评论
	public String mode = ""; 
	//被评论人名字,mode=2时有效
	public String to = ""; 
	//评论内容,mode=2时有效
	public String content = ""; 
}
