package com.gzfgeh.dialog;

import com.gzfgeh.wheelview.TimeSelectWheelView;
import com.gzfgeh.note.R;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class DateTimeSelectorDialogBuilder extends CustomDialog{
	private Context context;
	private RelativeLayout dateTimeSelectorDialogLayout;
	private TimeSelectWheelView timeSelectWheelView;
	private static OnSureClickListener onSureClickListener;
	
	public interface OnSureClickListener{
		void setOnSureClickListener(TimeSelectWheelView wheelView);
	}
	
	public DateTimeSelectorDialogBuilder(Context context){
		super(context);
		this.context = context;
		initView();
	}

	public DateTimeSelectorDialogBuilder(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}
	
	@SuppressLint("InflateParams") private void initView() {
		// TODO Auto-generated method stub
		dateTimeSelectorDialogLayout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.date_time_selector_dialog, null);
		timeSelectWheelView = (TimeSelectWheelView) dateTimeSelectorDialogLayout.findViewById(R.id.wheelview);
		setEditTextDialogProperties();
	}

	@SuppressLint("InlinedApi") private void setEditTextDialogProperties() {
		// TODO Auto-generated method stub
		this.setCustomDialogWindow(800, LayoutParams.WRAP_CONTENT)
			.setCustomDialogTitleLeftVisibility(View.VISIBLE)
			.setCustomDialogTitleLeftText("确定")
			.setCustomDialogTitleLeftTextClick(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (null != onSureClickListener)
						onSureClickListener.setOnSureClickListener(timeSelectWheelView);
					dismiss();
				}
			})
			.setCustomDialogTitleCenterTextColor("#000000")
			.setCustomDialogTitleLeftTextColor("#4169e1")
			.setCustomDialogTitleRightVisibility(View.VISIBLE)
			.setCustomDialogTitleCenterText("选择")
			.setCustomDialogTitleRightText("取消")
			.setCustomDialogTitleRightTextClick(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			})
			.setCustomDialogTitleRightTextColor("#4169e1")
			.setCustomDialogCenterMessageDefaultVisibility(View.GONE)
			.setCustomDialogMsgVisibility(View.VISIBLE)
			.setCustomDialogMessageView(dateTimeSelectorDialogLayout, context)
			.setCustomDialogBottomLeftBtnVisibility(View.GONE)
			.setCustomDialogBottomRightBtnVisibility(View.GONE);
	}

	@SuppressWarnings("static-access")
	public void setOnSureClickListener(OnSureClickListener onSureClickListener) {
		this.onSureClickListener = onSureClickListener;
	}

}
