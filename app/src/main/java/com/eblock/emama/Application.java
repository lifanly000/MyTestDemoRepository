package com.eblock.emama;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.example.wireframe.protocal.RequestManager;
import com.example.wireframe.utils.CrashHandler;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class Application extends android.app.Application 
{
    private static String TAG = "LbApplication";

    public Application() {
        super();
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setHorizonEnable(0);
        RequestManager.init(this);
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	     // 将该app注册到微信
	     msgApi.registerApp("wxe44ca3f84ceb1fb5");
        
//        CrashHandler handler = CrashHandler.getInstance();
//        handler.init(getApplicationContext());
    }

    private static Application instance;

    /**
     * @return
     */
    public static Application getInstance() {
    	if (null == instance) { 
            instance = new Application(); 
        } 
        return instance;
    }
    
    /**
	 * 禁止系统横竖屏
	 * @param index 0-禁止；1-不禁止
	 */
	public void setHorizonEnable(int index){
		ContentValues values = new ContentValues();
		ContentResolver resolver = this.getContentResolver();
		Uri uri = Uri.parse("content://settings/system");
		String name="accelerometer_rotation";
		int value = index;

		values.put("name", name);
		values.put("value", Integer.toString(value));
		resolver.insert(uri, values);
	}
	
    /**
     * 是否登陆
     */
    public boolean isLogin = false ;
    /**
     * 用戶名
     */
    public String userName ="";
    
    private List<Activity> mList = new LinkedList(); 
 
    // add Activity  
    public void addActivity(Activity activity) { 
        mList.add(activity); 
    } 
 
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    }  
    
    public static int payFlag = 100 ;
}