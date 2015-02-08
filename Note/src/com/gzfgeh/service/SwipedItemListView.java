package com.gzfgeh.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

public class SwipedItemListView extends ListView {
	    public static int MOD_FORBID = 0;
	    public static int MOD_LEFT = 1;
	    public static int MOD_RIGHT = 2;
	    public static int MOD_BOTH = 3;
	    private int mode = MOD_FORBID;
	    private int leftLength = 0;
	    private int rightLength = 0;
        private int slidePosition;
	    private int downY;
	    private int downX;
	    private View itemView;
	    private Scroller scroller;
	    private int mTouchSlop;
	  	private boolean canMove = false;
	  	private boolean isSlided = false;
	  
	  
	  	public SwipedItemListView(Context context){
			this(context,null);
		}
		public SwipedItemListView(Context context, AttributeSet attrs){
			this(context,attrs,0);
		}
		public SwipedItemListView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			scroller = new Scroller(context);
		    mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
			// TODO Auto-generated constructor stub
		}
		
	    public void initSlideMode(int mode){
	      this.mode = mode;
	    }
	    
	    @SuppressLint("ClickableViewAccessibility") @Override
	    public boolean onTouchEvent(MotionEvent ev) {

	    final int action = ev.getAction();
	    int lastX = (int) ev.getX();

	    switch (action) {
	    case MotionEvent.ACTION_DOWN:
	      System.out.println("touch-->" + "down");
	      
	      /*当前模式不允许滑动，则直接返回，交给ListView自身去处理*/
	      if(this.mode == MOD_FORBID){
	        return super.onTouchEvent(ev);
	      }
	      
	      // 如果处于侧滑完成状态，侧滑回去，并直接返回
	      if (isSlided) {
	        scrollBack();
	        return false;
	      }
	      
	      if (!scroller.isFinished()) {
	        return false;
	      }
	      downX = (int) ev.getX();
	      downY = (int) ev.getY();

	      slidePosition = pointToPosition(downX, downY);

	      // 无效的position, 不做任何处理
	      if (slidePosition == AdapterView.INVALID_POSITION) {
	        return super.onTouchEvent(ev);
	      }

	      // 获取我们点击的item view
	      itemView = getChildAt(slidePosition - getFirstVisiblePosition());
	      
	      /*此处根据设置的滑动模式，自动获取左侧或右侧菜单的长度*/
	      if(this.mode == MOD_BOTH){
	        this.leftLength = -itemView.getPaddingLeft();
	        this.rightLength = -itemView.getPaddingRight();
	      }else if(this.mode == MOD_LEFT){
	        this.leftLength = -itemView.getPaddingLeft();
	      }else if(this.mode == MOD_RIGHT){
	        this.rightLength = -itemView.getPaddingRight();
	      }
	      
	      break;
	    case MotionEvent.ACTION_MOVE:
	      System.out.println("touch-->" + "move");
	      
	      if (!canMove
	          && slidePosition != AdapterView.INVALID_POSITION
	          && (Math.abs(ev.getX() - downX) > mTouchSlop && Math.abs(ev
	              .getY() - downY) < mTouchSlop)) {
	        int offsetX = downX - lastX;
	        if(offsetX > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT)){
	          /*从右向左滑*/
	          canMove = true;
	        }else if(offsetX < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT)){
	          /*从左向右滑*/
	          canMove = true;
	        }else{
	          canMove = false;
	        }
	        /*此段代码是为了避免我们在侧向滑动时同时出发ListView的OnItemClickListener时间*/
	        MotionEvent cancelEvent = MotionEvent.obtain(ev);
	        cancelEvent
	            .setAction(MotionEvent.ACTION_CANCEL
	                | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
	        onTouchEvent(cancelEvent);
	      }
	      if (canMove) {
	        /*设置此属性，可以在侧向滑动时，保持ListView不会上下滚动*/
	        requestDisallowInterceptTouchEvent(true);
	        
	        // 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
	        int deltaX = downX - lastX;
	        if(deltaX < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT)){
	          /*向左滑*/
	          itemView.scrollTo(deltaX, 0);
	        }else if(deltaX > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT)){
	          /*向右滑*/
	          itemView.scrollTo(deltaX, 0);
	        }else{
	          itemView.scrollTo(0, 0);
	        }
	        return true; // 拖动的时候ListView不滚动
	      }
	    case MotionEvent.ACTION_UP:
	      System.out.println("touch-->" + "up");
	      if (canMove){
	        canMove = false;
	        scrollByDistanceX();
	      }
	      break;
	    }

	    // 否则直接交给ListView来处理onTouchEvent事件
	    return super.onTouchEvent(ev);
	  }

	  /**
	   * 根据手指滚动itemView的距离来判断是滚动到开始位置还是向左或者向右滚动
	   */
	  private void scrollByDistanceX() {
	    /*当前模式不允许滑动，则直接返回*/
	    if(this.mode == MOD_FORBID){
	      return;
	    }
	    if(itemView.getScrollX() > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT)){
	      /*从右向左滑*/
	      if (itemView.getScrollX() >= rightLength / 2) {
	        scrollLeft();
	      }  else {
	        // 滚回到原始位置
	        scrollBack();
	      }
	    }else if(itemView.getScrollX() < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT)){
	      /*从左向右滑*/
	      if (itemView.getScrollX() <= -leftLength / 2) {
	        scrollRight();
	      } else {
	        // 滚回到原始位置
	        scrollBack();
	      }
	    }else{
	      // 滚回到原始位置
	      scrollBack();
	    }

	  }

	  /**
	   * 往右滑动，getScrollX()返回的是左边缘的距离，就是以View左边缘为原点到开始滑动的距离，所以向右边滑动为负值
	   */
	  private void scrollRight() {
	    isSlided = true;
	    final int delta = (leftLength + itemView.getScrollX());
	    // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
	    scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,
	        Math.abs(delta));
	    postInvalidate(); // 刷新itemView
	  }

	  /**
	   * 向左滑动，根据上面我们知道向左滑动为正值
	   */
	  private void scrollLeft() {
	    isSlided = true;
	    final int delta = (rightLength - itemView.getScrollX());
	    // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
	    scroller.startScroll(itemView.getScrollX(), 0, delta, 0,
	        Math.abs(delta));
	    postInvalidate(); // 刷新itemView
	  }

	  /**
	   * 滑动会原来的位置
	   */
	  private void scrollBack() {
	    isSlided = false;
	    scroller.startScroll(itemView.getScrollX(), 0, -itemView.getScrollX(),
	        0, Math.abs(itemView.getScrollX()));
	    postInvalidate(); // 刷新itemView
	  }

	  @Override
	  public void computeScroll() {
	    // 调用startScroll的时候scroller.computeScrollOffset()返回true，
	    if (scroller.computeScrollOffset()) {
	      // 让ListView item根据当前的滚动偏移量进行滚动
	      itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());

	      postInvalidate();
	    }
	  }

	  /**
	   * 提供给外部调用，用以将侧滑出来的滑回去
	   */
	  public void slideBack() {
	    this.scrollBack();
	  }
	  
}
