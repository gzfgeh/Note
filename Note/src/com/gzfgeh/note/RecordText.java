package com.gzfgeh.note;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gzfgeh.data.OperationSQLiteItem;
import com.gzfgeh.service.BaseTitleBar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class RecordText extends BaseTitleBar {
	public static final String TEXT = "text";
	public static final String CONTENT = "写字";
	private final static String FILE_PATH=Environment.getExternalStorageDirectory()+
				File.separator + Display.NOTE + File.separator + TEXT + File.separator;
	
	private String content;
	private String encryption;
	private EditText data;
	
	private String name;
	private File file;
	private OperationSQLiteItem operationSQLiteItem;
	
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
		
		operationSQLiteItem = new OperationSQLiteItem(this);
		setTitle(CONTENT);
		setFile(file);
		displayRightBtn();
		
	}
	
	@SuppressLint("InflateParams") public void encrypt(){
		Builder dialog = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout)layoutInflater.inflate(R.layout.encrypt, null);
		dialog.setView(layout);
		final EditText encty = (EditText) layout.findViewById(R.id.encryption);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				encryption = encty.getText().toString();
				if (encryption.length() != 6){
					AlertDialog.Builder builder = new AlertDialog.Builder(RecordText.this);
					builder.setMessage("请输入6个数字!");
					builder.setNegativeButton("确定", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							encrypt();
						}
						
					});
					builder.show();
				}else{
					if (content.length() <= 6)
						operationSQLiteItem.addItemData(content,file.getAbsolutePath(),0,Long.valueOf(encryption),null,null,null);
					else 
						operationSQLiteItem.addItemData(content.substring(0, 5),file.getAbsolutePath(),0,Long.valueOf(encryption),null,null,null);
					
					setWriteFileFlag(true);
					finish();
				}
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}
	
	public void onClickTitleBtn(View v){
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(new long[]{1,100,0,0}, -1);
		
		content = data.getText().toString();
			switch (v.getId()) {
			case R.id.left_btn:
				if (null != file)
					file.delete();
				finish();
				break;
			case R.id.right_btn:
				if (content != null && !"".equals(content)){
					try {
						final FileOutputStream fos = new FileOutputStream(file);
						fos.write(content.getBytes());
						fos.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					AlertDialog dialog = new AlertDialog.Builder(this)
					.setTitle("是否加密")
					.setPositiveButton("是", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							encrypt();
						}
					})
					.setNegativeButton("否", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (content.length() <= 6)
								operationSQLiteItem.addItemData(content,file.getAbsolutePath(),0,0,null,null,null);
							else 
								operationSQLiteItem.addItemData(content.substring(0, 5),file.getAbsolutePath(),0,0,null,null,null);
							
							setWriteFileFlag(true);
							finish();
						}
					})
					.create();
					dialog.show();
					dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失 
					break;
				}
			default:
				break;
			}
		}
}
