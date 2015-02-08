package com.gzfgeh.note;

import java.util.Map;

import com.gzfgeh.service.SharedPreferencesData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Loading extends Activity {
	public final static int RESULT_CODE = 1;
	private String name;
	private String password;
	private boolean checked;
	private boolean auto;
	private SharedPreferencesData sp;
	private Map<String, String> map;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		auto = bundle.getBoolean("auto");
		name = bundle.getString("name");
		password = bundle.getString("password");
		checked = bundle.getBoolean("checked");
		
		sp = new SharedPreferencesData(this);
		map = sp.getParemeter();
		
		new Thread(new Runnable(){
			public void run() {
				try {
					Thread.sleep(1000);
					if ("".equals(map.get("name"))){
						if(checked)
							sp.save(name, password, checked,auto);
					}else{
						if ((name.equals(map.get("name"))) && (password.equals(map.get("password")))){
							if (checked){
								sp.save(name, password,checked,auto);
							}
						}else{
								intent = new Intent();
								intent.putExtra("cancle", "error");
								setResult(RESULT_CODE,intent);
								finish();
								return;
						}
					}
					
					intent = new Intent(Loading.this,Display.class);
					intent.putExtra("user_name", map.get("name"));
					startActivity(intent);
					overridePendingTransition(R.anim.in_alpha,R.anim.out_alpha);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	public void cancleClick(View v){
		sp.clearAll();
		intent = new Intent();
		intent.putExtra("cancle", "cancle");
		setResult(RESULT_CODE,intent);
		finish();
	}
	
}
