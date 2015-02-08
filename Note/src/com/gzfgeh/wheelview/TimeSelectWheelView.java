package com.gzfgeh.wheelview;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.gzfgeh.wheelview.WheelView.OnWheelChangedListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.gzfgeh.note.R;

public class TimeSelectWheelView extends RelativeLayout implements OnWheelChangedListener, OnCheckedChangeListener {
	private RelativeLayout titleView;
	private TextView titleYearText;
	private TextView titleMonthText;
	private TextView titleDayText;
	private TextView titleHourText;
	private TextView titleMinuteText;
	private TextView titleSecondText;
	
	private LinearLayout wheelDateViews;
	private LinearLayout wheelTimeViews;
	private ToggleButton toggleButton;
	private boolean dateOrTimeFlag = true;
	private WheelView wheelYear;
	private WheelView wheelMonth;
	private WheelView wheelDay;
	private WheelView wheelHour;
	private WheelView wheelMinute;
	private WheelView wheelSecond;
	
	private String[] years = new String[100];
	private String[] months = new String[12];
	private String[] tinyDays = new String[29];
	private String[] smallDays = new String[30];
	private String[] bigDays = new String[31];
	private String[] hours = new String[24];
	private String[] minutes = new String[60];
	private String[] seconds = new String[60];
	private String currentYear;
	private String currentMonth;
	private String currentDay;
	private String currentHour;
	private String currentMinute;
	private String currentSecond;
	private StrericWheelAdapter yearsAdapter;
	private StrericWheelAdapter monthsAdapter;
	private StrericWheelAdapter tinyDaysAdapter;
	private StrericWheelAdapter smallDaysAdapter;
	private StrericWheelAdapter bigDaysAdapter;
	private StrericWheelAdapter hoursAdapter;
	private StrericWheelAdapter minutesAdapter;
	private StrericWheelAdapter secondAdapter;
	
	public TimeSelectWheelView(Context context){
		super(context);
		initLayout(context);
	}
	
