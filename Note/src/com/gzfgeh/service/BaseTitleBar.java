package com.gzfgeh.service;

import java.io.File;

import com.gzfgeh.data.OperationSQLiteItem;
import com.gzfgeh.note.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseTitleBar extends Activity {
	private View titleView;
	private TextView titleText;
	private ImageButton leftBtn,rightBtn;
	private ImageView statusView;
	private View contentView;
	private RelativeLayout contentRelativeLayout;
	private RelativeLayout titleBarRelativeLayout;
	
	private String content;
	private File file;
	private boolean btnFlag = true;
	private boolean writeFileFlag = false;
	private OperationSQLiteItem operationSQLiteItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.base);
		
		statusView = (ImageView) findViewById(R.id.ok);
		titleText = (TextView) findViewById(R.id.titlebar_text);
		leftBtn = (ImageButton) findViewById(R.id.left_btn);
		rightBtn = (ImageButton) findViewById(R.id.right_btn);
		contentRelativeLayout = (RelativeLayout) findViewById(R.id.content);
		titleBarRelativeLayout = (RelativeLayout) findViewById(R.id.titlebar);
		
		operationSQLiteItem = new OperationSQLiteItem(this);
	}
	
	//add content layout
	public void setContentLayout(int layoutResId) {
	       LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	       contentView = inflater.inflate(layoutResId, null);
	       if (null != contentRelativeLayout)
	    	   contentRelativeLayout.addView(contentView);
	} 
	
	
	public void setContentLayout(View view) {
		// TODO Auto-generated method stub
		if (null != contentRelativeLayout)
			contentRelativeLayout.addView(view);
	}
	
	public View getContentView(){
		return contentView;
	}
	
	public ImageButton getLeftBtn(){
		return leftBtn;
	}
	
	public ImageButton getRightBtn(){
		return rightBtn;
	}
	
	public void setTitle(String title){
		if (null != titleText)
			titleText.setText(title);
	}
	
	public void setTitle(int resId){
		titleText.setText(getString(resId));
	}
	
	public void setLeftBtnRes(int resId){
		if (null != leftBtn)
			leftBtn.setBackgroundResource(resId);
	}
	
	public void setRightBtnRes(int resId){
		if (null !=rightBtn)
			rightBtn.setBackgroundResource(resId);
	}
	
	public void hideTitleView(){
		if (null != titleView)
			titleView.setVisibility(View.GONE);
	}
	
	public void hideTitleText(){
		if (null != titleText)
			titleText.setVisibility(View.GONE);
	}
	
	public void hideLeftBtn(){
		if (null != leftBtn)
			leftBtn.setVisibility(View.GONE);
	}
	
	public void hideRightBtn(){
		if (null != rightBtn)
			rightBtn.setVisibility(View.GONE);
	}
	
	public void displayTitleText(){
		if (null != titleText)
			titleText.setVisibility(View.VISIBLE);
	}
	
	public void displayLeftBtn(){
		if (null != leftBtn)
			leftBtn.setVisibility(View.VISIBLE);
	}
	
	public void displayRightBtn(){
		if (null != rightBtn)
			rightBtn.setVisibility(View.VISIBLE);
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public void setFlag(boolean flag){
		this.btnFlag = flag;
	}
	
	public void setWriteFileFlag(boolean flag){
		this.writeFileFlag = flag;
	}
	
	public void setTitleBarBackground(int color){
		titleBarRelativeLayout.setBackgroundColor(color);
	}
	
	@SuppressLint("NewApi") public void setTitleBarResource(int resid){
		titleBarRelativeLayout.setBackgroundResource(resid);
	}
	
	public void displayStatusView(){
		if (null != statusView)
			statusView.setVisibility(View.VISIBLE);
	}
	
	public void hideStatusView(){
		if (null != statusView)
			statusView.setVisibility(View.GONE);
	}
	
	@SuppressLint("InflateParams") private void encrypt(){
		Builder dialog = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout)layoutInflater.inflate(R.layout.encrypt, null);
		dialog.setView(layout);
		final EditText encty = (EditText) layout.findViewById(R.id.encryption);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String encryption = encty.getText().toString();
				if (encryption.length() != 6){
					AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
					writeFileFlag = true;
					finish();
				}
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}
	
	public void onClickTitleBtn(View v) {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(new long[]{1,100,0,0}, -1);
		
		switch (v.getId()) {
		case R.id.left_btn:
			if (null != file)
				file.delete();
			finish();
			break;
		case R.id.right_btn:
			if (btnFlag != false){
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
						writeFileFlag = true;
						finish();
					}
				})
				.create();
				dialog.show();
				dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失 
			}
			break;
		default:
			break;
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if ((writeFileFlag == false) && (null != file))
			file.delete();
	}
	
	
}
