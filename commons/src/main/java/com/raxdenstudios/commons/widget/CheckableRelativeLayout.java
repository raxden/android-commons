package com.raxdenstudios.commons.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
	
	private boolean isChecked;
	
	private static final int[] CheckedStateSet = {
	    android.R.attr.state_checked,
	};
	
	public CheckableRelativeLayout(Context context) {
		super(context);
	}	
	
	public CheckableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
	    final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
	    if (isChecked()) {
	        mergeDrawableStates(drawableState, CheckedStateSet);
	    }
	    return drawableState;
	}
	
	@Override
	public boolean performClick() {
	    toggle();
	    return super.performClick();
	}	
	
	@Override
	public boolean isChecked() {
		return isChecked;
	}
	
	@Override
	public void setChecked(boolean checked) {
		isChecked = checked;
	
	    refreshDrawableState();
	}
	
	@Override
	public void toggle() {
		setChecked(!isChecked);
	}

}