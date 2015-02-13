package com.gzfgeh.note;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gzfgeh.data.OperationSQLiteItem;
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
import android.widget.EditText;
import android.widget.ImageView;

public class RecordPicture extends BaseTitleBar {
	private static final String PHOTO = "photo";
	private static final String FILE_PATH=Environment.getExternalStorageDirectory()+
				File.separator + Display.NOTE + File.separator + PHOTO + File.separator;
	private static final int RESULT = 0x6666;
	
	private File file;
	private static final String CONTENT = "图片文件";
	private ImageView picture;
	private EditText pictureText;
	private int oldID;
	
	@SuppressLint({ "SimpleDateFormat", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		setContentLayout(R.layout.record_photo);
		
		//文件夹是否存在
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
		setTitle("拍照");
		
		picture = (ImageView) findViewById(R.id.pic);
		pictureText = (EditText) findViewById(R.id.pic_text);
		
		Intent i = getIntent();
		oldID = i.getIntExtra("ItemID", -1);
		if (oldID != -1){
			if (file != null)
				file.delete();
			
			OperationSQLiteItem operationSQLiteItem = new OperationSQLiteItem(this);
			final String path = operationSQLiteItem.queryContentUri(oldID);
			String[] pathSplit = path.split("/");
			String fileName = pathSplit[pathSplit.length - 1];
			getImage(fileName);
		}else{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用照相机
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(intent, RESULT);
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK){
			if (requestCode == RESULT){
				getImage(file.getAbsolutePath());
				displayRightBtn();
			}
		}else{
			if (file != null)
				file.delete();
			finish();
		}
		
		super.onActivityResult(requestCode, resultCode, data);  
	}
	
	private void getImage(String absolutePath) {
		// TODO Auto-generated method stub
		Bitmap bitmap;  
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		int maxH = 400;
		BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高  
		bitmap = BitmapFactory.decodeFile(absolutePath, options); //此时返回bm为空  
		//计算缩放比  
		int be = (int)(options.outHeight / (float)maxH);  
		int ys = options.outHeight % maxH;//求余数  
		float fe = ys / (float)maxH;  
		if(fe>=0.5)
			be = be + 1;  
		
		if (be <= 0)  
			be = 1;  
		options.inSampleSize = be;  
		    
		 //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false  
		options.inJustDecodeBounds = false;  
		bitmap=BitmapFactory.decodeFile(absolutePath,options);  
		picture.setImageBitmap(bitmap);  
		picture.setVisibility(View.VISIBLE);
		pictureText.setVisibility(View.VISIBLE);
	}
	
}
