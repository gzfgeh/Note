package com.gzfgeh.service;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyMediaRecorder {
	private static final String TAG = "MyMediaRecorder";
	public static final int HANDLER_MSG = 0x8888;
	private static final int MAX_LENGTH = 1000 * 60 * 10;
	private MediaRecorder mediaRecorder;
	private String filePath;
	private long startTime;
	private long endTime;
	private double db = 0;
	
	private Handler handler;
	private static final int BASE = 1;				//参考标准
	private static final int SPEED = 100;			//每隔100ms 采样
	
	public MyMediaRecorder(File file,Handler handler) {
		super();
		this.handler = handler;
		this.filePath = file.getAbsolutePath();
	}
	
	public void startRecord(){
		if (mediaRecorder == null)
			mediaRecorder = new MediaRecorder();
		
		try {
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);		//设置麦克风
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//设置编码格式
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); 	//设置输出文件格式
			mediaRecorder.setOutputFile(filePath);								//准备  
			mediaRecorder.setMaxDuration(MAX_LENGTH);  
			mediaRecorder.prepare();
			mediaRecorder.start();
			startTime = System.currentTimeMillis();
			updateMicStatus();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long stopRecord(){
		if (mediaRecorder == null)
			return 0;
		
		endTime = System.currentTimeMillis();
		mediaRecorder.stop();  
		mediaRecorder.reset();  
		mediaRecorder.release();  
		mediaRecorder = null;
	    return endTime - startTime;
	}
	
	private Runnable updateMicStatusTimer = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			updateMicStatus();
		}
		
	};
	
	private void updateMicStatus() {
		// TODO Auto-generated method stub
		if (mediaRecorder != null){
			double radio = (double) mediaRecorder.getMaxAmplitude() / BASE;
			if (radio > 1){
				db = 20 * Math.log10(radio);
			}
			Log.i(TAG, "db:" + db);
			Message msg = new Message();
			msg.what = HANDLER_MSG;
			msg.obj = (int)db;
			handler.sendMessage(msg);
			handler.postDelayed(updateMicStatusTimer,SPEED);
		}
	}
}
