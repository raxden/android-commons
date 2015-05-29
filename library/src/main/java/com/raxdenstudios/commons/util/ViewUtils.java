package com.raxdenstudios.commons.util;

import android.view.View;
import android.view.ViewGroup;

/**
 *
 * @author Angel Gomez
 */
public class ViewUtils {

	public static final void changeViewVisibility(View view, int visibility) {
		if(view instanceof ViewGroup){
			ViewGroup rl = ((ViewGroup)view);
			for(int count=0;count<rl.getChildCount();count++) {
				changeViewVisibility(rl.getChildAt(count), visibility);
			}
			rl.setVisibility(visibility);
		}else if(view instanceof View) {
			view.setVisibility(visibility);
		}
	}
	
	public static final String dump(View view) {
    	if (view == null) return "";    	
    	return "["+view.getLeft()+","+view.getTop()+", w="+view.getWidth()+", h="+view.getHeight()+"] mw="+view.getMeasuredWidth()+", mh="+view.getMeasuredHeight()+", scroll["+view.getScrollX()+","+view.getScrollY()+"]";
	}
	
	public static final void hideView(View view){
		changeViewVisibility(view, View.GONE);
	}
	
	public static final void showView(View view){
		changeViewVisibility(view, View.VISIBLE);
	}
	
}
