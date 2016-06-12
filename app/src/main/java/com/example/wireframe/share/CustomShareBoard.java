/**
 * 
 */

package com.example.wireframe.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.protocal.ProtocalEngine;
import com.example.wireframe.protocal.ProtocalEngineObserver;
import com.example.wireframe.protocal.SchemaDef;
import com.example.wireframe.protocal.protocalProcess.model.ShareResultRequestData;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

/**
 * 
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener ,ProtocalEngineObserver{

    private UMSocialService mController ;
    private Activity mActivity;
    private String title ;
    private String url ;

    

    public CustomShareBoard(Activity activity,UMSocialService controller,String shareTitle,String url) {
        super(activity);
        this.mActivity = activity;
        this.mController = controller;
        this.title = shareTitle;
        this.url =url;
        initView(activity,shareTitle);
//        android:layoutAnimation="@anim/layout_animation"
    }

    @SuppressWarnings("deprecation")
    private void initView(Context context,String shareTitle) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.custom_board, null);
        rootView.findViewById(R.id.wechat).setOnClickListener(this);
        rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
        rootView.findViewById(R.id.qq).setOnClickListener(this);
        rootView.findViewById(R.id.qzone).setOnClickListener(this);
        rootView.findViewById(R.id.sina).setOnClickListener(this);
        rootView.findViewById(R.id.cancel).setOnClickListener(this);
        rootView.findViewById(R.id.outBlank).setOnClickListener(this);
       TextView title =  (TextView) rootView.findViewById(R.id.share_text);
       title.setText(shareTitle);
       setContentView(rootView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.wechat:
                performShare(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.wechat_circle:
                performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.qq:
                performShare(SHARE_MEDIA.QQ);
                break;
            case R.id.qzone:
                performShare(SHARE_MEDIA.QZONE);
                break;
            case R.id.sina:
            	performShare(SHARE_MEDIA.SINA);
            	break;
            case R.id.cancel:
            case R.id.outBlank:
            	this.dismiss();
            	break;
            default:
                break;
        }
    }

    private void performShare(SHARE_MEDIA platform) {
        mController.postShare(mActivity, platform, new SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = platform.toString();
                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                    showText += "平台分享成功";
                } else {
                    showText += "平台分享失败";
                }
                Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
                startRequest();
                dismiss();
            }
        });
    }

    private void startRequest(){
        ProtocalEngine engine = new ProtocalEngine(mActivity);
        engine.setObserver(this);
        ShareResultRequestData data = new ShareResultRequestData();
        data.title = title ;
        data.url = url;
        engine.startRequest(SchemaDef.SHARE_RESULT,data);
    }

    @Override
    public void OnProtocalFinished(Object obj) {

    }

    @Override
    public void OnProtocalError(int errorCode) {

    }

    @Override
    public void OnProtocalProcess(Object obj) {

    }
}
