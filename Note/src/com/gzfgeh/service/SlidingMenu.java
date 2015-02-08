package com.gzfgeh.service;

import com.gzfgeh.note.R;
import com.nineoldandroids.view.ViewHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {
	private int screenWidth;			//屏幕宽度
	private int menuRightPadding;
	private int menuWidth;				//菜单宽度
	private int halfMenuWidth;
	private boolean once;
	private boolean isOpen;
	private ViewGroup menu;
	private ViewGroup mainContent;
	
	
	public SlidingMenu(Context context,AttributeSet attrs){
		this(context,attrs,0);
	}
	
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle)  
    {  
        super(context, attrs, defStyle);
        
        WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		screenWidth = outMetrics.widthPixels;
  
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,  
                R.styleable.SlidingMenu, defStyle, 0);
        int n = a.getIndexCount();  
        for (int i = 0; i < n; i++)  
        {  
            int attr = a.getIndex(i);  
            switch (attr)  
            {  
            case R.styleable.SlidingMenu_rightPadding:  
                // 默认50  
                menuRightPadding = a.getDimensionPixelSize(attr,  
                        (int) TypedValue.applyDimension(  
                                TypedValue.COMPLEX_UNIT_DIP, 50f,  
                                getResources().getDisplayMetrics()));// 默认为10DP  
                break;  
            }  
        }  
        a.recycle();
        
        
    }  
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if (!once){
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			menu = (ViewGroup) wrapper.getChildAt(0);
			mainContent = (ViewGroup) wrapper.getChildAt(1);
			//dp to px
			menuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
					, menuRightPadding, mainContent.getResources().getDisplayMetrics());
			menuWidth = screenWidth - menuRightPadding;
			halfMenuWidth = menuWidth / 2;
			menu.getLayoutParams().width = menuWidth;
			mainContent.getLayoutParams().width = screenWidth;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		
		if (changed){
			this.scrollTo(menuWidth, 0);	//隐藏菜单
			once = true;
		}
		
	}

	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
			
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();
			if (scrollX > halfMenuWidth){
				isOpen = false;
				this.smoothScrollTo(menuWidth, 0);
			}
			else{
				isOpen = true;
				this.smoothScrollTo(0, 0);
			}
			return true;
			
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	public void openMenu(){
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	public void closeMenu(){
		if (isOpen){
			this.smoothScrollTo(menuWidth,0);
			isOpen = false;
		}
	}

	public void toggleMenu(){
		if (isOpen)
			closeMenu();
		else
			openMenu();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / menuWidth;
		float leftScale = 1 - 0.3f * scale;
		float rightScale = 0.8f + scale * 0.2f;
		
		ViewHelper.setScaleX(menu, leftScale);
		ViewHelper.setScaleY(menu, leftScale);
		ViewHelper.setAlpha(menu, 0.6f + 0.4f * (1 - scale));
		ViewHelper.setTranslationX(menu, menuWidth * scale * 0.7f);

		ViewHelper.setPivotX(mainContent, 0);
		ViewHelper.setPivotY(mainContent, mainContent.getHeight() / 2);
		ViewHelper.setScaleX(mainContent, rightScale);
		ViewHelper.setScaleY(mainContent, rightScale);
	}
	
	
}
