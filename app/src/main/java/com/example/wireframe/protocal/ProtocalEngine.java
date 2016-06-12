package com.example.wireframe.protocal;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.wireframe.protocal.protocalProcess.ProtocalConstrustor;
import com.example.wireframe.protocal.protocalProcess.ProtocalParser;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeWorkRequestData;

import java.util.HashMap;
import java.util.Map;

public class ProtocalEngine {

	// 当前请求计划
	private int curSchema;
	
	// 当前请求地址
	private String requestUrl = "";
	
	// 当前请求发送的数据
	private String sendDataStr = "";
	
	// 当前请求的缓存文件名
	private String md5FileName = "";
	
	// volley请求队列
	private RequestQueue mRequestQueue = null;
	
	private StringRequest mStringRequest = null;
	
	private Context context = null;
	
	// 回调接口
	private ProtocalEngineObserver observer = null;
	
	public ProtocalEngine(Context context)
	{
		Logger.log("ProtocalEngine", "ProtocalEngine - in");

		this.context = context;
		
		if(mRequestQueue==null){
			mRequestQueue = RequestManager.getRequestQueue();
		}
		
		Logger.log("ProtocalEngine", "ProtocalEngine - out");
	}
	
	public void setObserver(ProtocalEngineObserver observer) 
	{
		Logger.log("ProtocalEngine", "setObserver - in");
	
		this.observer = observer;
		
		Logger.log("ProtocalEngine", "setObserver - out");
	}
	
	
	public void startRequest(int schema, Object requestObj) 
	{
		Logger.log("ProtocalEngine", "startRequest - in");
		
		curSchema = schema;
		
		switch(schema)
		{
		case SchemaDef.JOURNAL_HOME_PAGE:
			requestJournalHomePageData(requestObj);
			break;
		case SchemaDef.JOURNAL_HOME_DETAIL:
			requestJournalHomeDetailData(requestObj);
			break;
		case SchemaDef.JOURNAL_HOMEWORK_ANSWER:
			requestJournalHomeWorkData(requestObj);
			break;
		case SchemaDef.GROW_UP_HOME:
			requestGrowUpHomeData(requestObj);
			break;
		case SchemaDef.GROW_UP_HOME_DETAIL:
			requestGrowUpHomeDetailData(requestObj);
			break;
		case SchemaDef.GROW_UP_RESULT_DETAIL:
			requestGrowUpResultDetailData(requestObj);
			break;
		case SchemaDef.GET_VERI_CODE:
			requestGetVeriCodeData(requestObj);
			break;
		case SchemaDef.RESET_PASSWORD:
			requestResetPasswordData(requestObj);
			break;
		case SchemaDef.USER_LOGIN:
			requestUserLoginData(requestObj);
			break;
		case SchemaDef.USER_REGISTER:
			requestUserRegisterData(requestObj);
			break;
		case SchemaDef.FIND_PASSWORD:
			requestFindPasswordData(requestObj);
			break;
		case SchemaDef.GROW_UP_NUMBER:
			requestGrowUpNumberData(requestObj);
			break;
		case SchemaDef.MY_COURSE_ONLINE:
			requestMyCourseOnlineData(requestObj);
			break;
		case SchemaDef.MY_COURSE_OFFLINE:
			requestMyCourseOfflineData(requestObj);
			break;
		case SchemaDef.MY_COURSE_OFFLINE_BUY_INFO:
			requestMyCourseOfflineBuyInfoData(requestObj);
			break;
		case SchemaDef.MY_COURSE_ONLINE_BUY_INFO:
			requestMyCourseOnlineBuyInfoData(requestObj);
			break;
		case SchemaDef.MY_BUY_COURSE_ONLINE_LIST:
			requestMyBuyCourseOnlineData(requestObj);
			break;
		case SchemaDef.MY_BUY_COURSE_OFFLINE_LIST:
			requestMyBuyCourseOfflineData(requestObj);
			break;
		case SchemaDef.MY_COURSE_OFFLINE_DETAIL:
			requestMyBuyCourseOfflineDetailData(requestObj);
			break;
		case SchemaDef.MY_COURSE_ONLINE_DETAIL:
			requestMyBuyCourseOnlineDetailData(requestObj);
			break;
		case SchemaDef.MY_COURSE_OFFLINE_NOT_MEMBER_TYR_USE:
			requestMyCourseOfflineNotMemberTryUseData(requestObj);
			break;
		case SchemaDef.MY_COURSE_OFFLINE_NOT_MEMBER_USE:
			requestMyCourseOfflineNotMemberUseData(requestObj);
			break;
		case SchemaDef.E_XIUXIU_HOMEPAGE:
			requestEXiuxiuHomePageData(requestObj);
			break;
		case SchemaDef.E_XIUXIU_HOMEPAGE_DETAIL:
			requestEXiuxiuHomePageDetailData(requestObj);
			break;
		case SchemaDef.USER_INFO:
			requestUserInfoData(requestObj);
			break;
		case SchemaDef.UPDATE_INFO:
			requestUpdateData(requestObj);
			break;
		case SchemaDef.FEED_BACK_INFO:
			requestFeedBackData(requestObj);
			break;
		case SchemaDef.ZAN_COMMENT:
			requestZanAndCommentData(requestObj);
			break;
		case SchemaDef.RANK_INFO:
			requestRankInfoData(requestObj);
			break;
		case SchemaDef.PAY_INFO:
			requestPayInfoData(requestObj);
			break;
		case SchemaDef.SHARE_RESULT:
			requestShareReult(requestObj);
			break;
		default:
			break;
		}
		
		Logger.log("ProtocalEngine", "startRequest - out");
	}
	
