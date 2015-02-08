package com.gzfgeh.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesData {
	private Context context;

	public SharedPreferencesData(Context context) {
		super();
		this.context = context;
	}
	
	public void save(String userName,String passWord,boolean isChecked,boolean auto){
		SharedPreferences sharedPreferences = context.getSharedPreferences("password", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("name", userName);
		editor.putString("password", passWord);
		editor.putBoolean("checked", isChecked);
		editor.putBoolean("auto", auto);
		editor.commit();
	}
	
	public Map<String, String> getParemeter(){
		SharedPreferences sharedPreferences = context.getSharedPreferences("password", Context.MODE_PRIVATE);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", sharedPreferences.getString("name", ""));
		map.put("password", sharedPreferences.getString("password",""));
		map.put("checked", String.valueOf(sharedPreferences.getBoolean("checked", false)));
		map.put("auto", String.valueOf(sharedPreferences.getBoolean("auto", false)));
		return map;
	}
	
	public void clearAll(){
		SharedPreferences sharedPreferences = context.getSharedPreferences("password", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
	
	private static final String BIND__FLAG = "bind_flag";

	public static boolean isBind(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences("pre_tuisong",
				Context.MODE_PRIVATE);
		return sp.getBoolean(BIND__FLAG, false);
	}

	public static void bind(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences("pre_tuisong",
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(BIND__FLAG, true).commit();
	}
	
	
	public static void unbind(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences("pre_tuisong",
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(BIND__FLAG, false).commit();
	}

}
