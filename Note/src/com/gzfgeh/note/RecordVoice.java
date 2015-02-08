package com.gzfgeh.note;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gzfgeh.service.BaseTitleBar;
import com.gzfgeh.service.MyMediaRecorder;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class RecordVoice extends BaseTitleBar {
	private static final String TAG = "RecordVoice";

	private static final String VOCIE = "voice";
	private final static String FILE_PATH=Environment.getExternalStorageDirectory()+
				File.separator + Display.NOTE + File.separator + VOCIE + File.separator;
	
	private ImageView voiceImage;
	private ImageView talkStrong;
	private int[] location = new int[2];
	
	private View talkOKView;
	private View talkCancleView;
	
	private float yDown;
	private float yMove;
	
	private static final String CONTENT = "音频文件";
	
	private MyMediaRecorder myMediaRecorder;
	private File file;
	@SuppressLint("HandlerLeak") private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == MyMediaRecorder.HANDLER_MSG){
				int strong = (Integer)msg.obj;
				if (strong < 60)
					talkStrong.setBackgroundResource(R.drawable.a);
				else if (strong >= 60 && strong < 70)
					talkStrong.setBackgroundResource(R.drawable.b);
				else if (strong >= 70 && strong < 80)
					talkStrong.setBackgroundResource(R.drawable.c);
				else if (strong >= 80 && strong < 90)
					talkStrong.setBackgroundResource(R.drawable.d);
				else
					talkStrong.setBackgroundResource(R.drawable.e);
				
			}
		}
		
	};
	
	@SuppressLint({ "ClickableViewAccessibility", "SimpleDateFormat" }) @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.record_voice);
		//文件夹是否存在
		File fold = new File(FILE_PATH);
		if (!fold.exists())
			fold.mkdirs();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date currentDate = new Date(System.currentTimeMillis());
		String name = format.format(currentDate) + ".3gp";
		
		file = new File(FILE_PATH + name);
		try {
			if (!file.exists())
				file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setContent(CONTENT);
		setFile(file);
		setTitle("录音");
		
		myMediaRecorder = new MyMediaRecorder(file,handler);
		
		voiceImage = (ImageView) findViewById(R.id.voice);
		voiceImage.setBackgroundResource(R.drawable.talk);
		final AnimationDrawable voiceAnimation = (AnimationDrawable) voiceImage.getBackground();
		voiceAnimation.setOneShot(false);	//一直
		voiceAnimation.start();
		
		talkStrong = (ImageView) findViewById(R.id.talk_strong);
		
		talkOKView = findViewById(R.id.talk_ok);
		talkCancleView = findViewById(R.id.talk_cancle);

		voiceImage.setOnLongClickListener(new OnLongClickListener() {
			
			public boolean onLongClick(View view) {
				// TODO Auto-generated method stub
				Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
				vibrator.vibrate(new long[]{1,100,0,0}, -1);
				
				voiceAnimation.stop();
				talkOKView.setVisibility(View.VISIBLE);
				talkCancleView.setVisibility(View.GONE);
				
				voiceImage.getLocationOnScreen(location);
				yDown = location[1] + 120;
				
				talkStrong.setBackgroundResource(R.drawable.e);
				myMediaRecorder.startRecord();
				Log.i(TAG, "LongClick");
				
				view.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View view, MotionEvent event) {
						// TODO Auto-generated method stub
						
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							Log.i(TAG, "down");
							break;
							
						case MotionEvent.ACTION_MOVE:
							yMove = event.getRawY();
							if (yDown - yMove > 200){
								talkOKView.setVisibility(View.GONE);
								talkCancleView.setVisibility(View.VISIBLE);
							}else{
								talkOKView.setVisibility(View.VISIBLE);
								talkCancleView.setVisibility(View.GONE);
							}
							Log.i(TAG, "move");
							break;
							
						case MotionEvent.ACTION_UP:
							if (yDown - yMove < 200){
								displayRightBtn();
								displayStatusView();
							}
							else
								file.delete();
							
							myMediaRecorder.stopRecord();	
							talkOKView.setVisibility(View.GONE);
							talkCancleView.setVisibility(View.GONE);
							voiceAnimation.start();
							Log.i(TAG, "up");
							break;

						default:
							break;
						}
						return false;
					}
				});
				
				return false;
			}
		});
		
	}
	
}
