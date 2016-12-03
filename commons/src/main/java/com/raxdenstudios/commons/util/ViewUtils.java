package com.raxdenstudios.commons.util;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * @author Angel Gomez
 */
public class ViewUtils {

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void setBackground(View view, Drawable drawable) {
		if (SDKUtils.hasJellyBean()) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
	}

	public static void setVisibility(View view, int visibility) {
		if(view instanceof ViewGroup){
			ViewGroup rl = ((ViewGroup)view);
			for(int count=0;count<rl.getChildCount();count++) {
				setVisibility(rl.getChildAt(count), visibility);
			}
			rl.setVisibility(visibility);
		}else if(view instanceof View) {
			view.setVisibility(visibility);
		}
	}
	
	public static String dump(View view) {
    	if (view == null) return "";    	
    	return "["+view.getLeft()+","+view.getTop()+", w="+view.getWidth()+", h="+view.getHeight()+"] mw="+view.getMeasuredWidth()+", mh="+view.getMeasuredHeight()+", scroll["+view.getScrollX()+","+view.getScrollY()+"]";
	}

}
