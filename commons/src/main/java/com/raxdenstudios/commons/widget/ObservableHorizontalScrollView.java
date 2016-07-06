package com.raxdenstudios.commons.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;

public class ObservableHorizontalScrollView extends HorizontalScrollView {

	public interface OnScrollStoppedListener{
	    void onScrollStopped(ObservableHorizontalScrollView view, int x, int y);
	}
	
	private OnScrollStoppedListener onScrollStopped = null;
	
	public interface OnScrollChangedListener{
	    void onScrollChanged(ObservableHorizontalScrollView view, int x, int y, int oldx, int oldy);
	}	
	
	private OnScrollChangedListener onScrollChanged = null;

	public interface OnScrollListener{
		public void onScrollStateChanged(ObservableHorizontalScrollView view, int scrollState);
	}	
	
	private OnScrollListener onScrollListener = null;
	
	private Runnable scrollerTask;
	private int initialPosition;
	private int newCheck = 100;
	
	private Object o = new Object();
	private boolean flying;	
	
	public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
	    super(context, attrs);

	    scrollerTask = new Runnable() {

	        public void run() {

	            int newPosition = getScrollX();
	            if (initialPosition - newPosition == 0) { //has stopped
	                if (onScrollStopped!=null) {
	                	onScrollStopped.onScrollStopped(ObservableHorizontalScrollView.this, getScrollX(), getScrollY());
	                }
	        		synchronized (o) {
		                if (isFlying()) {
							setFlying(false);
							if (onScrollListener != null) {
								onScrollListener.onScrollStateChanged(ObservableHorizontalScrollView.this, AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
							}
						}
	        		}
	            } else {
	                initialPosition = getScrollX();
	                ObservableHorizontalScrollView.this.postDelayed(scrollerTask, newCheck);
	            }
	        }
	    };
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			startScrollerTask();
        }
		return super.onTouchEvent(event);
	}	

	@Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
		synchronized (o) {
			if (!isFlying()) {
				setFlying(true);
				if (onScrollListener != null) {
					onScrollListener.onScrollStateChanged(this, AbsListView.OnScrollListener.SCROLL_STATE_FLING);
				}
			}        
        }
        if (onScrollChanged != null) {
        	onScrollChanged.onScrollChanged(this, x, y, oldx, oldy);
        }
	}

	public void setOnScrollStoppedListener(OnScrollStoppedListener onScrollStopped){
		this.onScrollStopped = onScrollStopped;
	}
	
	public void setOnScrollChanged(OnScrollChangedListener onScrollChanged) {
		this.onScrollChanged = onScrollChanged;
	}
	
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	public void startScrollerTask(){
	    initialPosition = getScrollX();
	    postDelayed(scrollerTask, newCheck);
	}	

	public boolean isFlying() {
		synchronized (o) {
			return flying;	
		}
	}

	public void setFlying(boolean flying) {
		synchronized (o) {
			this.flying = flying;	
		}
	}
	
}
