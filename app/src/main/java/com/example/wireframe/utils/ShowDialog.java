package com.example.wireframe.utils;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.eblock.emama.R;

public class ShowDialog {
	/**
	 * 更换头像
	 * @param context
	 */
	public static void showImageForIcon(Context context){
		final Dialog dialog = new Dialog(context, R.style.MyDialog);

		View myView = View.inflate(context, R.layout.dialog_for_icon, null);
		dialog.setContentView(myView);
		TextView text1 = (TextView) myView.findViewById(R.id.text1);
		TextView text2 = (TextView) myView.findViewById(R.id.text2);
		TextView text3 = (TextView) myView.findViewById(R.id.text3);
		
		text1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//拍照
			}
		});
		text2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//从相册中选择
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
	
	/**
	 * 分享
	 * @param context
	 */
	public static void shareToOthers(Context context){
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
//		Dialog dialog = new Dialog(context)
		
		View myView = View.inflate(context, R.layout.dialog_for_icon, null);
		dialog.setContentView(myView);
		TextView text1 = (TextView) myView.findViewById(R.id.text1);
		TextView text2 = (TextView) myView.findViewById(R.id.text2);
		TextView text3 = (TextView) myView.findViewById(R.id.text3);
		
		text1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//拍照
			}
		});
		text2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//从相册中选择
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
	
	/**
	 * 客服事件对话框
	 * 
	 */
	public static void ShowTelephoneDialog(final Context context,final String telephone) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(context.getString(R.string.dialogTelePhone001));
		builder.setPositiveButton(
				context.getString(R.string.dialogTelePhone002),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+ telephone));
						context.startActivity(intent);
					}
				});
		builder.setNegativeButton(
				context.getString(R.string.dialogTelePhone003),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
	
}
