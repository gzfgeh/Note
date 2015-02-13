package com.gzfgeh.note;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gzfgeh.data.OperationSQLiteItem;
import com.gzfgeh.music.MusicBroadcastReceiver;
import com.gzfgeh.service.BaseTitleBar;
import com.gzfgeh.service.MyMediaRecorder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
	
	private View fileDisplay;
	private TextView filePath;
	@SuppressWarnings("unused")
	private Button clickNote;
	private int oldID;
	
	private MusicBroadcastReceiver musicBroadcastReceiver;
	
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
		
		talkStrong = (ImageView) findViewById(R.id.talk_strong);
		talkOKView = findViewById(R.id.talk_ok);
		talkCancleView = findViewById(R.id.talk_cancle);
		clickNote = (Button) findViewById(R.id.click_note);
		fileDisplay = findViewById(R.id.file_display);
		filePath = (TextView) findViewById(R.id.file_path);
		
		Intent intent = getIntent();
		oldID = intent.getIntExtra("ItemID", -1);
		if (oldID != -1){
			if (file != null)
				file.delete();
			
			OperationSQLiteItem operationSQLiteItem = new OperationSQLiteItem(this);
			final String path = operationSQLiteItem.queryContentUri(oldID);
			String[] pathSplit = path.split("/");
			String fileName = pathSplit[pathSplit.length - 1];
			filePath.setText(fileName);
			fileDisplay.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri uri = Uri.parse(path);
					intent.setDataAndType(uri, "audio/*");
					startActivity(intent);
				}
			});
		}else{
			fileDisplay.setVisibility(View.GONE);
			myMediaRecorder = new MyMediaRecorder(file,handler);
			
			voiceImage = (ImageView) findViewById(R.id.voice);
			voiceImage.setBackgroundResource(R.drawable.talk);
			final AnimationDrawable voiceAnimation = (AnimationDrawable) voiceImage.getBackground();
			voiceAnimation.setOneShot(false);	//一直
			voiceAnimation.start();
			
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		musicBroadcastReceiver = new MusicBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.queuechanged");
        intentFilter.addAction("com.android.music.playbackcomplete");
        intentFilter.addAction("com.android.music.playstatechanged");
        registerReceiver(musicBroadcastReceiver, intentFilter);
        super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (musicBroadcastReceiver != null)
			unregisterReceiver(musicBroadcastReceiver);
		super.onPause();
	}
	
	
	
}