	// public void cancelRequest();
	
	// 请求学习日志首页数据
	void requestJournalHomePageData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructJournalHomePageRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
				
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	
	String ConstructJournalHomePageRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructJournalHomePageRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	
	void requestJournalHomeDetailData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructJournalHomeDetailRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructJournalHomeDetailRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructJournalHomeDetailRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestGrowUpHomeData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructGrowUpHomeRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructGrowUpHomeRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructGrowUpHomeRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestGrowUpNumberData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructGrowUpNumberRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructGrowUpNumberRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructGrowUpNumberRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestMyCourseOnlineData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructMyCourseOnlineRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructMyCourseOnlineRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructMyCourseOnlineRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestMyBuyCourseOnlineData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructMyBuyCourseOnlineRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructMyBuyCourseOnlineRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructMyBuyCourseOnlineRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestMyBuyCourseOfflineDetailData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructMyBuyCourseOfflineDetailRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructMyBuyCourseOfflineDetailRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructMyBuyCourseOfflineDetailRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestMyBuyCourseOnlineDetailData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructMyBuyCourseOnlineDetailRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructMyBuyCourseOnlineDetailRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructMyBuyCourseOnlineDetailRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestMyCourseOfflineNotMemberTryUseData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructMyCourseOfflineNotMemberTryUseRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructMyCourseOfflineNotMemberTryUseRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructMyCourseOfflineNotMemberTryUseRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestMyCourseOfflineNotMemberUseData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructMyCourseOfflineNotMemberUseRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructMyCourseOfflineNotMemberUseRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructMyCourseOfflineNotMemberUseRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestEXiuxiuHomePageData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructEXiuxiuHomePageRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructEXiuxiuHomePageRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructEXiuxiuHomePageRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestEXiuxiuHomePageDetailData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructEXiuxiuHomePageDetailRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructEXiuxiuHomePageDetailRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructEXiuxiuHomeDetailPageRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestUserInfoData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructUserInfoRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructUserInfoRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructUserInfoRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestUpdateData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructUpdateRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructUpdateRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructUpdateRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestFeedBackData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructFeedBackRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructFeedBackRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructFeedBackData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestZanAndCommentData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructZanAndCommentRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructZanAndCommentRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructZanAndCommentData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestRankInfoData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructRankInfoRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructRankInfoRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructRankInfoData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestPayInfoData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructPayRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructPayRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructPayData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestShareReult(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");

		requestUrl = UrlDefine.getZhangyuUrl();

		String data = ConstructShareResultRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}

		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructShareResultRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructShareResult(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestMyBuyCourseOfflineData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructMyBuyCourseOfflineRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructMyBuyCourseOfflineRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructMyBuyCourseOfflineRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestMyCourseOfflineData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructMyCourseOfflineRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructMyCourseOfflineRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructMyCourseOfflineRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestMyCourseOfflineBuyInfoData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructMyCourseOfflineBuyInfoRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructMyCourseOfflineBuyInfoRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructMyCourseOfflineBuyInfoRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestMyCourseOnlineBuyInfoData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructMyCourseOnlineBuyInfoRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructMyCourseOnlineBuyInfoRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructMyCourseOnlineBuyInfoRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestGrowUpHomeDetailData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructGrowUpHomeDetailRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructGrowUpHomeDetailRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructGrowUpHomeDetailRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestGrowUpResultDetailData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructGrowUpResultDetailRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructGrowUpResultDetailRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructGrowUpResultDetailRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestGetVeriCodeData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructGetVeriCodeRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructGetVeriCodeRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructGetVeriCodeRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestResetPasswordData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructResetPasswordRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructResetPasswordRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructResetPasswordRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestUserLoginData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructUserLoginRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructUserLoginRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructUserLoginRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestUserRegisterData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructUserRegisterRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructUserRegisterRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructUserRegisterRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	void requestFindPasswordData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		
		requestUrl = UrlDefine.getZhangyuUrl();
		
		String data = ConstructFindPasswordRequestData(requestObj);
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	String ConstructFindPasswordRequestData(Object requestObj)
	{
		try{
			return ProtocalConstrustor.ConstructFindPasswordRequestData(requestObj,context);
		}catch(Exception e){
			return null;
		}
	}
	//提交作业语音
	void requestJournalHomeWorkData(Object requestObj)
	{
		Logger.log("ProtocalEngine", "requestMainPageData - in");
		JournalHomeWorkRequestData requestData = (JournalHomeWorkRequestData)requestObj;
		requestUrl = UrlDefine.getZhangyuUrl()+"workId="+requestData.workId+"&lens="+requestData.lens;
		
		String data = requestData.data;
		
		if(data != null)
		{
			sendDataStr = data;
			startHttpRequestPost(sendDataStr);
		}
		else
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_REQUEST);
		}
		
