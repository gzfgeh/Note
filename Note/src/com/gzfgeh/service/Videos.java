package com.gzfgeh.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gzfgeh.note.Display;
import com.gzfgeh.note.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Videos extends Activity {
	private static final String VIDEO = "video";
	private final static String FILE_PATH=Environment.getExternalStorageDirectory()+
				File.separator + Display.NOTE + File.separator + VIDEO + File.separator;
	
	private MediaRecorder mediaRecorder;
	private File file;
	private String filePath;
	
	private SurfaceView surfaceView;
	private RelativeLayout recordLayout;
	private RelativeLayout getLayout;
	private TextView timerView;
	private Handler handler;
	
	private int hou = 0;
	private int min = 0;
	private int sec = 0;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.videos);
		
		//文件夹是否存在
		File fold = new File(FILE_PATH);
		if (!fold.exists())
			fold.mkdirs();
		
		
		surfaceView = (SurfaceView) findViewById(R.id.surface_view);
		surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.getHolder().setFixedSize(176, 144);
		surfaceView.getHolder().setKeepScreenOn(true);
		
		timerView = (TextView) findViewById(R.id.timer);
		timerView.setText("00:00:00");
		recordLayout = (RelativeLayout) findViewById(R.id.video_layout);
		getLayout = (RelativeLayout)findViewById(R.id.display_button);
		recordLayout.setVisibility(View.VISIBLE);
		getLayout.setVisibility(View.GONE);
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.record:
			recordLayout.setVisibility(View.GONE);
			getLayout.setVisibility(View.VISIBLE);
			startRecord();
			handler.postDelayed(ReflashTime, 1000);
			break;
		case R.id.yes:
			if (mediaRecorder != null){
				mediaRecorder.stop();
				mediaRecorder.release();
				mediaRecorder = null;
			}
			handler.removeCallbacks(ReflashTime);
			
			Intent intent = new Intent();
			intent.putExtra("fileName", file.getAbsolutePath());
			setResult(RESULT_OK,intent);
			finish();
			break;
		case R.id.no:
			if (mediaRecorder != null){
				mediaRecorder.stop();
				mediaRecorder.release();
				mediaRecorder = null;
			}
			if (null != file)
				file.delete();
			recordLayout.setVisibility(View.VISIBLE);
			getLayout.setVisibility(View.GONE);
			handler.removeCallbacks(ReflashTime);
			break;
		default:
			break;
		}
	}
	@SuppressLint("SimpleDateFormat") private void startRecord() {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date currentDate = new Date(System.currentTimeMillis());
		filePath = format.format(currentDate) + ".wmv";
		
		file = new File(FILE_PATH + filePath);
		try {
			if (!file.exists())
				file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		handler = new Handler();
		
		mediaRecorder = new MediaRecorder();
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mediaRecorder.setVideoSize(320, 240);
		mediaRecorder.setVideoFrameRate(5);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		mediaRecorder.setOutputFile(file.getAbsolutePath());
		mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());	//必须要加
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaRecorder.start();
	}
	
	private Runnable ReflashTime = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			sec++;
			handler.postDelayed(ReflashTime, 1000);
			if (sec == 60){
				sec = 0;
				min++;
			}
			
			if (min == 60){
				min = 0;
				hou++;
			}
			
			timerView.setText(timeFormat(hou) + ":" + timeFormat(min) + ":" + timeFormat(sec));
		}
		
	};
	
	private String timeFormat(int time){
		if (time / 10 == 0){
			return "0" + time;
		}else{
			return time + "";
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mediaRecorder != null){
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK){
			if (keyCode == KeyEvent.KEYCODE_BACK){
				if (null != file)
					file.delete();
				finish();
				return true;
			}
		}
		return false;
	}
}
