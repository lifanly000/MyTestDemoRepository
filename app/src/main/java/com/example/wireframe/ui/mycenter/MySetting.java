package com.example.wireframe.ui.mycenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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
import com.example.wireframe.protocal.protocalProcess.model.UploadIconResponseData;
import com.example.wireframe.ui.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MySetting extends BaseActivity implements OnClickListener {
    private ImageView myIcon;

    private static final String IMAGE_FILE_NAME = "/faceImage.jpg";
    private static final String IMAGE_ICON_NAME = "/icon.jpg";

    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int SELECT_PIC_KITKAT = 3;

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

        if (getIntent().hasExtra("iconUrl")) {
            iconUrl = getIntent().getStringExtra("iconUrl");
        }

        if (!TextUtils.isEmpty(iconUrl)) {
            imageLoader.displayImage(iconUrl, myIcon, options);
        }

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.item1).setOnClickListener(this);
        findViewById(R.id.item2).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
        requestLocalPermission();
    }

    /**
     * android 6.0 申请权限
     */
    private void requestLocalPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
//                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
//            Toast.makeText(this,"申请权限",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    0);
        }
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
                intent = new Intent(this, MyResetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.exit://安全退出
                editor.putBoolean("hasLogin", false);
                editor.putString("userName", "");
                editor.commit();
                application.isLogin = false;
                application.userName = "";
                intent = new Intent(this, LoginActivity.class);
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
                case SELECT_PIC_KITKAT:
                    startPhotoZoom(data.getData());
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                        System.out.println("保存到目录");
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
        if (uri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url=getPath(this,uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        }else{
            intent.setDataAndType(uri, "image/*");
        }
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

    private Bitmap getBitmapFromUri(Uri uri)
    {
        try
        {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void getImageToView(Intent data) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Uri uri = data.getData();
            Bitmap photo = getBitmapFromUri(uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            uploadpic(saveMyBitmap(photo), photo);
        }else {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
//			BitmapDrawable bd= new BitmapDrawable(photo);
//			myIcon.setBackgroundDrawable(bd);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
//			String pic = new String(Base64.encode(baos.toByteArray(),
//					Base64.DEFAULT));
                uploadpic(saveMyBitmap(photo), photo);
            }
        }
    }

    public File saveMyBitmap(Bitmap mBitmap) {
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
     */
    private void uploadpic(File file, final Bitmap photo) {
        startProgress();
//		File file = new File("C://Users//Administrator//Desktop//image//PLL8.png");
        SharedPreferences userPreferences =
                ProtocalParser.getShareUserPreferences(this);
        String token = userPreferences.getString("jsessionId", "");

        String urlStr = "http://101.200.176.75:8080/emama/upload?resType=1"
                + "&len=" + String.valueOf(file.length()) + "&token=" + token;

        try {
            AjaxParams params = new AjaxParams();
            params.put("res", file); // 上传文件

            FinalHttp fh = new FinalHttp();
            fh.post(urlStr, params, new AjaxCallBack() {
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
                            BitmapDrawable bd = new BitmapDrawable(photo);
                            //	myIcon.setBackgroundDrawable(bd);
                            myIcon.setImageDrawable(bd);
//						if(!TextUtils.isEmpty(repdata.icon)){
//							imageLoader.displayImage(repdata.icon, myIcon, options);
//						}
                            Toast.makeText(MySetting.this, "发送成功。", Toast.LENGTH_SHORT).show();
                        } else {
                            endProgress();
                            Toast.makeText(MySetting.this, "发送失败，请稍后重试。", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MySetting.this, "发送失败，请稍后重试。", Toast.LENGTH_SHORT).show();
                }

            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     * 更换头像
     */
    public void showImageForIcon() {
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
//				Intent intentFromGallery = new Intent();
//				intentFromGallery.setType("image/*"); // 设置文件类型
//				intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
//				startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    startActivityForResult(intent, SELECT_PIC_KITKAT);
                } else {
                    startActivityForResult(intent, IMAGE_REQUEST_CODE);
                }
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

    //以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
