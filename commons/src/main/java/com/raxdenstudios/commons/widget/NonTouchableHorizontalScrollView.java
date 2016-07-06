package com.raxdenstudios.commons.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class NonTouchableHorizontalScrollView extends HorizontalScrollView {
	
    public NonTouchableHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return false; // scrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }    
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	return false;
    }
    
}
