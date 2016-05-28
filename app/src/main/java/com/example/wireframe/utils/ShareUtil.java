package com.example.wireframe.utils;

import android.app.Activity;

import com.example.wireframe.share.UmengShareUtils;

public class ShareUtil {
	
	/**
	 * @param context
	 * @param str		分享内容
	 * @param title   标题
	 */
	public static void shareByYoumeng(Activity context,String str,String title,String url){
		UmengShareUtils umengShareUtils = new UmengShareUtils(context, str,title,null,url);
//    	umengShareUtils.share();//弹出分享面板
    	umengShareUtils.postShare(title);//弹出自定义分享面板
	}
}