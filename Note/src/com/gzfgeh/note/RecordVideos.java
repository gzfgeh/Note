package com.gzfgeh.note;

import java.io.File;

import com.gzfgeh.service.BaseTitleBar;
import com.gzfgeh.service.Videos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RecordVideos extends BaseTitleBar {
	private static final String CONTENT = "ÊÓÆµÎÄ¼þ";
	private static final int RESULT = 0x6666;
	
	@SuppressLint("SimpleDateFormat") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_videos);
		
		setContent(CONTENT);
		setTitle("¿ÌÂ¼");
		
		Intent intent = new Intent(RecordVideos.this,Videos.class);
		startActivityForResult(intent,RESULT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK){
			if (requestCode == RESULT){
				//getImage(file.getAbsolutePath());
				Intent intent = getIntent();
				String fileName = intent.getStringExtra("fileName");
				if (fileName != null){
					File file = new File(fileName);
					setFile(file);
					displayRightBtn();
					displayStatusView();
					setFlag(true);
				}
			}
		}else{
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);  
	}
}
