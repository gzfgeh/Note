package com.gzfgeh.service;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.gzfgeh.note.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class help extends Activity{
	private TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		textView = (TextView) findViewById(R.id.display_text);
		autoBindBaiduYunTuiSong();
	}
	private void autoBindBaiduYunTuiSong() {
		// TODO Auto-generated method stub
		if (!SharedPreferencesData.isBind(getApplicationContext()))  
        {  
            PushManager.startWork(getApplicationContext(),  
                    PushConstants.LOGIN_TYPE_API_KEY,  
                    "HpLXcHVB8GSUtW0vUxfztl6p");  
        }  
	}

	
}
