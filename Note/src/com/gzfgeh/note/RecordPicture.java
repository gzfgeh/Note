package com.gzfgeh.note;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gzfgeh.service.BaseTitleBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;

public class RecordPicture extends BaseTitleBar {
	private static final String PHOTO = "photo";
	private static final String FILE_PATH=Environment.getExternalStorageDirectory()+
				File.separator + Display.NOTE + File.separator + PHOTO + File.separator;
	private static final int RESULT = 0x6666;
	
	private File file;
	private static final String CONTENT = "ͼƬ�ļ�";
	
	@SuppressLint({ "SimpleDateFormat", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		setContentLayout(R.layout.record_photo);
		
		//�ļ����Ƿ����
		File fold = new File(FILE_PATH);
		if (!fold.exists())
			fold.mkdirs();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date currentDate = new Date(System.currentTimeMillis());
		String name = format.format(currentDate) + ".jpg";
		
		file = new File(FILE_PATH + name);
		try {
			if (!file.exists()){
				file.setReadable(true);
				file.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setFile(file);
		setContent(CONTENT);
		setTitle("����");
	}
	
	public void onClick(View view){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //���������
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, RESULT); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK){
			if (requestCode == RESULT){
				//getImage(file.getAbsolutePath());
				displayRightBtn();
				displayStatusView();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);  
	}
	
	@SuppressWarnings("unused")
	private void getImage(String absolutePath) {
		// TODO Auto-generated method stub
		Bitmap bitmap;  
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		int maxH = 200;
		BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inJustDecodeBounds = true;
		// ��ȡ���ͼƬ�Ŀ�͸�  
		bitmap = BitmapFactory.decodeFile(absolutePath, options); //��ʱ����bmΪ��  
		//�������ű�  
		int be = (int)(options.outHeight / (float)maxH);  
		int ys = options.outHeight % maxH;//������  
		float fe = ys / (float)maxH;  
		if(fe>=0.5)be = be + 1;  
		if (be <= 0)  
		be = 1;  
		options.inSampleSize = be;  
		    
		 //���¶���ͼƬ��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false  
		options.inJustDecodeBounds = false;  
		bitmap=BitmapFactory.decodeFile(absolutePath,options);  
//		picture.setImageBitmap(bitmap);  
//		picture.setVisibility(View.VISIBLE);
	}
	
}
