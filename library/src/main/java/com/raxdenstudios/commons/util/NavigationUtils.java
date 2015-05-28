package com.raxdenstudios.commons.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;

import com.raxdenstudios.commons.R;

import java.io.Serializable;

public class NavigationUtils {

    private static final String TAG = NavigationUtils.class.getSimpleName();

    private static NavigationUtils INSTANCE = null;
	
	private NavigationUtils() {}
	
	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new NavigationUtils();
		}
	}
	
	public static NavigationUtils getInstance() {
		if (INSTANCE == null) createInstance();
		return INSTANCE;
	}

    public void loadActivity(Context context, Class<?> clss) {
        loadActivity(context, clss, (Object)null, null, 0);
    }

    public void loadActivity(Context context, Class<?> clss, Bundle value) {
        loadActivity(context, clss, value, 0);
    }

    public void loadActivity(Context context, Class<?> clss, Bundle value, int requestCode) {
        loadActivity(context, clss, (Object)value, null, requestCode);
    }

    public void loadActivity(Context context, Class<?> clss, Parcelable value) {
        loadActivity(context, clss, value, value != null ? value.getClass().getSimpleName() : null, 0);
    }

    public void loadActivity(Context context, Class<?> clss, Parcelable value, String parcelableName) {
        loadActivity(context, clss, value, parcelableName, 0);
    }

	public void loadActivity(Context context, Class<?> clss, Parcelable value, String parcelableName, int requestCode) {
        loadActivity(context, clss, (Object)value, parcelableName, requestCode);
	}

    public void loadActivity(Context context, Class<?> clss, Serializable value) {
        loadActivity(context, clss, value, value != null ? value.getClass().getSimpleName() : null, 0);
    }

    public void loadActivity(Context context, Class<?> clss, Serializable value, String serializableName) {
        loadActivity(context, clss, value, serializableName, 0);
    }

    public void loadActivity(Context context, Class<?> clss, Serializable value, String serializableName, int requestCode) {
        loadActivity(context, clss, (Object)value, serializableName, requestCode);
    }

    private void loadActivity(Context context, Class<?> clss, Object value, String name, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, clss);
        if (value != null && value instanceof Bundle) {
            intent.putExtras((Bundle)value);
        } else if (value != null && value instanceof Parcelable) {
            intent.putExtra(name, (Parcelable)value);
        } else if (value != null && value instanceof Serializable) {
            intent.putExtra(name, (Serializable)value);
        }
        if (context instanceof FragmentActivity) {
            ((FragmentActivity)context).overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            if (requestCode > 0) {
                ((FragmentActivity)context).startActivityForResult(intent, requestCode);
            } else {
                context.startActivity(intent);
            }
        } else {
            context.startActivity(intent);
        }
    }
}
