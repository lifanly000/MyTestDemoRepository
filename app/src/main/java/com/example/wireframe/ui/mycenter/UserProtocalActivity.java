package com.example.wireframe.ui.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eblock.emama.R;
import com.example.wireframe.ui.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by lifan on 2016/7/29.
 */
public class UserProtocalActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_protocal);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textView = (TextView) findViewById(R.id.text);
        InputStream abpath = getClass().getResourceAsStream("/assets/user_protocal.txt");
        String content = new String(InputStreamToByte(abpath));
        textView.setText(content);
    }

    private byte[] InputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        }catch (Exception e){
            e.printStackTrace();
            return new byte[2];
        }
    }
}
