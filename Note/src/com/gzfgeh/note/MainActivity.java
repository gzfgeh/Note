package com.gzfgeh.note;

import java.util.Map;

import com.gzfgeh.service.SharedPreferencesData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends Activity {
	private EditText userName,passWord;
	private CheckBox remember;
	private CheckBox auto;
	@SuppressWarnings("unused")
	private Button loading;
	private SharedPreferencesData sp;
	private Map<String, String> map;
	private final static int REQUEST_CODE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		userName = (EditText) findViewById(R.id.username);
		passWord = (EditText) findViewById(R.id.password);
		remember = (CheckBox) findViewById(R.id.remember);
		auto = (CheckBox)findViewById(R.id.auto);
		loading = (Button) findViewById(R.id.signin_button);
		
		sp = new SharedPreferencesData(this);
		map = sp.getParemeter();
		
		userName.setText(map.get("name"));
		boolean checked = Boolean.valueOf(map.get("checked"));
		boolean isAuto = Boolean.valueOf(map.get("auto"));
		auto.setChecked(isAuto);
		remember.setChecked(checked);
		if (checked)
			passWord.setText(map.get("password"));
		if(isAuto){
			Intent intent = new Intent();
			intent.setClass(this, Display.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_alpha,R.anim.out_alpha);
		}
	}
	
	public void LoaderClick(View v){
		String name = userName.getText().toString();
		String password = passWord.getText().toString();
		boolean checked = remember.isChecked();
		boolean isAuto = auto.isChecked();
		
		if (name == null || "".equals(name) || password == null || "".equals(password)){
			AlertDialog dialog = new AlertDialog.Builder(this)
			.setTitle("提示")
			.setMessage("用户名或者密码不能为空！")
			.setPositiveButton("确定", null)
			.create();
			
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}else{
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("name", name);
			bundle.putString("password", password);
			bundle.putBoolean("checked", checked);
			bundle.putBoolean("auto", isAuto);
			intent.putExtras(bundle);
			intent.setClass(this, Loading.class);
			startActivityForResult(intent, REQUEST_CODE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_CODE){
			if (resultCode == Loading.RESULT_CODE){
				Bundle bundle = data.getExtras();
				String str = bundle.getString("cancle");
				if ("error".equals(str)){
					AlertDialog dialog = new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("用户名或者密码错误，请重新输入！")
					.setPositiveButton("确定", null)
					.create();
					
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
				}
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if (Intent.FLAG_ACTIVITY_CLEAR_TOP == intent.getFlags() ){
			finish();
		}
	}
	
	
	
}