	public TimeSelectWheelView(Context context, AttributeSet attrs){
		super(context, attrs);
		initLayout(context);
	}
	public TimeSelectWheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initLayout(context);
	}

	private void initLayout(Context context) {
		// TODO Auto-generated method stub
		LayoutInflater.from(context).inflate(R.layout.time_select_layout, this, true);
		titleView = (RelativeLayout) findViewById(R.id.time_select_title);
		toggleButton = (ToggleButton) findViewById(R.id.time_select_toggle_button);
		titleYearText = (TextView) findViewById(R.id.time_select_year_text);
		titleMonthText = (TextView) findViewById(R.id.time_select_month_text);
		titleDayText = (TextView) findViewById(R.id.time_select_day_text);
		titleHourText = (TextView) findViewById(R.id.time_select_hour_text);
		titleMinuteText = (TextView) findViewById(R.id.time_select_minute_text);
		titleSecondText = (TextView) findViewById(R.id.time_select_second_text);
		
		wheelDateViews = (LinearLayout) findViewById(R.id.time_select_date_wheel_views);
		wheelTimeViews = (LinearLayout) findViewById(R.id.time_select_time_wheel_views);
		wheelYear = (WheelView) findViewById(R.id.time_select_wheel_year);
		wheelMonth = (WheelView) findViewById(R.id.time_select_wheel_month);
		wheelDay = (WheelView) findViewById(R.id.time_select_wheel_day);
		wheelHour = (WheelView) findViewById(R.id.time_select_wheel_hour);
		wheelMinute = (WheelView) findViewById(R.id.time_select_wheel_minute);
		wheelSecond = (WheelView) findViewById(R.id.time_select_wheel_second);
		
		wheelYear.setSoundEffectsEnabled(true);
		wheelMonth.setSoundEffectsEnabled(true);
		wheelDay.setSoundEffectsEnabled(true);
		wheelHour.setSoundEffectsEnabled(true);
		wheelMinute.setSoundEffectsEnabled(true);
		wheelSecond.setSoundEffectsEnabled(true);
		
		wheelYear.addChangingListener(this);
		wheelMonth.addChangingListener(this);
		wheelDay.addChangingListener(this);
		wheelHour.addChangingListener(this);
		wheelMinute.addChangingListener(this);
		wheelSecond.addChangingListener(this);
		toggleButton.setOnCheckedChangeListener(this);
		setInitData();
	}

	@SuppressLint("SimpleDateFormat") private void setInitData() {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date currentDate = new Date(System.currentTimeMillis());
		String date = format.format(currentDate);
		String[] times = date.split("-");
		currentYear = times[0];
		currentMonth = times[1];
		currentDay = times[2];
		currentHour = times[3];
		currentMinute = times[4];
		currentSecond = times[5];
		
		for (int i = 0; i < years.length; i++) {
			years[i] = 1960 + i + " 年";
		}
		for (int i = 0; i < months.length; i++) {
			months[i] = 1 + i + " 月";
		}
		for (int i = 0; i < tinyDays.length; i++) {
			tinyDays[i] = 1 + i + " 日";
		}
		for (int i = 0; i < smallDays.length; i++) {
			smallDays[i] = 1 + i + " 日";
		}
		for (int i = 0; i < bigDays.length; i++) {
			bigDays[i] = 1 + i + " 日";
		}
		for (int i = 0; i < hours.length; i++) {
			hours[i] = i + " 点";
		}
		for (int i = 0; i < minutes.length; i++) {
			minutes[i] = i + " 分";
		}
		for (int i = 0; i < seconds.length; i++) {
			seconds[i] = i + " 秒";
		}

		yearsAdapter = new StrericWheelAdapter(years);
		monthsAdapter = new StrericWheelAdapter(months);
		tinyDaysAdapter = new StrericWheelAdapter(tinyDays);
		smallDaysAdapter = new StrericWheelAdapter(smallDays);
		bigDaysAdapter = new StrericWheelAdapter(bigDays);
		hoursAdapter = new StrericWheelAdapter(hours);
		minutesAdapter = new StrericWheelAdapter(minutes);
		secondAdapter = new StrericWheelAdapter(seconds);
		
		wheelHour.setAdapter(hoursAdapter);
		wheelHour.setCurrentItem(Integer.valueOf(currentHour));
		wheelHour.setCyclic(true);
		wheelHour.setSoundEffectsEnabled(true);
		wheelMinute.setAdapter(minutesAdapter);
		wheelMinute.setCurrentItem(Integer.valueOf(currentMinute));
		wheelMinute.setCyclic(true);
		wheelMinute.setSoundEffectsEnabled(true);
		wheelSecond.setAdapter(secondAdapter);
		wheelSecond.setCurrentItem(Integer.valueOf(currentSecond));
		wheelSecond.setCyclic(true);
		wheelSecond.setSoundEffectsEnabled(true);
		wheelYear.setAdapter(yearsAdapter);
		wheelYear.setCurrentItem(Integer.valueOf(currentYear) - 1960);
		wheelYear.setCyclic(true);
		wheelYear.setSoundEffectsEnabled(true);
		wheelMonth.setAdapter(monthsAdapter);
		wheelMonth.setCurrentItem(Integer.valueOf(currentMonth) - 1);
		wheelMonth.setCyclic(true);
		wheelMonth.setSoundEffectsEnabled(true);
		wheelDay.setAdapter(smallDaysAdapter);
		wheelDay.setCurrentItem(Integer.valueOf(currentDay) - 1);
		wheelDay.setCyclic(true);
		wheelDay.setSoundEffectsEnabled(true);
		
		titleYearText.setText(wheelYear.getCurrentItemValue().split(" ")[0]);
		titleMonthText.setText(wheelMonth.getCurrentItemValue().split(" ")[0]);
		titleDayText.setText(wheelDay.getCurrentItemValue().split(" ")[0]);
		titleHourText.setText(displayTime(wheelHour.getCurrentItemValue().split(" ")[0]));
		titleMinuteText.setText(displayTime(wheelMinute.getCurrentItemValue().split(" ")[0]));
		titleSecondText.setText(displayTime(wheelSecond.getCurrentItemValue().split(" ")[0]));
		
		setWheelDateViewsVisibility(View.VISIBLE);
		setWheelTimeViewsVisibility(View.GONE);
	}
	
	
	public void setCurrentYear(String currentYear) {
		wheelYear.setCurrentItem(Integer.valueOf(currentYear) - 1960);
	}

	public void setCurrentMonth(String currentMonth) {
		wheelMonth.setCurrentItem(Integer.valueOf(currentMonth) - 1);
	}

	public void setCurrentDay(String currentDay) {
		wheelDay.setCurrentItem(Integer.valueOf(currentDay) - 1);
	}
	
	public void setCurrentHour(String currentHour) {
		wheelYear.setCurrentItem(Integer.valueOf(currentHour));
	}

	public void setCurrentMinute(String currentMinute) {
		wheelMonth.setCurrentItem(Integer.valueOf(currentMinute));
	}

	public void setCurrentSecond(String currentSecond) {
		wheelDay.setCurrentItem(Integer.valueOf(currentSecond));
	}
	
	public String getSelectTime(){
		return titleYearText.getText().toString().trim() + "-" +
				titleMonthText.getText().toString().trim() + "-" +
				 titleDayText.getText().toString().trim() + "-" +
				  titleHourText.getText().toString().trim() + "-" + 
				   titleMinuteText.getText().toString().trim() + "-" +
				    titleSecondText.getText().toString().trim();
	}
	
	public void setTitleClick(OnClickListener listener){
		titleView.setOnClickListener(listener);
	}
	
	public void setWheelDateViewsVisibility(int visibility){
		wheelDateViews.setVisibility(visibility);
	}
	
	public void setWheelTimeViewsVisibility(int visibility){
		wheelTimeViews.setVisibility(visibility);
	}
	
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		String trim = null;
		switch (wheel.getId()) {
		case R.id.time_select_wheel_year:
			trim = wheelYear.getCurrentItemValue().trim();
			trim = trim.split(" ")[0];
			titleYearText.setText(trim);
			if (isLeapYear(trim)) {
				if (Integer.valueOf(currentMonth) == 2) {
					wheelDay.setAdapter(tinyDaysAdapter);
				} else if(isBigMonth(Integer.valueOf(currentMonth))){
					wheelDay.setAdapter(bigDaysAdapter);
				} else {
					wheelDay.setAdapter(smallDaysAdapter);
				}
			} else if(isBigMonth(Integer.valueOf(currentMonth))){
				wheelDay.setAdapter(bigDaysAdapter);
			} else {
				wheelDay.setAdapter(smallDaysAdapter);
			}
			break;
			
		case R.id.time_select_wheel_month:
			currentMonth = wheelMonth.getCurrentItemValue().trim();
			currentMonth = currentMonth.split(" ")[0];
			titleMonthText.setText(currentMonth);
			switch (Integer.valueOf(currentMonth)) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				wheelDay.setAdapter(bigDaysAdapter);
				break;
			case 2:
				String yearString = wheelYear.getCurrentItemValue().trim();
				yearString = yearString.split(" ")[0];
				
				if (isLeapYear(yearString)) {
					wheelDay.setAdapter(tinyDaysAdapter);
				} else {
					wheelDay.setAdapter(smallDaysAdapter);
				}
				break;
				
			default:
				wheelDay.setAdapter(smallDaysAdapter);
				break;
			}
			break;
		case R.id.time_select_wheel_day:
			titleDayText.setText(wheelDay.getCurrentItemValue().trim().split(" ")[0]);
			break;
			
		case R.id.time_select_wheel_hour:
			titleHourText.setText(displayTime(wheelHour.getCurrentItemValue().trim().split(" ")[0]));
			break;
			
		case R.id.time_select_wheel_minute:
			titleMinuteText.setText(displayTime(wheelMinute.getCurrentItemValue().trim().split(" ")[0]));
			break;
			
		case R.id.time_select_wheel_second:
			titleSecondText.setText(displayTime(wheelSecond.getCurrentItemValue().trim().split(" ")[0]));
			break;
		}
	}

	private boolean isBigMonth(int month){
		boolean isBigMonth = false;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			isBigMonth = true;
			break;

		default:
			isBigMonth = false;
			break;
		}
		return isBigMonth;
	}
	
	private boolean isLeapYear(String year) {
		int temp = Integer.parseInt(year);
		return temp % 4 == 0 ? true : false;
	}
	
	private String displayTime(String time){
		int timeValue = Integer.valueOf(time);
		if (timeValue < 10)
			return "0" + timeValue;
		else
			return time;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
		if (dateOrTimeFlag){
			setWheelDateViewsVisibility(View.GONE);
			setWheelTimeViewsVisibility(View.VISIBLE);
		}else{
			setWheelDateViewsVisibility(View.VISIBLE);
			setWheelTimeViewsVisibility(View.GONE);
		}
		dateOrTimeFlag = !dateOrTimeFlag;	
	}
}
