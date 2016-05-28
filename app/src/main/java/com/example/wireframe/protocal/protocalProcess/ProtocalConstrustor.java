package com.example.wireframe.protocal.protocalProcess;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;

import com.eblock.emama.Application;
import com.example.wireframe.protocal.protocalProcess.model.EXiuxiuHomePageDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.FeedBackRequestData;
import com.example.wireframe.protocal.protocalProcess.model.FindPasswordRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GetVeriCodeRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpHomeRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpNumberRequestData;
import com.example.wireframe.protocal.protocalProcess.model.GrowUpResultDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineBuyInfoRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineNotMemberTryUseRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOfflineNotMemberUseRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineBuyInfoRequestData;
import com.example.wireframe.protocal.protocalProcess.model.MyCourseOnlineDetailRequestData;
import com.example.wireframe.protocal.protocalProcess.model.PayRequestData;
import com.example.wireframe.protocal.protocalProcess.model.RankInfoRequestData;
import com.example.wireframe.protocal.protocalProcess.model.ResetPasswordRequestData;
import com.example.wireframe.protocal.protocalProcess.model.UpdateRequestData;
import com.example.wireframe.protocal.protocalProcess.model.UserLoginRequestData;
import com.example.wireframe.protocal.protocalProcess.model.UserRegisterRequestData;
import com.example.wireframe.protocal.protocalProcess.model.ZanAndCommentRequestData;

public class ProtocalConstrustor {
	
	private static Application application = Application.getInstance();
	
    public static String initIMEI(Context context) {

        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return mTelephonyMgr.getDeviceId();
    }

    static final String USERINFO_PREFERENCES_KEY = "UserInfo";

