package com.gzfgeh.service;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class SwipedItemDelete extends ListView {
	private int slop;				//滑动最小距离
	private int minFlingVelocity;	//滑动最小速度
	private int maxFlingVelocity;	//滑动最大速度
	private float downX;			//
	private float downY;			//
	private int downItemPosition;	//按下位置
	private View downView;			//对应的View
	private int itemWidth;			//Item 宽度
	private VelocityTracker velocityTracker;
	private boolean slopFlag;		//是否正在滑动
	private long animationTime = 150;
	private OnDismissCallBack onDismissCallBack; 
	
	public void setAnimationTime(long animationTime){
		this.animationTime = animationTime;
	}
	
	public void setOnDismissCallBack(OnDismissCallBack onDismissCallBack){
		this.onDismissCallBack = onDismissCallBack;
	}
	
	public SwipedItemDelete(Context context){
		this(context,null);
	}
	public SwipedItemDelete(Context context, AttributeSet attrs){
		this(context,attrs,0);
	}
	public SwipedItemDelete(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		ViewConfiguration vc = ViewConfiguration.get(context);
		slop = vc.getScaledTouchSlop();
		minFlingVelocity = vc.getScaledMinimumFlingVelocity();
		maxFlingVelocity = vc.getScaledMaximumFlingVelocity();
	}

	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			return handlerActionDown(ev);
					
		case MotionEvent.ACTION_MOVE:
			return handlerActionMove(ev);
			
		case MotionEvent.ACTION_UP:
			handlerActionUp(ev);
			break;
			
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	private boolean handlerActionDown(MotionEvent ev) {
		// TODO Auto-generated method stub
		downX = ev.getX();
		downY = ev.getY();
		downItemPosition = pointToPosition((int)downX,(int)downY);
		
		if (AdapterView.INVALID_POSITION == downItemPosition)
			return super.onTouchEvent(ev);
		
		downView = getChildAt(downItemPosition - getFirstVisiblePosition());
		if (null != downView)
			itemWidth = downView.getWidth();
		
		if (null == velocityTracker){
			velocityTracker = VelocityTracker.obtain();
			velocityTracker.addMovement(ev);
		}
		
		return super.onTouchEvent(ev);
	}

	@SuppressLint("Recycle") private boolean handlerActionMove(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (velocityTracker == null || downView == null)
			return super.onTouchEvent(ev);
		
		float distanceX = ev.getX() - downX;
		float distanceY = ev.getY() - downY;
		
		//是否可以滑动
		//if (Math.abs(distanceX) > slop && Math.abs(distanceY) < slop){
		if (distanceX < 0 && Math.abs(distanceY) < slop){
			slopFlag = true;
			MotionEvent cancleEvent = MotionEvent.obtain(ev);
			cancleEvent.setAction(MotionEvent.ACTION_CANCEL |
					(ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
			
			onTouchEvent(cancleEvent);
		}
		
		if (slopFlag){
			//移动 和 渐变
			ViewHelper.setTranslationX(downView, distanceX);
			ViewHelper.setAlpha(downView, Math.max(0f, 1f - 2f * Math.abs(distanceX) / itemWidth));
			return true;
		}
		return super.onTouchEvent(ev);
	}
	private void handlerActionUp(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (velocityTracker == null || downView == null || !slopFlag)
			return;
		
		float distanceX = ev.getX() - downX;
		//float distanceX = downX - ev.getX();
		
		velocityTracker.computeCurrentVelocity(1000);
		float velocX = Math.abs(velocityTracker.getXVelocity());
		float velocY = Math.abs(velocityTracker.getYVelocity());
		
		boolean isDismiss = false;
		boolean isDelete = false;
		
		if (Math.abs(distanceX) > itemWidth / 2){
			isDismiss = true;
			isDelete = distanceX > 0;
		}else if( minFlingVelocity <= velocX && velocX <= maxFlingVelocity && velocY < velocX){
			isDismiss = true;
			isDelete = velocityTracker.getXVelocity() > 0;
		}
		
		if (isDismiss){
			ViewPropertyAnimator.animate(downView).translationX(
					isDelete ? itemWidth : -itemWidth).alpha(0)
					.setDuration(animationTime)
					.setListener(new AnimatorListenerAdapter() {

						@Override
						public void onAnimationEnd(Animator animation) {
							// TODO Auto-generated method stub
							performDismiss(downView, downItemPosition);
						}
						
					});
		}else{
			ViewPropertyAnimator.animate(downView)
			.translationX(0)
			.alpha(1)
			.setDuration(animationTime)
			.setListener(null);
		}
		
		if (velocityTracker != null){
			velocityTracker.recycle();
			velocityTracker = null;
		}
		
		slopFlag = false;
	}
	protected void performDismiss(final View downView, final int downItemPosition) {
		// TODO Auto-generated method stub
		final ViewGroup.LayoutParams lp = downView.getLayoutParams();	//获取item的布局参数
		final int originalHeight = downView.getHeight();
		
		ValueAnimator animator = ValueAnimator.ofInt(originalHeight,0)
				.setDuration(animationTime);
		animator.start();
		
		animator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				if (onDismissCallBack != null)
					onDismissCallBack.onDismiss(downItemPosition);
				
				//这段代码很重要，因为我们并没有将item从ListView中移除，而是将item的高度设置为0  
                //所以我们在动画执行完毕之后将item设置回来 
				ViewHelper.setAlpha(downView, 1f);
				ViewHelper.setTranslationX(downView, 0);
				ViewGroup.LayoutParams lp = downView.getLayoutParams();
				lp.height = originalHeight;
				downView.setLayoutParams(lp);
			}
			
		});
		
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				// TODO Auto-generated method stub
				lp.height = (Integer) valueAnimator.getAnimatedValue();
				downView.setLayoutParams(lp);
			}
		});
		
	}
	
	public interface OnDismissCallBack{
		public void onDismiss(int dismissPotision);
	}
}
