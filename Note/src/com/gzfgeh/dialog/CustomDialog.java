package com.gzfgeh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzfgeh.note.R;

public class CustomDialog extends Dialog implements DialogInterface{
	@SuppressWarnings("unused")
	private Context context;
	private volatile static CustomDialog instance;
	
	private View customDialogView;
	private LinearLayout customDialogParentPanel;
	@SuppressWarnings("unused")
	private RelativeLayout customDialogMain;
	private LinearLayout customDialogTopPanel;
	private FrameLayout customDialogMsgView;
	
	private FrameLayout customDialogTitleCenter;
	private TextView customDialogTitleCenterText;
	private ImageView customDialogTitleCenterImage;
	
	private FrameLayout customDialogTitleLeft;
	private TextView customDialogTitleLeftText;
	private ImageView customDialogTitleLeftImage;
	
	private FrameLayout customDialogTitleRight;
	private TextView customDialogTitleRightText;
	private ImageView customDialogTitleRightImage;
	
	private LinearLayout customDialogCenterMsgDefaultView;
	private TextView customDialogCenterMsgDefaultText;
	
	private FrameLayout customDialogTitleNextLayout;
	private View customDialogTitleDivider;
	
	private Button customDialogBottomLeftBtn;
	private Button customDialogBottomRightBtn;
	
	private final String defTextColor = "#FFFFFFFF";
	private final String defDividerColor = "#11000000";
	private final String defMsgColor = "#FFFFFFFF";
	private final String defDialogColor = "#FFFFFFFF";
	private int windowWidth = 0;
	private int windowHeight = 0;
	@SuppressWarnings("unused")
	private boolean isDisappear = true;
	
	public CustomDialog(Context context){
		super(context);
		initDialog(context);
	}
	
	public CustomDialog(Context context, int theme) {
		super(context, theme);
		initDialog(context);
	}
	
