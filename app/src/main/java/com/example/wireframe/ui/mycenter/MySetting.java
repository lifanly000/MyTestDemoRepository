package com.example.wireframe.ui.mycenter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.protocal.protocalProcess.ProtocalParser;
import com.example.wireframe.protocal.protocalProcess.model.JournalHomeWorkResponseData;
import com.example.wireframe.protocal.protocalProcess.model.UploadIconResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.ui.journal.JournalDetailActivity;
import com.example.wireframe.utils.DensityUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class MySetting extends BaseActivity implements OnClickListener {
	private ImageView myIcon;
	
	private static final String IMAGE_FILE_NAME = "/faceImage.jpg";
	private static final String IMAGE_ICON_NAME = "/icon.jpg";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	
	private String iconUrl = "";
	
	//异步加载图片的显示参数
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.showImageOnLoading(R.drawable.user_center_user_icon)
				.showImageForEmptyUri(R.drawable.user_center_user_icon)
				.showImageOnFail(R.drawable.user_center_user_icon)
				.build();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_setting);
		
		myIcon = (ImageView) findViewById(R.id.myIcon);
		
		if(getIntent().hasExtra("iconUrl")){
			iconUrl = getIntent().getStringExtra("iconUrl");
		}
		
		if(!TextUtils.isEmpty(iconUrl)){
			imageLoader.displayImage(iconUrl, myIcon, options);
		}
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.item1).setOnClickListener(this);
		findViewById(R.id.item2).setOnClickListener(this);
		findViewById(R.id.exit).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.item1://更换头像
			showImageForIcon();
			break;
		case R.id.item2://修改密码
			intent = new Intent(this,MyResetPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.exit://安全退出
			editor.putBoolean("hasLogin", false);
			editor.putString("userName", "");
			editor.commit();
			application.isLogin = false ;
			application.userName ="";
			intent = new Intent(this,LoginActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			case CAMERA_REQUEST_CODE:
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					System.out.println("保存到目录");
					File tempFile = new File(
							Environment.getExternalStorageDirectory()
									+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
							.show();
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
//			BitmapDrawable bd= new BitmapDrawable(photo);
//			myIcon.setBackgroundDrawable(bd);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
//			String pic = new String(Base64.encode(baos.toByteArray(),
//					Base64.DEFAULT));
			uploadpic(saveMyBitmap(photo),photo);
		}
	}
	
	public File saveMyBitmap(Bitmap mBitmap)  {
		 String s = new SimpleDateFormat("yyyyMMdd_hhmmss")
		 .format(new Date(System.currentTimeMillis()));
        File f = new File(
				Environment.getExternalStorageDirectory()
				+ "/" + s + ".jpg");
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
                fOut.flush();
                fOut.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
        return f;
}
	
	/**
	 * 上传头像
	 * 
	 * @param pic
	 */
	private void uploadpic(File file,final Bitmap photo) {
		startProgress();
//		File file = new File("C://Users//Administrator//Desktop//image//PLL8.png");
		SharedPreferences userPreferences =
				ProtocalParser.getShareUserPreferences(this);
		String token = userPreferences.getString("jsessionId", "");
		
		String urlStr = "http://101.200.176.75:8080/emama/upload?resType=1"
				+ "&len="+ String.valueOf(file.length())+"&token="+token;
		
		try {
			AjaxParams params = new AjaxParams();
			  params.put("res", file); // 上传文件
			 
			  FinalHttp fh = new FinalHttp();
			  fh.post(urlStr, params, new AjaxCallBack(){
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				try {
					JSONObject jsonRoot = new JSONObject(t.toString());
					UploadIconResponseData repdata = new UploadIconResponseData();

					if (jsonRoot.has("result")) {
						JSONObject jResult = jsonRoot.getJSONObject("result");
						repdata.commonData.result_code = jResult
								.getString("code");
						repdata.commonData.result_msg = jResult
								.getString("msg");
					}
					if (jsonRoot.has("responseData")) {
						JSONObject jResData = jsonRoot
								.getJSONObject("responseData");
						repdata.icon = jResData.getString("icon");
					}
					if (repdata.commonData.result_code.equals("0")) {
						endProgress();
						BitmapDrawable bd= new BitmapDrawable(photo);
					//	myIcon.setBackgroundDrawable(bd);
						myIcon.setImageDrawable(bd);
//						if(!TextUtils.isEmpty(repdata.icon)){
//							imageLoader.displayImage(repdata.icon, myIcon, options);
//						}
						Toast.makeText(MySetting.this, "发送成功。", 0).show();
					}else{
						endProgress();
						Toast.makeText(MySetting.this, "发送失败，请稍后重试。", 0).show();
					}
				} catch (JSONException e) {
					endProgress();
					e.printStackTrace();
				}
			}
			
			@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					endProgress();
					Toast.makeText(MySetting.this, "发送失败，请稍后重试。", 0).show();
				}
			 
			  });
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	
	/**
	 * 更换头像
	 * @param context
	 */
	public  void showImageForIcon(){
		final Dialog dialog = new Dialog(this, R.style.MyDialog);

		View myView = View.inflate(this, R.layout.dialog_for_icon, null);
		dialog.setContentView(myView);
		TextView text1 = (TextView) myView.findViewById(R.id.text1);
		TextView text2 = (TextView) myView.findViewById(R.id.text2);
		TextView text3 = (TextView) myView.findViewById(R.id.text3);
		
		text1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//拍照
				dialog.dismiss();
				Intent intentFromCapture = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				// 判断存储卡是否可以用，可用进行存储
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

					intentFromCapture
							.putExtra(MediaStore.EXTRA_OUTPUT, Uri
									.fromFile(new File(Environment
											.getExternalStorageDirectory(),
											IMAGE_FILE_NAME)));
				}
				startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
			}
		});
		text2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//从相册中选择
				dialog.dismiss();
				Intent intentFromGallery = new Intent();
				intentFromGallery.setType("image/*"); // 设置文件类型
				intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
			}
		});
		text3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.BOTTOM);
		lp.width = LayoutParams.FILL_PARENT;
		dialogWindow.setAttributes(lp);
//		dialog .setCanceledOnTouchOutside(false);
		dialog.show();
	}
}
