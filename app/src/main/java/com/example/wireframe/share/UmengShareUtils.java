package com.example.wireframe.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Gravity;

import com.eblock.emama.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 友盟分享工具类
 * 
 * @author tangxian
 * 
 */
public class UmengShareUtils {

	private Activity mActivity;

	/** 友盟分享服务 */
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	/** QQ app id */
	private static final String QQAppId = "100424468";

	/** QQ app key */
	private static final String QQAppKey = "c7394704798a158208a74ab60104f0ba";

	// 注意：在微信授权的时候，必须传递appSecret
	// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
	public static final String WXAppId = "wxe44ca3f84ceb1fb5";
	public static final String WXAppSecret = "78008716bfa2ed38eb7cf43fe635c096";

	/** 要分享的内容 */
	private String content;

	/** 分享的url */
	private String url = "http://www.eblockschina.com";
	private String imageUrl = null;

	/** 分享的标题 */
	private static  String title = "分享";

	public UmengShareUtils(Activity mActivity, String content,String title,String imageUrl, String url) {
		this.mActivity = mActivity;
		this.content = content;
		this.imageUrl = imageUrl;
		this.title = title;
		if(TextUtils.isEmpty(url)){
			url = "http://www.eblockschina.com";
		}else{
			this.url = url;
		}
		configPlatforms();
		setShareContent();
		// 设置分享面板显示的内容
		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA);
	}

	/** 调用分享面板 */
	public void share() {
		mController.openShare(mActivity, false);
	}

	
	  /**
     * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
     * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
     */
    public void postShare(String title) {
        CustomShareBoard shareBoard = new CustomShareBoard(mActivity,mController,title,url);
        shareBoard.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }
    
	/**
	 * 如需使用sso需要在onActivity中调用此方法
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void authSSO(int requestCode, int resultCode, Intent data) {
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private void configPlatforms() {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		// 添加QQ、QZone平台
		addQQQZonePlatform();

		// 添加微信、微信朋友圈平台
		addWXPlatform();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {

		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, QQAppId,
				QQAppKey);
		qqSsoHandler.setTitle(content);
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity,
				QQAppId, QQAppKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {

		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mActivity, WXAppId, WXAppSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, WXAppId,
				WXAppSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent() {
		Bitmap bitmap = null;
		if (TextUtils.isEmpty(imageUrl)) {
			bitmap = BitmapFactory.decodeResource(mActivity.getResources(),R.drawable.ic_launcher);
		} else {
			bitmap = BitmapFactory.decodeFile(imageUrl);
		}
		// 分享图片
		UMImage urlImage = new UMImage(mActivity, bitmap);

		// 设置微信分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(content);
		weixinContent.setTitle(title);
		weixinContent.setTargetUrl(url);
		weixinContent.setShareMedia(urlImage);
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		circleMedia.setTitle(title);
		circleMedia.setTargetUrl(url);
		circleMedia.setShareMedia(urlImage);
		mController.setShareMedia(circleMedia);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(content);
		qzone.setTitle(title);
		qzone.setTargetUrl(url);
		qzone.setShareMedia(urlImage);
		mController.setShareMedia(qzone);

		// 设置QQ分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setTitle(title);
		qqShareContent.setShareContent(content);
		qqShareContent.setTargetUrl(url);
		qqShareContent.setShareMedia(urlImage);
		mController.setShareMedia(qqShareContent);

		// 设置新浪分享内容
		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setShareContent(content);
		sinaContent.setTitle(title);
		sinaContent.setTargetUrl(url);
		sinaContent.setShareMedia(urlImage);
		mController.setShareMedia(sinaContent);
	}
}