	public static CustomDialog getInstance(Context context){
		if (null == instance){
			synchronized (CustomDialog.class) {
				if (null == instance){
					instance = new CustomDialog(context,R.style.custom_dialog);
				}
			}
		}
		
		return instance;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();

		if (windowWidth == 0 || windowHeight == 0) {
			params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		} else {
			params.width = windowWidth;
			params.height = windowHeight;
		}
		getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);
		//下面这一句一定要加上
		getWindow().setBackgroundDrawableResource(
				R.drawable.background_transparent);
	}
	
	private void initDialog(Context context) {
		// TODO Auto-generated method stub
		customDialogView = View.inflate(context, R.layout.custom_dialog, null);
		customDialogParentPanel = (LinearLayout) customDialogView.
							findViewById(R.id.custom_dialog_parent_panel);
		
		customDialogMain = (RelativeLayout) customDialogView.
							findViewById(R.id.custom_dialog_main);
		
		customDialogTopPanel = (LinearLayout) customDialogView.
							findViewById(R.id.custom_dialog_top_panel);
		
		customDialogTitleCenter = (FrameLayout) customDialogView.
							findViewById(R.id.custom_dialog_title_center);
		
		customDialogMsgView = (FrameLayout) customDialogView.
							findViewById(R.id.custom_dialog_msg_view);
		
		customDialogTitleCenterText = (TextView) customDialogView.
							findViewById(R.id.custom_dialog_title_center_text);
		
		customDialogTitleCenterImage = (ImageView) customDialogView.
							findViewById(R.id.custom_dialog_title_center_icon);
		
		customDialogTitleLeft = (FrameLayout) customDialogView.
							findViewById(R.id.custom_dialog_title_left);
		
		customDialogTitleLeftText = (TextView) customDialogView.
							findViewById(R.id.custom_dialog_title_left_text);
		
		customDialogTitleLeftImage = (ImageView) customDialogView.
							findViewById(R.id.custom_dialog_title_left_icon);
		
		customDialogTitleRight = (FrameLayout) customDialogView.
							findViewById(R.id.cutsom_dialog_title_right);
		
		customDialogTitleRightText = (TextView) customDialogView.
							findViewById(R.id.cutsom_dialog_title_right_text);
		
		customDialogTitleRightImage = (ImageView) customDialogView.
							findViewById(R.id.cutsom_dialog_title_right_icon);
		
		customDialogTitleNextLayout = (FrameLayout) customDialogView.
							findViewById(R.id.custom_dialog_title_next_layout);
		
		customDialogTitleDivider = customDialogView.
							findViewById(R.id.custom_dialog_title_divider);
		
		customDialogCenterMsgDefaultView = (LinearLayout) customDialogView.
							findViewById(R.id.custom_dialog_center_msg_default_view);
		
		customDialogCenterMsgDefaultText = (TextView) customDialogView.
							findViewById(R.id.custom_dialog_center_msg_default_text);
		
		customDialogBottomLeftBtn = (Button) customDialogView.
							findViewById(R.id.custom_dialog_bottom_left_btn);
		
		customDialogBottomRightBtn = (Button) customDialogView.
				findViewById(R.id.custom_dialog_bottom_right_btn);
		
//		customDialogMain.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View view) {
//				// TODO Auto-generated method stub
//				if(isDisappear)
//					dismiss();
//			}
//		});
		
		setContentView(customDialogView);
		toDefault();
	}
	
	public void toDefault(){
		customDialogTitleCenterText.setTextColor(Color.parseColor(defTextColor));
		customDialogTitleDivider.setBackgroundColor(Color.parseColor(defDividerColor));
		customDialogCenterMsgDefaultText.setTextColor(Color.parseColor(defMsgColor));
		customDialogParentPanel.setBackgroundColor(Color.parseColor(defDialogColor));
	}
	
	public CustomDialog setCustomDialogWindow(int windowWidth, int windowHeight){
		this.windowHeight = windowHeight;
		this.windowWidth = windowWidth;
		return this;
	}
	
	public CustomDialog setCustomDialogBackground(String colorString){
		customDialogParentPanel.setBackgroundColor(Color.parseColor(colorString));
		return this;
	}
	
	public CustomDialog setCustomDialogVisibility(int visibility){
		customDialogParentPanel.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleBackground(String colorString){
		customDialogTopPanel.setBackgroundColor(Color.parseColor(colorString));
		return this;
	}
	
	public CustomDialog setCustomDialogTitleVisibility(int visibility){
		customDialogTopPanel.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleDividerVisibility(int visibility){
		customDialogTitleDivider.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogCenterMessageDefaultVisibility(int visibility){
		customDialogCenterMsgDefaultView.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogCenterMessageDefaultText(String text){
		customDialogCenterMsgDefaultText.setText(text);
		return this;
	}
	
	public CustomDialog setCustomDialogCenterMessageDefaultTextColor(String colorString){
		customDialogCenterMsgDefaultText.setTextColor(Color.parseColor(colorString));
		return this;
	}
	
	public CustomDialog setCustomDialogMsgVisibility(int visibility){
		customDialogMsgView.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogDividerColor(String colorString){
		customDialogTitleDivider.setBackgroundColor(Color.parseColor(colorString));
		return this;
	}
	
	public CustomDialog setCustomDialogTitleCenterTextVisibility(int visibility){
		customDialogTitleCenterText.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleCenterText(String title){
		customDialogTitleCenterText.setText(title);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleCenterTextColor(String colorString){
		customDialogTitleCenterText.setTextColor(Color.parseColor(colorString));
		return this;
	}
	
	public CustomDialog setCustomDialogTitleCenterImageVisibility(int visibility){
		customDialogTitleCenterImage.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleCenterImage(int resid){
		customDialogTitleCenterImage.setBackgroundResource(resid);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleCenterVisibility(int visibility){
		customDialogTitleCenter.setVisibility(visibility);
		return this;
	}
	
	
	public CustomDialog setCustomDialogTitleLeftTextVisibility(int visibility){
		customDialogTitleLeftText.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleLeftText(String title){
		customDialogTitleLeftText.setText(title);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleLeftTextColor(String colorString){
		customDialogTitleLeftText.setTextColor(Color.parseColor(colorString));
		return this;
	}
	
	public CustomDialog setCustomDialogTitleLeftImageVisibility(int visibility){
		customDialogTitleLeftImage.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleLeftImage(int resid){
		customDialogTitleLeftImage.setBackgroundResource(resid);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleLeftVisibility(int visibility){
		customDialogTitleLeft.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleLeftTextClick(View.OnClickListener click){
		customDialogTitleLeftText.setOnClickListener(click);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleRightTextVisibility(int visibility){
		customDialogTitleRightText.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleRightText(String title){
		customDialogTitleRightText.setText(title);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleRightTextColor(String colorString){
		customDialogTitleRightText.setTextColor(Color.parseColor(colorString));
		return this;
	}
	
	public CustomDialog setCustomDialogTitleRightImageVisibility(int visibility){
		customDialogTitleRightImage.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleRightImage(int resid){
		customDialogTitleRightImage.setBackgroundResource(resid);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleRightVisibility(int visibility){
		customDialogTitleRight.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleRightTextClick(View.OnClickListener click){
		customDialogTitleRightText.setOnClickListener(click);
		return this;
	}
	
	public CustomDialog setCustomDialogBottomLeftBtnResource(int resid){
		customDialogBottomLeftBtn.setBackgroundResource(resid);
		return this;
	}
	
	public CustomDialog setCustomDialogBottomLeftBtnVisibility(int visibility){
		customDialogBottomLeftBtn.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogBottomLeftBtnText(String text){
		customDialogBottomLeftBtn.setText(text);
		return this;
	}
	
	public CustomDialog setCustomDialogBottomLeftBtnClick(View.OnClickListener click){
		customDialogBottomLeftBtn.setOnClickListener(click);
		return this;
	}
	
	public CustomDialog setCustomDialogBottomRightBtnResource(int resid){
		customDialogBottomRightBtn.setBackgroundResource(resid);
		return this;
	}
	
	public CustomDialog setCustomDialogBottomRightBtnVisibility(int visibility){
		customDialogBottomRightBtn.setVisibility(visibility);
		return this;
	}
	
	public CustomDialog setCustomDialogBottomRightBtnText(String text){
		customDialogBottomRightBtn.setText(text);
		return this;
	}
	
	public CustomDialog setCustomDialogBottomRightBtnClick(View.OnClickListener click){
		customDialogBottomRightBtn.setOnClickListener(click);
		return this;
	}
	
	public CustomDialog setCustomDialogMessageView(int resource, Context context){
		View view = View.inflate(context, resource, null);
		if (customDialogMsgView.getChildCount() > 0){
			customDialogMsgView.removeAllViews();
		}
		customDialogMsgView.addView(view);
		return this;
	}
	
	public CustomDialog setCustomDialogMessageView(View view, Context context){
		if (customDialogMsgView.getChildCount() > 0){
			customDialogMsgView.removeAllViews();
		}
		customDialogMsgView.addView(view);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleNextLayout(int resource, Context context){
		View view = View.inflate(context, resource, null);
		if (customDialogTitleNextLayout.getChildCount() > 0){
			customDialogTitleNextLayout.removeAllViews();
		}
		customDialogTitleNextLayout.addView(view);
		return this;
	}
	
	public CustomDialog setCustomDialogTitleNextLayout(View view, Context context){
		if (customDialogTitleNextLayout.getChildCount() > 0){
			customDialogTitleNextLayout.removeAllViews();
		}
		customDialogTitleNextLayout.addView(view);
		return this;
	}
	
	public CustomDialog setCustomDialogDisappearOnTouchOutside(boolean isDisappear){
		this.isDisappear = isDisappear;
		this.setCanceledOnTouchOutside(isDisappear);
		return this;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}
	
	public FrameLayout getCustomDialogLayout(){
		return customDialogMsgView;
	}
	
}
