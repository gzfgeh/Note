package com.gzfgeh.service;

import com.gzfgeh.note.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewHelper;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class PullLayout extends ScrollView {
	private ImageView pullTop;
	private LinearLayout fillContentText;
	private LinearLayout fillContentVoice;
	private LinearLayout fillContentPhoto;
	private LinearLayout fillContentMoive;
	private TextView tv;
	
	private int range;		//隐藏的高度
    private int tvHeight;
    private int tvWidth;
	private boolean isTouchRunning;
	private boolean isCancleTouch;
	private ObjectAnimator objectAnimator;
	private float downY;
	private float newY;
	private float mDownY;
	private float mUpY;
	private int scanHeigth, scanWidth;
	private static OnPullLayoutListener onPullLayoutListener;
	
	public PullLayout(Context context){
		this(context,null);
	}
	
	public PullLayout(Context context,AttributeSet attrs){
		this(context,attrs,0);
	}
	
	@SuppressWarnings("deprecation")
	public PullLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		scanHeigth = wm.getDefaultDisplay().getHeight();
		scanWidth = wm.getDefaultDisplay().getWidth();
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		setVerticalScrollBarEnabled(false);
		fillContentText = (LinearLayout) findViewById(R.id.text);
		fillContentVoice = (LinearLayout) findViewById(R.id.sounds);
		fillContentPhoto = (LinearLayout) findViewById(R.id.pic);
		fillContentMoive = (LinearLayout) findViewById(R.id.movie);
		
		tv = (TextView) findViewById(R.id.tv);
		
		pullTop = (ImageView) findViewById(R.id.pull_top);
		pullTop.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				pullTop.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				range = pullTop.getHeight();
				scrollTo(0, range);
				pullTop.getLayoutParams().height = range;
			}
		});
		
		tv.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                tv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                tvHeight = tv.getHeight();
                tvWidth = tv.getWidth();
                ViewHelper.setTranslationY(fillContentText, tvHeight);
                ViewHelper.setTranslationY(fillContentVoice, tvHeight);
                ViewHelper.setTranslationY(fillContentPhoto, tvHeight);
                ViewHelper.setTranslationY(fillContentMoive, tvHeight);
            }
        });
		
		fillContentText.setLayoutParams(new LayoutParams(scanWidth, scanHeigth-60-250-50));
		fillContentVoice.setLayoutParams(new LayoutParams(scanWidth, scanHeigth-60-250-50));
		fillContentPhoto.setLayoutParams(new LayoutParams(scanWidth, scanHeigth-60-250-50));
		fillContentMoive.setLayoutParams(new LayoutParams(scanWidth, scanHeigth-60-250-50));
	}
	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isCancleTouch = false;
			isTouchRunning = true;
			downY = ev.getY();
			mDownY = downY;
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (objectAnimator != null && objectAnimator.isRunning()) {
            ev.setAction(MotionEvent.ACTION_UP);
            isCancleTouch = true;
        }
        if (isCancleTouch && ev.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        if (ev.getActionIndex() != 0 && getScrollY() < range) {
            ev.setAction(MotionEvent.ACTION_UP);
            isCancleTouch = true;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                isTouchRunning = true;
                if (getScrollY() != 0) {
                    newY = 0;
                    downY = ev.getY();
                } else {
                	newY = ev.getY() - downY;
                    if (newY > 0) {
                        setT((int) -newY / 5);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            	mUpY = ev.getY();
                isTouchRunning = false;
                if (Math.abs(mUpY - mDownY) > 100)
	                if (getScrollY() < range) {
	                    if (newY != 0) {
	                        reset();
	                        if ((null != onPullLayoutListener) && (status == Status.Open))
	                        	onPullLayoutListener.setOnPullLayoutListener();
	                    } else {
	                    	if ((mUpY - mDownY < range/4) && (status == Status.Close))
	                    		reset();
	                    	else
	                    		toggle();
	                    }
	                    return true;
	                }
                break;
        }
        return super.onTouchEvent(ev);
	}

	
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
        if (t > range) {
            return;
        } else if (!isTouchRunning && t != range) {
            scrollTo(0, range);
        } else {
            animateScroll(t);
        }
	}

	private Integer evaluate(float fraction, Object startValue, Integer endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

	public void setT(int t) {
        scrollTo(0, t);
        if (t < 0) {
            animatePull(t);
        }
    }

	private void animateScroll(int t) {
        float percent = (float) t / range;
        ViewHelper.setTranslationY(pullTop, t);
        ViewHelper.setTranslationY(fillContentText, tvHeight * percent);
        ViewHelper.setTranslationY(fillContentVoice, tvHeight * percent);
        ViewHelper.setTranslationY(fillContentPhoto, tvHeight * percent);
        ViewHelper.setTranslationY(fillContentMoive, tvHeight * percent);
        ViewHelper.setScaleX(tv, 2 - percent);
        ViewHelper.setScaleY(tv, 2 - percent);
        //ViewHelper.setTranslationX(tv, tvWidth * (1 - percent) / 2f);
        ViewHelper.setTranslationY(tv, t + tvHeight * (1 - percent) / 2f);
        tv.setTextColor(evaluate(percent, Color.WHITE, Color.BLUE));
    }
	
	private void animatePull(int t) {
        pullTop.getLayoutParams().height = range - t;
        pullTop.requestLayout();
        float percent = (float) t / range;
        ViewHelper.setScaleX(tv, 2 - percent);
        ViewHelper.setScaleY(tv, 2 - percent);
        //ViewHelper.setTranslationX(tv, tvWidth * (1 - percent) / 2f);
    }

	private void toggle() {
		// TODO Auto-generated method stub
		if (isOpen()) {
            close();
        } else {
            open();
        }
	}
	
	private Status status = Status.Close;

    public enum Status {
        Open, Close;
    }

    public boolean isOpen() {
        return status == Status.Open;
    }

    private void reset() {
        if (objectAnimator != null && objectAnimator.isRunning()) {
            return;
        }
        objectAnimator = ObjectAnimator.ofInt(this, "t", (int) -newY / 5, 0);
        objectAnimator.setDuration(150);
        objectAnimator.start();
    }

    public void close() {
        if (objectAnimator != null && objectAnimator.isRunning()) {
            return;
        }
        objectAnimator = ObjectAnimator.ofInt(this, "t", getScrollY(), range);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator arg0) {
                isTouchRunning = true;
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                isTouchRunning = false;
                status = Status.Close;
            }

            @Override
            public void onAnimationCancel(Animator arg0) {

            }
        });
        objectAnimator.setDuration(250);
        objectAnimator.start();
        tv.setText("下拉添加");
    }

    public void open() {
        if (objectAnimator != null && objectAnimator.isRunning()) {
            return;
        }
        objectAnimator = ObjectAnimator.ofInt(this, "t", getScrollY(), (int) (-getScrollY() / 2.2f), 0);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator arg0) {
                isTouchRunning = true;
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                isTouchRunning = false;
                status = Status.Open;
            }

            @Override
            public void onAnimationCancel(Animator arg0) {

            }
        });
        objectAnimator.setDuration(400);
        objectAnimator.start();
        tv.setText("继续下拉");
    }
	
    public void setOnPullLayoutListener(OnPullLayoutListener onPullLayoutListener){
    	this.onPullLayoutListener = onPullLayoutListener;
    }
    
    public interface OnPullLayoutListener {
    	public void setOnPullLayoutListener();
    }
}
