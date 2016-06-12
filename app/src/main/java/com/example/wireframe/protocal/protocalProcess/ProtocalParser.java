package com.example.wireframe.protocal.protocalProcess;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.wireframe.protocal.protocalProcess.model.Article;
import com.example.wireframe.protocal.protocalProcess.model.Banner;
import com.example.wireframe.protocal.protocalProcess.model.Comment;
import com.example.wireframe.protocal.protocalProcess.model.Course;
import com.example.wireframe.protocal.protocalProcess.model.CourseTimeInfo;
import com.example.wireframe.protocal.protocalProcess.model.DialogInfo;
import com.example.wireframe.protocal.protocalProcess.model.EXiuxiuHomePageDetailResponseData;
import com.example.wireframe.protocal.protocalProcess.model.EXiuxiuHomePageResponseData;
import com.example.wireframe.protocal.protocalProcess.model.FeedBackResponseData;
import com.example.wireframe.protocal.protocalProcess.model.FindPasswordResponseData;
import com.example.wireframe.protocal.protocalProcess.model.GetVeriCodeResponseData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeDetailResponseData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeResponseData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberResponseData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpResultDetailResponseData;
import com.example.wireframe.protocal.protocalProcess.model.JournalDayInfo;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeDetailResponseData;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeResponseData;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeWorkResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyBuyCourseOnlineResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineBuyInfoResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineDetailResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineNotMemberTryUseResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineNotMemberUseResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineBuyInfoResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineDetailResponseData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineResponseData;
import com.example.wireframe.protocal.protocalProcess.model.PayResponseData;
import com.example.wireframe.protocal.protocalProcess.model.RankInfoResponseData;
import com.example.wireframe.protocal.protocalProcess.model.RankItem;
import com.example.wireframe.protocal.protocalProcess.model.ReportDetail;
import com.example.wireframe.protocal.protocalProcess.model.ReportInfo;
import com.example.wireframe.protocal.protocalProcess.model.ResetPasswordResponseData;
import com.example.wireframe.protocal.protocalProcess.model.ResponseCommonData;
import com.example.wireframe.protocal.protocalProcess.model.ScheduleDetail;
import com.example.wireframe.protocal.protocalProcess.model.Task;
import com.example.wireframe.protocal.protocalProcess.model.UpdateResponseData;
import com.example.wireframe.protocal.protocalProcess.model.UserInfoResponseData;
import com.example.wireframe.protocal.protocalProcess.model.UserLoginResponseData;
import com.example.wireframe.protocal.protocalProcess.model.UserRegisterResponseData;
import com.example.wireframe.protocal.protocalProcess.model.Works;
import com.example.wireframe.protocal.protocalProcess.model.ZanAndCommentResponseData;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProtocalParser {

    static final String USERINFO_PREFERENCES_KEY = "UserInfo";

    static public SharedPreferences getShareUserPreferences(Context context) {
        return context.getSharedPreferences(USERINFO_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    static public void WriteUserInfo(Context context, String uid, String jsessionId) {
        SharedPreferences userPreferences = getShareUserPreferences(context);
        Editor editor = userPreferences.edit();

        editor.putString("uid", uid);
        editor.putString("jsessionId", jsessionId);

        editor.commit();
    }

    private static void ParseCommonData(Context context, JSONObject jsonRoot, ResponseCommonData commonData)
            throws Exception {
	   if(jsonRoot.has("busiCode")){
		   commonData.busiCode = jsonRoot.getString("busiCode");
	   }
	   if(jsonRoot.has("serverTime")){
		   commonData.serverTime = jsonRoot.getString("serverTime");
	   }

        if(jsonRoot.has("userInfo")){
	        JSONObject jsonUserInfo = jsonRoot.getJSONObject("userInfo");
	        if(jsonUserInfo !=null && jsonUserInfo.length()>0){
	        	if(jsonUserInfo.has("uid")){
	        		commonData.userInfo_uid = jsonUserInfo.getString("uid");
	        	}
		        if(jsonUserInfo.getString("jsessionId").length()>0){
		        	commonData.userInfo_jsessionId = jsonUserInfo.getString("jsessionId");
		        }
		        WriteUserInfo(context, commonData.userInfo_uid, commonData.userInfo_jsessionId);
	        }
        }
        if(jsonRoot.has("result")){
	        JSONObject jsonResult = jsonRoot.getJSONObject("result");
	        commonData.result_code = jsonResult.getString("code");
	        commonData.result_msg = jsonResult.getString("msg");
	        commonData.result_url = jsonResult.getString("url");
        }
    }

    public static Object ParseJouanalHomePageResponseData(Context context, String responseData) throws Exception {
        JSONObject jsonRoot = new JSONObject(responseData);
        JournalHomeResponseData repdata = new JournalHomeResponseData();

        ParseCommonData(context, jsonRoot, repdata.commonData);
        if (!repdata.commonData.result_code.equals("0000") && 
        	!repdata.commonData.result_code.equals("0")) {
            return repdata;
        }

        if (jsonRoot.has("responseData")) {
            JSONObject jResData = jsonRoot.getJSONObject("responseData");
            if(jResData.has("yearMonth")){
            	repdata.yearMonth = jResData.getString("yearMonth");
            }
            if(jResData.has("days")){
            	JSONArray jDayData = jResData.getJSONArray("days");
            	if (jDayData != null) {
                    for (int j01 = 0; j01 < jDayData.length(); j01++) {
                    	JSONObject jInfo = jDayData.getJSONObject(j01);
                    	JournalDayInfo info = new JournalDayInfo();
                    	info.day = JsonParserUtil.parseStr(jInfo, "day", "");
                    	info.unread = JsonParserUtil.parseStr(jInfo, "unread", "");
                    	info.today = JsonParserUtil.parseStr(jInfo, "today", "");
                    	info.actStatus = JsonParserUtil.parseStr(jInfo, "actStatus", "");
                    	if(jInfo.has("works")){
                    		JSONArray jWorksData = jInfo.getJSONArray("works");
                    		if(jWorksData !=null){
                    			for(int j02=0;j02<jWorksData.length();j02++){
                    				JSONObject jInfo2 = jWorksData.getJSONObject(j02);
                    				Works workInfo = new Works();
                    				workInfo.workId = JsonParserUtil.parseStr(jInfo2, "workId", "");
                    				workInfo.type = JsonParserUtil.parseStr(jInfo2, "type", "");
                    				workInfo.date = JsonParserUtil.parseStr(jInfo2, "date", "");
                    				workInfo.title = JsonParserUtil.parseStr(jInfo2, "title", "");
                    				workInfo.summary = JsonParserUtil.parseStr(jInfo2, "summary", "");
                    				workInfo.unread = JsonParserUtil.parseStr(jInfo2, "unread", "");
                    				info.works.add(workInfo);
                    			}
                    		}
                    	}
                    	repdata.daysInfo.add(info);
                    }
            	}
            	
            }
        }


        return repdata;
    }
    
    public static Object ParseJouanalHomeDetailResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	JournalHomeDetailResponseData repdata = new JournalHomeDetailResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		repdata.date = jResData.getString("date");
    		repdata.title = jResData.getString("title");
    		repdata.icon = jResData.getString("icon");
    		repdata.publisherName = jResData.getString("publisherName");
    		repdata.publisherType = jResData.getString("publisherType");
    		repdata.publishTime = jResData.getString("publishTime");
    		repdata.page = jResData.getString("page");
    		if(jResData.has("dutyDescription")){
    			repdata.dutyDescription = jResData.getString("dutyDescription");
    		}
    		if(jResData.has("dutyVoiceUrl")){
    			repdata.dutyVoiceUrl = jResData.getString("dutyVoiceUrl");
    		}
    		
    		if(jResData.has("details")){
    			JSONArray jDetails = jResData.getJSONArray("details");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					ReportDetail info = new ReportDetail();
    					info.content = JsonParserUtil.parseStr(jInfo, "content", "");
    					info.imageUrl = JsonParserUtil.parseStr(jInfo, "imageUrl", "");
    					info.type = JsonParserUtil.parseStr(jInfo, "type", "");
    					info.videoUrl = JsonParserUtil.parseStr(jInfo, "videoUrl", "");
    					info.videoWord = JsonParserUtil.parseStr(jInfo, "videoWord", "");
    					repdata.details.add(info);
    				}
    			}
    		}
    		
    		if(jResData.has("dialogs")){
    			JSONArray jDetails = jResData.getJSONArray("dialogs");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					DialogInfo info = new DialogInfo();
    					info.time = JsonParserUtil.parseStr(jInfo, "date", "");
    					info.from = JsonParserUtil.parseStr(jInfo, "from", "");
    					info.type = JsonParserUtil.parseStr(jInfo, "type", "");
    					info.urlOrContent = JsonParserUtil.parseStr(jInfo, "urlOrContent", "");
    					repdata.dialogs.add(info);
    				}
    			}
    		}
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalHomeWorkResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	JournalHomeWorkResponseData repdata = new JournalHomeWorkResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		repdata.solutionId = jResData.getString("solutionId");
    	}
    	
    	return repdata;
    }
    public static Object ParseJouanalGrowUpResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	GrowUpHomeResponseData repdata = new GrowUpHomeResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		if(jResData.has("reports")){
    			JSONArray jReports = jResData.getJSONArray("reports");
    			if(jReports !=null){
    				for(int j01=0;j01<jReports.length();j01++){
    					JSONObject jInfo = jReports.getJSONObject(j01);
    					ReportInfo info = new ReportInfo();
    					info.reportType = JsonParserUtil.parseStr(jInfo, "reportType", "");
    					info.reportId = JsonParserUtil.parseStr(jInfo, "reportId", "");
    					info.title = JsonParserUtil.parseStr(jInfo, "title", "");
    					info.publishTime = JsonParserUtil.parseStr(jInfo, "publishTime", "");
    					info.imageUrl = JsonParserUtil.parseStr(jInfo, "imageUrl", "");
    					
    					repdata.reports.add(info);
    				}
    			}
    		}
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalGrowUpDetailResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	GrowUpHomeDetailResponseData repdata = new GrowUpHomeDetailResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		repdata.title =	JsonParserUtil.parseStr(jResData, "title", "");
    		repdata.icon =	JsonParserUtil.parseStr(jResData, "icon", "");
    		repdata.publisherName =	JsonParserUtil.parseStr(jResData, "publisherName", "");
    		repdata.publisherType =	JsonParserUtil.parseStr(jResData, "publisherType", "");
    		repdata.publishTime =	JsonParserUtil.parseStr(jResData, "publishTime", "");
    		repdata.page =	JsonParserUtil.parseStr(jResData, "page", "");
    		repdata.cmtPage =	JsonParserUtil.parseStr(jResData, "cmtPage", "");
    		
    		if(jResData.has("details")){
    			JSONArray jDetails = jResData.getJSONArray("details");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					ReportDetail info = new ReportDetail();
    					info.content = JsonParserUtil.parseStr(jInfo, "content", "");
    					info.imageUrl = JsonParserUtil.parseStr(jInfo, "imageUrl", "");
    					info.type = JsonParserUtil.parseStr(jInfo, "type", "");
    					info.videoUrl = JsonParserUtil.parseStr(jInfo, "videoUrl", "");
    					info.videoWord = JsonParserUtil.parseStr(jInfo, "videoWord", "");
    					repdata.details.add(info);
    				}
    			}
    		}
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalGrowUpResultDetailResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	GrowUpResultDetailResponseData repdata = new GrowUpResultDetailResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		repdata.title =	JsonParserUtil.parseStr(jResData, "title", "");
    		repdata.icon =	JsonParserUtil.parseStr(jResData, "icon", "");
    		repdata.publisherName =	JsonParserUtil.parseStr(jResData, "publisherName", "");
    		repdata.publisherType =	JsonParserUtil.parseStr(jResData, "publisherType", "");
    		repdata.publishTime =	JsonParserUtil.parseStr(jResData, "publishTime", "");
    		repdata.page =	JsonParserUtil.parseStr(jResData, "page", "");
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalGetVeriCodeResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	GetVeriCodeResponseData repdata = new GetVeriCodeResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		if(jResData.has("code")){
    			repdata.code = jResData.getString("code");
    		}
    		
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalResetPasswordResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	ResetPasswordResponseData repdata = new ResetPasswordResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	return repdata;
    }
    
    public static Object ParseJouanalUserLoginResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	UserLoginResponseData repdata = new UserLoginResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	return repdata;
    }
    
    public static Object ParseJouanalUserRegisterResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	UserRegisterResponseData repdata = new UserRegisterResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	return repdata;
    }
    
    public static Object ParseJouanalFindPasswordResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	FindPasswordResponseData repdata = new FindPasswordResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	return repdata;
    }
    
    public static Object ParseJouanalGrowUpNumberResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	GrowUpNumberResponseData repdata = new GrowUpNumberResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
	    	if(jResData.has("amount")){
	    		repdata.amount = JsonParserUtil.parseStr(jResData, "amount", "");
	    	}
	    	
	    	if(jResData.has("tasks")){
				JSONArray jDetails = jResData.getJSONArray("tasks");
				if (jDetails != null) {
					for (int j01 = 0; j01 < jDetails.length(); j01++) {
						JSONObject jInfo = jDetails.getJSONObject(j01);
						Task info = new Task();
						info.name = JsonParserUtil.parseStr(jInfo, "name", "");
						info.score = JsonParserUtil.parseStr(jInfo, "score", "");
						info.lastComplete = JsonParserUtil.parseStr(jInfo, "lastComplete", "");
						info.lastCompleteScore = JsonParserUtil.parseStr(jInfo, "lastCompleteScore", "");
						info.tag = JsonParserUtil.parseStr(jInfo, "tag", "");
						repdata.tasks.add(info);
					}
				}
			}
    	}
    	return repdata;
    }
    
    public static Object ParseJouanalMyCourseOnlineResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	MyCourseOnlineResponseData repdata = new MyCourseOnlineResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
	    	if(jResData.has("courses")){
	    		JSONArray jDetails = jResData.getJSONArray("courses");
	    		if (jDetails != null) {
	    			for (int j01 = 0; j01 < jDetails.length(); j01++) {
	    				JSONObject jInfo = jDetails.getJSONObject(j01);
	    				Course info = new Course();
	    				info.name = JsonParserUtil.parseStr(jInfo, "name", "");
	    				info.courseId = JsonParserUtil.parseStr(jInfo, "courseId", "");
	    				info.schedule = JsonParserUtil.parseStr(jInfo, "schedule", "");
	    				repdata.courses.add(info);
	    			}
	    		}
	    	}
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalMyBuyCourseOnlineResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	MyBuyCourseOnlineResponseData repdata = new MyBuyCourseOnlineResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		if(jResData.has("courses")){
    			JSONArray jDetails = jResData.getJSONArray("courses");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					Course info = new Course();
    					info.name = JsonParserUtil.parseStr(jInfo, "name", "");
    					info.courseId = JsonParserUtil.parseStr(jInfo, "courseId", "");
    					info.totalSchedule = JsonParserUtil.parseStr(jInfo, "totalSchedule", "");
    					repdata.courses.add(info);
    				}
    			}
    		}
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalMyBuyCourseOfflineResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	MyBuyCourseOnlineResponseData repdata = new MyBuyCourseOnlineResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		if(jResData.has("courses")){
    			JSONArray jDetails = jResData.getJSONArray("courses");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					Course info = new Course();
    					info.name = JsonParserUtil.parseStr(jInfo, "name", "");
    					info.courseId = JsonParserUtil.parseStr(jInfo, "courseId", "");
    					info.totalSchedule = JsonParserUtil.parseStr(jInfo, "totalSchedule", "");
    					repdata.courses.add(info);
    				}
    			}
    		}
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalMyBuyCourseOfflineDetailResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	MyCourseOfflineDetailResponseData repdata = new MyCourseOfflineDetailResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		repdata.title = JsonParserUtil.parseStr(jResData, "title", "");
    		repdata.totalSchedule = JsonParserUtil.parseStr(jResData, "totalSchedule", "");
    		repdata.introduce = JsonParserUtil.parseStr(jResData, "introduce", "");
    		repdata.teacher = JsonParserUtil.parseStr(jResData, "teacher", "");
    		repdata.region = JsonParserUtil.parseStr(jResData, "region", "");
    		repdata.school = JsonParserUtil.parseStr(jResData, "school", "");
    		repdata.student = JsonParserUtil.parseStr(jResData, "student", "");
    		repdata.studentTel = JsonParserUtil.parseStr(jResData, "studentTel", "");
    		repdata.serviceTel = JsonParserUtil.parseStr(jResData, "serviceTel", "");
    		
    		if(jResData.has("schedules")){
    			JSONArray jDetails = jResData.getJSONArray("schedules");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					ScheduleDetail info = new ScheduleDetail();
    					info.scheduleId = JsonParserUtil.parseStr(jInfo, "scheduleId", "");
    					info.schedule = JsonParserUtil.parseStr(jInfo, "schedule", "");
    					info.price = JsonParserUtil.parseStr(jInfo, "price", "");
    					info.effectiveDate = JsonParserUtil.parseStr(jInfo, "effectiveDate", "");
    					repdata.schedules.add(info);
    				}
    			}
    		}
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalMyBuyCourseOnlineDetailResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	MyCourseOnlineDetailResponseData repdata = new MyCourseOnlineDetailResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		repdata.isMember = JsonParserUtil.parseStr(jResData, "isMember", "");
    		repdata.isFree = JsonParserUtil.parseStr(jResData, "isFree", "");
    		repdata.title = JsonParserUtil.parseStr(jResData, "title", "");
    		repdata.totalSchedule = JsonParserUtil.parseStr(jResData, "totalSchedule", "");
    		repdata.teacher = JsonParserUtil.parseStr(jResData, "teacher", "");
    		repdata.introduction = JsonParserUtil.parseStr(jResData, "introduction", "");
    		repdata.price = JsonParserUtil.parseStr(jResData, "price", "");
    		repdata.serviceTel = JsonParserUtil.parseStr(jResData, "serviceTel", "");
    		
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalMyCourseOfflineNotMemberTryUseResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	MyCourseOfflineNotMemberTryUseResponseData repdata = new MyCourseOfflineNotMemberTryUseResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		repdata.isTested = JsonParserUtil.parseStr(jResData, "isTested", "");
    		repdata.title = JsonParserUtil.parseStr(jResData, "title", "");
    		repdata.icon = JsonParserUtil.parseStr(jResData, "icon", "");
    		repdata.publisherName = JsonParserUtil.parseStr(jResData, "publisherName", "");
    		repdata.publisherType = JsonParserUtil.parseStr(jResData, "publisherType", "");
    		repdata.publishTime = JsonParserUtil.parseStr(jResData, "publishTime", "");
    		repdata.testSchedule = JsonParserUtil.parseStr(jResData, "testSchedule", "");
    		repdata.detail = JsonParserUtil.parseStr(jResData, "detail", "");
    		
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalMyCourseOfflineNotMemberUseResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	MyCourseOfflineNotMemberUseResponseData repdata = new MyCourseOfflineNotMemberUseResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	return repdata;
    }
    
    public static Object ParseJouanalMyCourseOnlineBuyInfoResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	MyCourseOnlineBuyInfoResponseData repdata = new MyCourseOnlineBuyInfoResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		repdata.isMember = JsonParserUtil.parseStr(jResData, "isMember", "");
    		repdata.title = JsonParserUtil.parseStr(jResData, "title", "");
    		repdata.icon = JsonParserUtil.parseStr(jResData, "icon", "");
    		repdata.publisherName = JsonParserUtil.parseStr(jResData, "publisherName", "");
    		repdata.publisherType = JsonParserUtil.parseStr(jResData, "publisherType", "");
    		repdata.publishTime = JsonParserUtil.parseStr(jResData, "publishTime", "");
    		repdata.detail = JsonParserUtil.parseStr(jResData, "detail", "");
    	}
    	
    	return repdata;
    }
    
    
    public static Object ParseJouanalUserInfoResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	UserInfoResponseData repdata = new UserInfoResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		repdata.name = JsonParserUtil.parseStr(jResData, "name", "");
    		repdata.studentNumber = JsonParserUtil.parseStr(jResData, "studentNumber", "");
    		repdata.icon = JsonParserUtil.parseStr(jResData, "icon", "");
    		repdata.school = JsonParserUtil.parseStr(jResData, "school", "");
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalUpdateResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	UpdateResponseData repdata = new UpdateResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		repdata.name = JsonParserUtil.parseStr(jResData, "name", "");
    		repdata.size = JsonParserUtil.parseStr(jResData, "size", "");
    		repdata.version = JsonParserUtil.parseStr(jResData, "version", "");
    		repdata.downloadPrompt = JsonParserUtil.parseStr(jResData, "downloadPrompt", "");
    		repdata.downloadUrl = JsonParserUtil.parseStr(jResData, "downloadUrl", "");
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalFeedBackInfoData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	FeedBackResponseData repdata = new FeedBackResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	return repdata;
    }
    
    public static Object ParseJouanalZanAndCommentData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	ZanAndCommentResponseData repdata = new ZanAndCommentResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	return repdata;
    }

	public static Object ParseShareResultData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	ZanAndCommentResponseData repdata = new ZanAndCommentResponseData();

    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") &&
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	return repdata;
    }
    
    public static Object ParseJouanalRankInfoData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	RankInfoResponseData repdata = new RankInfoResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		if(jResData.has("totolCount")){
    			repdata.totolCount = JsonParserUtil.parseStr(jResData, "totolCount", "");
    		}
    		if(jResData.has("selfRank")){
    			repdata.selfRank = JsonParserUtil.parseStr(jResData, "selfRank", "");
    		}
    		if(jResData.has("items")){
    			JSONArray jDetails = jResData.getJSONArray("items");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					RankItem info = new RankItem();
    					info.ranking = JsonParserUtil.parseStr(jInfo, "ranking", "");
    					info.name = JsonParserUtil.parseStr(jInfo, "name", "");
    					info.number = JsonParserUtil.parseStr(jInfo, "number", "");
    					info.region = JsonParserUtil.parseStr(jInfo, "region", "");
    					info.score = JsonParserUtil.parseStr(jInfo, "score", "");
    					repdata.items.add(info);
    				}
    			}
    		}
    	}
    	return repdata;
    }
    
    public static Object ParseJouanalPayInfoData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	PayResponseData repdata = new PayResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    			JSONObject jResData = jsonRoot.getJSONObject("responseData");
    			repdata.payType = JsonParserUtil.parseStr(jResData, "payType", "");
    			repdata.orderid = JsonParserUtil.parseStr(jResData, "orderid", "");
    			repdata.appid = JsonParserUtil.parseStr(jResData, "appid", "");
    			repdata.partnerid = JsonParserUtil.parseStr(jResData, "partnerid", "");
    			repdata.prepay_id = JsonParserUtil.parseStr(jResData, "prepay_id", "");
    			repdata.myPackage = JsonParserUtil.parseStr(jResData, "package", "");
    			repdata.noncestr = JsonParserUtil.parseStr(jResData, "noncestr", "");
    			repdata.timestamp = JsonParserUtil.parseStr(jResData, "timestamp", "");
    			repdata.sign = JsonParserUtil.parseStr(jResData, "sign", "");
    			repdata.payUrl = JsonParserUtil.parseStr(jResData, "payUrl", "");
    	}
    	return repdata;
    }
    
    public static Object ParseJouanalEXiuxiuHomePageResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	EXiuxiuHomePageResponseData repdata = new EXiuxiuHomePageResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		if(jResData.has("banners")){
    			JSONArray jDetails = jResData.getJSONArray("banners");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					Banner info = new Banner();
    					info.eShowId = JsonParserUtil.parseStr(jInfo, "eShowId", "");
    					info.image = JsonParserUtil.parseStr(jInfo, "image", "");
    					info.title = JsonParserUtil.parseStr(jInfo, "title", "");
    					repdata.banners.add(info);
    				}
    			}
    		}
    		if(jResData.has("articles")){
    			JSONArray jDetails = jResData.getJSONArray("articles");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					Article info = new Article();
    					info.eShowId = JsonParserUtil.parseStr(jInfo, "eShowId", "");
    					info.icon = JsonParserUtil.parseStr(jInfo, "icon", "");
    					info.title = JsonParserUtil.parseStr(jInfo, "title", "");
    					info.praiseCount = JsonParserUtil.parseStr(jInfo, "praiseCount", "");
    					info.commentCount = JsonParserUtil.parseStr(jInfo, "commentCount", "");
    					repdata.articles.add(info);
    				}
    			}
    		}
    	}
    	
    	return repdata;
    }
    
    
    public static Object ParseJouanalEXiuxiuHomePageDetailsResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	EXiuxiuHomePageDetailResponseData repdata = new EXiuxiuHomePageDetailResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		
    		repdata.title = JsonParserUtil.parseStr(jResData, "title", "");
    		repdata.icon = JsonParserUtil.parseStr(jResData, "icon", "");
    		repdata.publisherName = JsonParserUtil.parseStr(jResData, "publisherName", "");
    		repdata.publisherType = JsonParserUtil.parseStr(jResData, "publisherType", "");
    		repdata.publishTime = JsonParserUtil.parseStr(jResData, "publishTime", "");
    		repdata.praiseCount = JsonParserUtil.parseStr(jResData, "praiseCount", "");
    		repdata.commentCount = JsonParserUtil.parseStr(jResData, "commentCount", "");
    		repdata.page = JsonParserUtil.parseStr(jResData, "page", "");
    		
    		if(jResData.has("details")){
    			JSONArray jDetails = jResData.getJSONArray("details");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					ReportDetail info = new ReportDetail();
    					info.content = JsonParserUtil.parseStr(jInfo, "content", "");
    					info.imageUrl = JsonParserUtil.parseStr(jInfo, "imageUrl", "");
    					info.type = JsonParserUtil.parseStr(jInfo, "type", "");
    					info.videoUrl = JsonParserUtil.parseStr(jInfo, "videoUrl", "");
    					info.videoWord = JsonParserUtil.parseStr(jInfo, "videoWord", "");
    					repdata.details.add(info);
    				}
    			}
    		}
    		if(jResData.has("comments")){
    			JSONArray jDetails = jResData.getJSONArray("comments");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					Comment info = new Comment();
    					info.by = JsonParserUtil.parseStr(jInfo, "by", "");
    					info.to = JsonParserUtil.parseStr(jInfo, "to", "");
    					info.comment = JsonParserUtil.parseStr(jInfo, "comment", "");
    					repdata.comments.add(info);
    				}
    			}
    		}
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalMyCourseOfflineResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	MyCourseOfflineResponseData repdata = new MyCourseOfflineResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
	    	if(jResData.has("courses")){
	    		JSONArray jDetails = jResData.getJSONArray("courses");
	    		if (jDetails != null) {
	    			for (int j01 = 0; j01 < jDetails.length(); j01++) {
	    				JSONObject jInfo = jDetails.getJSONObject(j01);
	    				Course info = new Course();
	    				info.name = JsonParserUtil.parseStr(jInfo, "name", "");
	    				info.courseId = JsonParserUtil.parseStr(jInfo, "courseId", "");
	    				info.time = JsonParserUtil.parseStr(jInfo, "time", "");
	    				info.teacher = JsonParserUtil.parseStr(jInfo, "teacher", "");
	    				info.totalSchedule = JsonParserUtil.parseStr(jInfo, "totalSchedule", "");
	    				info.remainSchedule = JsonParserUtil.parseStr(jInfo, "remainSchedule", "");
	    				info.append = JsonParserUtil.parseStr(jInfo, "append", "");
	    				repdata.courses.add(info);
	    			}
	    		}
	    	}
    	}
    	
    	return repdata;
    }
    
    public static Object ParseJouanalMyCourseOfflineBuyInfoResponseData(Context context, String responseData) throws Exception {
    	JSONObject jsonRoot = new JSONObject(responseData);
    	MyCourseOfflineBuyInfoResponseData repdata = new MyCourseOfflineBuyInfoResponseData();
    	
    	ParseCommonData(context, jsonRoot, repdata.commonData);
    	if (!repdata.commonData.result_code.equals("0000") && 
    			!repdata.commonData.result_code.equals("0")) {
    		return repdata;
    	}
    	if (jsonRoot.has("responseData")) {
    		JSONObject jResData = jsonRoot.getJSONObject("responseData");
    		repdata.isPurchased = JsonParserUtil.parseStr(jResData, "isPurchased", "");
    		repdata.name = JsonParserUtil.parseStr(jResData, "name", "");
    		repdata.teacher = JsonParserUtil.parseStr(jResData, "teacher", "");
    		repdata.totalSchedule = JsonParserUtil.parseStr(jResData, "totalSchedule", "");
    		repdata.area = JsonParserUtil.parseStr(jResData, "area", "");
    		repdata.school = JsonParserUtil.parseStr(jResData, "school", "");
    		repdata.student = JsonParserUtil.parseStr(jResData, "student", "");
    		repdata.studentTel = JsonParserUtil.parseStr(jResData, "studentTel", "");
    		repdata.tuition = JsonParserUtil.parseStr(jResData, "tuition", "");
    		repdata.date = JsonParserUtil.parseStr(jResData, "date", "");
    		repdata.serviceTel = JsonParserUtil.parseStr(jResData, "serviceTel", "");
    		
    		
    		if(jResData.has("scheduleList")){
    			JSONArray jDetails = jResData.getJSONArray("scheduleList");
    			if (jDetails != null) {
    				for (int j01 = 0; j01 < jDetails.length(); j01++) {
    					JSONObject jInfo = jDetails.getJSONObject(j01);
    					CourseTimeInfo info = new CourseTimeInfo();
    					info.scheduleId = JsonParserUtil.parseStr(jInfo, "scheduleId", "");
    					info.schedule = JsonParserUtil.parseStr(jInfo, "schedule", "");
    					info.price = JsonParserUtil.parseStr(jInfo, "price", "");
    					info.effectiveDate = JsonParserUtil.parseStr(jInfo, "effectiveDate", "");
    					repdata.scheduleList.add(info);
    				}
    			}
    		}
    	}
    	
    	return repdata;
    }

    static public String decollator01 = ",";
    static public String decollator02 = "|";
    static public String decollator03 = "_";


}