		Logger.log("ProtocalEngine", "requestMainPageData - out");
	}
	
	////////////////////////////////////////////////////////////////////
	void startHttpRequestGet(String url)
	{
		Logger.log("ProtocalEngine", "startHttpRequestGet - in");
		
        // 2 创建StringRequest对象
        mStringRequest = new StringRequest(url,
	        new Response.Listener<String>() {
	            @Override
	            public void onResponse(String response) {
	                // System.out.println("请求结果:" + response);
	            	requestFinished(response);
	            }
	        }, new Response.ErrorListener() {
	            @Override
	            public void onErrorResponse(VolleyError error) {
	                // System.out.println("请求错误:" + error.toString());
	            	requestFailed(error.toString());
	            }
	        });
        // 3 将StringRequest添加到RequestQueue
        mRequestQueue.add(mStringRequest);
        
		Logger.log("ProtocalEngine", "startHttpRequestGet - out");
	}
	
	void startHttpRequestPost(String data)
	{
		Logger.log("ProtocalEngine", "startHttpRequestPost - in");
		
        mStringRequest = new StringRequest(Method.POST,                 
    		requestUrl,                
    		new Response.Listener<String>() {                    
    			@Override                    
    			public void onResponse(String response) { 
    				// 请求成功处理
    				// System.out.println("请求结果:" + response); 
    				requestFinished(response);
    				}                
    			},                 
			new Response.ErrorListener() {                    
				@Override                    
				public void onErrorResponse(VolleyError error) { 
					// 请求失败处理
					// System.out.println("请求错误:" + error.toString());   
					requestFailed(error.toString());
					}                
				}) 
        {            
            @Override
            protected Map<String,String> getParams() {
            	// System.out.println("请求上行数据:" + sendDataStr); 
                Map<String,String> params = new HashMap<String, String>();      
                params.put("transMessage", sendDataStr);
                return params;
            }
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                // params.put("Content-Type","application/x-www-form-urlencoded");
//                return params;
//        	}
        };
        
        RetryPolicy retryPolicy = new DefaultRetryPolicy(15000/*15秒*/, 
        		// DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        		0,
        		DefaultRetryPolicy.DEFAULT_BACKOFF_MULT); 
        mStringRequest.setRetryPolicy(retryPolicy);
        mRequestQueue.add(mStringRequest);
        
        Logger.log("ProtocalEngine", "startHttpRequestPost - out");
	}
	
	
	void requestFinished(String responseData)
	{
		Log.i("ProtocalEngine", responseData);
		
		if(responseData == null || responseData.length() == 0)
		{
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_NO_DATA);
		}
		try{
			switch(curSchema)
			{
			case SchemaDef.JOURNAL_HOME_PAGE:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalHomePageResponseData(context,responseData));
				break;
			case SchemaDef.JOURNAL_HOME_DETAIL:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalHomeDetailResponseData(context,responseData));
				break;
			case SchemaDef.JOURNAL_HOMEWORK_ANSWER:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalHomeWorkResponseData(context,responseData));
				break;
			case SchemaDef.GROW_UP_HOME:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalGrowUpResponseData(context,responseData));
				break;
			case SchemaDef.GROW_UP_HOME_DETAIL:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalGrowUpDetailResponseData(context,responseData));
				break;
			case SchemaDef.GROW_UP_RESULT_DETAIL:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalGrowUpResultDetailResponseData(context,responseData));
				break;
			case SchemaDef.GET_VERI_CODE:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalGetVeriCodeResponseData(context,responseData));
				break;
			case SchemaDef.RESET_PASSWORD:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalResetPasswordResponseData(context,responseData));
				break;
			case SchemaDef.USER_LOGIN:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalUserLoginResponseData(context,responseData));
				break;
			case SchemaDef.USER_REGISTER:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalUserRegisterResponseData(context,responseData));
				break;
			case SchemaDef.FIND_PASSWORD:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalFindPasswordResponseData(context,responseData));
				break;
			case SchemaDef.GROW_UP_NUMBER:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalGrowUpNumberResponseData(context,responseData));
				break;
			case SchemaDef.MY_COURSE_ONLINE:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalMyCourseOnlineResponseData(context,responseData));
				break;
			case SchemaDef.MY_COURSE_ONLINE_BUY_INFO:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalMyCourseOnlineBuyInfoResponseData(context,responseData));
				break;
			case SchemaDef.MY_COURSE_OFFLINE:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalMyCourseOfflineResponseData(context,responseData));
				break;
			case SchemaDef.MY_COURSE_OFFLINE_BUY_INFO:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalMyCourseOfflineBuyInfoResponseData(context,responseData));
				break;
			case SchemaDef.MY_BUY_COURSE_ONLINE_LIST:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalMyBuyCourseOnlineResponseData(context,responseData));
				break;
			case SchemaDef.MY_BUY_COURSE_OFFLINE_LIST:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalMyBuyCourseOfflineResponseData(context,responseData));
				break;
			case SchemaDef.MY_COURSE_OFFLINE_DETAIL:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalMyBuyCourseOfflineDetailResponseData(context,responseData));
				break;
			case SchemaDef.MY_COURSE_ONLINE_DETAIL:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalMyBuyCourseOnlineDetailResponseData(context,responseData));
				break;
			case SchemaDef.MY_COURSE_OFFLINE_NOT_MEMBER_TYR_USE:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalMyCourseOfflineNotMemberTryUseResponseData(context,responseData));
				break;
			case SchemaDef.MY_COURSE_OFFLINE_NOT_MEMBER_USE:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalMyCourseOfflineNotMemberUseResponseData(context,responseData));
				break;
			case SchemaDef.E_XIUXIU_HOMEPAGE:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalEXiuxiuHomePageResponseData(context,responseData));
				break;
			case SchemaDef.E_XIUXIU_HOMEPAGE_DETAIL:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalEXiuxiuHomePageDetailsResponseData(context,responseData));
				break;
			case SchemaDef.USER_INFO:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalUserInfoResponseData(context,responseData));
				break;
			case SchemaDef.UPDATE_INFO:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalUpdateResponseData(context,responseData));
				break;
			case SchemaDef.FEED_BACK_INFO:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalFeedBackInfoData(context,responseData));
				break;
			case SchemaDef.ZAN_COMMENT:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalZanAndCommentData(context,responseData));
				break;
			case SchemaDef.RANK_INFO:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalRankInfoData(context,responseData));
				break;
			case SchemaDef.PAY_INFO:
				observer.OnProtocalFinished(ProtocalParser.ParseJouanalPayInfoData(context,responseData));
				break;
			case SchemaDef.SHARE_RESULT:
				observer.OnProtocalFinished(ProtocalParser.ParseShareResultData(context,responseData));
				break;
			default:
				break;
			}
		}catch(Exception e){
			if(observer != null)
				observer.OnProtocalError(ProtocalEngineObserver.ERROR_PARSE_DATA);
		}
		
		Logger.log("ProtocalEngine", "requestFinished - out");
	}
	
	void requestFailed(String error)
	{
		Logger.log("ProtocalEngine", "requestFailed - in");
		
		if(observer != null)
			observer.OnProtocalError(ProtocalEngineObserver.ERROR_NETWORK);
		
		Logger.log("ProtocalEngine", "requestFailed - out");
	}
	
}


