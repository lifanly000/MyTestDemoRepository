package com.example.wireframe.protocal;

public interface ProtocalEngineObserver 
{
	/*
	 * 成功
	 * */
	public void OnProtocalFinished(Object obj);
	
	public static final int ERROR_REQUEST 		= 0x01;
	public static final int ERROR_NETWORK 		= 0x02;
	public static final int ERROR_PARSE_DATA 	= 0x04;
	public static final int ERROR_NO_DATA 		= 0x08;
	
	/*
	 * 异常
	 * errorCode(int)
	 * */
	public void OnProtocalError(int errorCode);
	
	/*
	 * 过程
	 * */
	public void OnProtocalProcess(Object obj);
	
}
