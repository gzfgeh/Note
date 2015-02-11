package com.gzfgeh.note;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gzfgeh.data.OperationSQLiteItem;
import com.gzfgeh.service.BaseTitleBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RecordText extends BaseTitleBar {
	public static final String TEXT = "text";
	public static final String CONTENT = "写字";
	private final static String FILE_PATH=Environment.getExternalStorageDirectory()+
				File.separator + Display.NOTE + File.separator + TEXT + File.separator;
	
	private String content;
	private EditText data;
	
	private String name;
	private File file;
	private OperationSQLiteItem operationSQLiteItem;
	private File oldFile;
	private String oldDataString;
	private int oldID;
	
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		setContentLayout(R.layout.record_text);
		
		data = (EditText) findViewById(R.id.text);
		
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date(System.currentTimeMillis());
		name = formate.format(date) + ".txt";
		
		//文件夹是否存在
		File fold = new File(FILE_PATH);
		if (!fold.exists())
			fold.mkdirs();
		//文件是否存在
		file = new File(FILE_PATH + name);
		try {
			if (!file.exists())
				file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setFile(file);
		
		operationSQLiteItem = new OperationSQLiteItem(this);
		setTitle(CONTENT);
		displayRightBtn();
		
		Intent intent = getIntent();
		oldID = intent.getIntExtra("ItemID", -1);
		if (oldID != -1){
			try {
				String filePath = operationSQLiteItem.queryContentUri(oldID);
				oldFile = new File(filePath);
				if (!oldFile.exists())
					oldFile.createNewFile();
				@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(new FileReader(oldFile));
				StringBuffer sb = new StringBuffer();
				String temp = br.readLine();
				while (temp != null){
					sb.append(temp);
					temp = br.readLine();
				}
				oldDataString = sb.toString();
				data.setText(oldDataString);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void onClickTitleBtn(View v){
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(new long[]{1,100,0,0}, -1);
		
		content = data.getText().toString();
		switch (v.getId()) {
		case R.id.left_btn:
			if (oldDataString != null)
				if (oldDataString.equals(content))
					Toast.makeText(this, "笔记没有更改", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(this, "放弃更改", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, "放弃添加", Toast.LENGTH_SHORT).show();
			
			if (null != file)
				file.delete();
			break;
			
		case R.id.right_btn:
			if (oldDataString != null){
				if (oldDataString.equals(content)){
					if (null != file)
						file.delete();
					Toast.makeText(this, "笔记没有更改", Toast.LENGTH_SHORT).show();
				}
				else{
					if (content.length() <= 6)
						operationSQLiteItem.updateItemContent(oldID, content, file.getAbsolutePath(), null, 0, 0, null, null, null);
					else
						operationSQLiteItem.updateItemContent(oldID, content.substring(0,5), file.getAbsolutePath(), null, 0, 0, null, null, null);
					
					if (null != oldFile)
						oldFile.delete();
					
					try {
						final FileOutputStream fos = new FileOutputStream(file);
						fos.write(content.getBytes());
						fos.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				if (content != null && !"".equals(content)){
					try {
						final FileOutputStream fos = new FileOutputStream(file);
						fos.write(content.getBytes());
						fos.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// TODO Auto-generated method stub
					if (content.length() <= 6)
						operationSQLiteItem.addItemData(content,file.getAbsolutePath(),0,0,null,null,null);
					else 
						operationSQLiteItem.addItemData(content.substring(0, 5),file.getAbsolutePath(),0,0,null,null,null);
				}else{
					if (file != null)
						file.delete();
				}
			}
			break;
			
		default:
			break;
		}
		finish();
	}
	
}