    static public SharedPreferences getShareUserPreferences(Context context) {
        return context.getSharedPreferences(USERINFO_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private static String lpad(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }
    
    
    public static String getUidString(Context context)
    {
        SharedPreferences userPreferences = getShareUserPreferences(context);
        Editor editor = userPreferences.edit();
        String uid = userPreferences.getString("uid", "");
        editor.commit();
        return uid;
    }
    
    private static void addCommonData(JSONObject jsonRoot, Context context) throws Exception {


    	jsonRoot.put("product", "e-mama");	
        addMobileInfo(jsonRoot, context);
        addUserInfo(jsonRoot, context);
    }

    private static void addMobileInfo(JSONObject jsonRoot, Context context) throws Exception {
        JSONObject jsonMobileInfo = new JSONObject();

        jsonMobileInfo.put("platform", "android");
        String imei = initIMEI(context);
        jsonMobileInfo.put("imei", imei);

        jsonRoot.put("mobileInfo", jsonMobileInfo);
    }

    private static void addUserInfo(JSONObject jsonRoot, Context context) throws Exception {
        JSONObject jsonUserInfo = new JSONObject();

        SharedPreferences userPreferences = getShareUserPreferences(context);
        Editor editor = userPreferences.edit();
        String uid = userPreferences.getString("uid", "");
        String jsessionId = userPreferences.getString("jsessionId", "");
        editor.commit();

        if(application.isLogin){
	        jsonUserInfo.put("uid", uid);
	        jsonUserInfo.put("jsessionId", jsessionId);
        }else{
        	 jsonUserInfo.put("uid", "");
 	        jsonUserInfo.put("jsessionId", "");
        }

        jsonRoot.put("userInfo", jsonUserInfo);
    }

    // 学习日志 首页
    public static String ConstructJournalHomePageRequestData(Object obj,Context context) throws Exception {
        JSONObject jsonRoot = new JSONObject();

        jsonRoot.put("busiCode", "100001");
        addCommonData(jsonRoot, context);
        
        JournalHomeRequestData cmdInfo = (JournalHomeRequestData) obj;

        JSONObject jsonCommandInfo = new JSONObject();
        jsonCommandInfo.put("yearMonth", cmdInfo.yearMonth);
        jsonRoot.put("commandInfo", jsonCommandInfo);

        return jsonRoot.toString();
    }
    
    public static String ConstructJournalHomeDetailRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100002");
    	addCommonData(jsonRoot, context);
    	
    	JournalHomeDetailRequestData cmdInfo = (JournalHomeDetailRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("workId", cmdInfo.workId);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    
    
    public static String ConstructGrowUpHomeRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100101");
    	addCommonData(jsonRoot, context);
    	
    	GrowUpHomeRequestData cmdInfo = (GrowUpHomeRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("reportType", cmdInfo.reportType);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructGrowUpHomeDetailRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100102");
    	addCommonData(jsonRoot, context);
    	
    	
    	GrowUpHomeDetailRequestData cmdInfo = (GrowUpHomeDetailRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("reportId", cmdInfo.reportId);
    	jsonCommandInfo.put("reportType", cmdInfo.reportType);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    public static String ConstructGrowUpResultDetailRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100103");
    	addCommonData(jsonRoot, context);
    	
    	
    	GrowUpResultDetailRequestData cmdInfo = (GrowUpResultDetailRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("reportId", cmdInfo.reportId);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructGrowUpNumberRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100004");
    	addCommonData(jsonRoot, context);
    	
    	
    	GrowUpNumberRequestData cmdInfo = (GrowUpNumberRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("onlyAmount", cmdInfo.onlyAmount);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructGetVeriCodeRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100303");
    	addCommonData(jsonRoot, context);
    	
    	
    	GetVeriCodeRequestData cmdInfo = (GetVeriCodeRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("phone", cmdInfo.phone);
    	jsonCommandInfo.put("type", cmdInfo.type);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructResetPasswordRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100304");
    	addCommonData(jsonRoot, context);
    	
    	
    	ResetPasswordRequestData cmdInfo = (ResetPasswordRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("pwdOld", cmdInfo.pwdOld);
    	jsonCommandInfo.put("pwdNew", cmdInfo.pwdNew);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructUserLoginRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100302");
    	addCommonData(jsonRoot, context);
    	
    	
    	UserLoginRequestData cmdInfo = (UserLoginRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("accountName", cmdInfo.accountName);
    	jsonCommandInfo.put("pwd", cmdInfo.pwd);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructUserRegisterRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100301");
    	addCommonData(jsonRoot, context);
    	
    	
    	UserRegisterRequestData cmdInfo = (UserRegisterRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("accountName", cmdInfo.accountName);
    	jsonCommandInfo.put("pwd", cmdInfo.pwd);
    	jsonCommandInfo.put("phone", cmdInfo.phone);
    	jsonCommandInfo.put("code", cmdInfo.code);
    	jsonCommandInfo.put("cityCode", cmdInfo.cityCode);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructFindPasswordRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100305");
    	addCommonData(jsonRoot, context);
    	
    	
    	FindPasswordRequestData cmdInfo = (FindPasswordRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("pwdNew", cmdInfo.pwdNew);
    	jsonCommandInfo.put("code", cmdInfo.code);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructMyCourseOnlineRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100311");
    	addCommonData(jsonRoot, context);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructMyBuyCourseOnlineRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100321");
    	addCommonData(jsonRoot, context);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructMyBuyCourseOfflineRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100322");
    	addCommonData(jsonRoot, context);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructMyCourseOnlineBuyInfoRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100312");
    	addCommonData(jsonRoot, context);
    	
    	MyCourseOnlineBuyInfoRequestData cmdInfo = (MyCourseOnlineBuyInfoRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("courseId", cmdInfo.courseId);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	return jsonRoot.toString();
    }
    
    public static String ConstructMyBuyCourseOfflineDetailRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100323");
    	addCommonData(jsonRoot, context);
    	
    	MyCourseOfflineDetailRequestData cmdInfo = (MyCourseOfflineDetailRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("courseId", cmdInfo.courseId);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	return jsonRoot.toString();
    }
    
    public static String ConstructMyBuyCourseOnlineDetailRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100324");
    	addCommonData(jsonRoot, context);
    	
    	MyCourseOnlineDetailRequestData cmdInfo = (MyCourseOnlineDetailRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("courseId", cmdInfo.courseId);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	return jsonRoot.toString();
    }
    
    public static String ConstructMyCourseOfflineNotMemberTryUseRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100325");
    	addCommonData(jsonRoot, context);
    	
    	MyCourseOfflineNotMemberTryUseRequestData cmdInfo = (MyCourseOfflineNotMemberTryUseRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("courseId", cmdInfo.courseId);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	return jsonRoot.toString();
    }
    
    public static String ConstructMyCourseOfflineNotMemberUseRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100326");
    	addCommonData(jsonRoot, context);
    	
    	MyCourseOfflineNotMemberUseRequestData cmdInfo = (MyCourseOfflineNotMemberUseRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("courseId", cmdInfo.courseId);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	return jsonRoot.toString();
    }
    public static String ConstructPayData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100330");
    	addCommonData(jsonRoot, context);
    	
    	PayRequestData cmdInfo = (PayRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("courseId", cmdInfo.courseId);
    	jsonCommandInfo.put("payType", cmdInfo.payType);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	return jsonRoot.toString();
    }
    
    public static String ConstructEXiuxiuHomeDetailPageRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100202");
    	addCommonData(jsonRoot, context);
    	
    	EXiuxiuHomePageDetailRequestData cmdInfo = (EXiuxiuHomePageDetailRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("eShowId", cmdInfo.eShowId);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	return jsonRoot.toString();
    }
    
    public static String ConstructUpdateRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "150001");
    	addCommonData(jsonRoot, context);
    	
    	UpdateRequestData cmdInfo = (UpdateRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("appName", cmdInfo.appName);
    	jsonCommandInfo.put("packageName", cmdInfo.packageName);
    	jsonCommandInfo.put("versionName", cmdInfo.versionName);
    	jsonCommandInfo.put("versionCode", cmdInfo.versionCode);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	return jsonRoot.toString();
    }
    
    public static String ConstructFeedBackData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "150002");
    	addCommonData(jsonRoot, context);
    	
    	FeedBackRequestData cmdInfo = (FeedBackRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("phone", cmdInfo.phone);
    	jsonCommandInfo.put("email", cmdInfo.email);
    	jsonCommandInfo.put("content", cmdInfo.content);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	return jsonRoot.toString();
    }
    
    public static String ConstructZanAndCommentData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100203");
    	addCommonData(jsonRoot, context);
    	
    	ZanAndCommentRequestData cmdInfo = (ZanAndCommentRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("eShowId", cmdInfo.eShowId);
    	jsonCommandInfo.put("mode", cmdInfo.mode);
    	jsonCommandInfo.put("to", cmdInfo.to);
    	jsonCommandInfo.put("content", cmdInfo.content);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	return jsonRoot.toString();
    }
    
    public static String ConstructRankInfoData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "150003");
    	addCommonData(jsonRoot, context);
    	
    	RankInfoRequestData cmdInfo = (RankInfoRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("type", cmdInfo.type);
    	jsonCommandInfo.put("pageNo", cmdInfo.pageNo);
    	jsonCommandInfo.put("pageSize", cmdInfo.pageSize);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructUserInfoRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100307");
    	addCommonData(jsonRoot, context);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructEXiuxiuHomePageRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100201");
    	addCommonData(jsonRoot, context);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructMyCourseOfflineRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100313");
    	addCommonData(jsonRoot, context);
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	
    	return jsonRoot.toString();
    }
    
    public static String ConstructMyCourseOfflineBuyInfoRequestData(Object obj,Context context) throws Exception {
    	JSONObject jsonRoot = new JSONObject();
    	
    	jsonRoot.put("busiCode", "100314");
    	addCommonData(jsonRoot, context);
    	
    	MyCourseOfflineBuyInfoRequestData cmdInfo = (MyCourseOfflineBuyInfoRequestData) obj;
    	
    	JSONObject jsonCommandInfo = new JSONObject();
    	jsonCommandInfo.put("courseId", cmdInfo.courseId);
    	jsonRoot.put("commandInfo", jsonCommandInfo);
    	return jsonRoot.toString();
    }


}
