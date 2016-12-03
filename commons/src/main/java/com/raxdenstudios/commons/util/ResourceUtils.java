package com.raxdenstudios.commons.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * @author Angel Gomez
 */
public class ResourceUtils {

    public static int getResourceId(Context context, String name) {
        int resID = getDrawableId(context, name);
        if (resID == 0) {
            resID = getStringId(context, name);
        }
        if (resID == 0) {
            resID = getLayoutId(context, name);
        }
        if (resID == 0) {
            resID = getColorId(context, name);
        }
        return resID;
    }

    public static int getColorId(Context context, String name) {
        int resID = 0;
        if (Utils.hasValue(name)) {
            resID = context.getResources().getIdentifier(name.replaceAll("R.color.", ""), "color", context.getPackageName());
        }
        return resID;
    }

    public static int getStringId(Context context, String name) {
        int resID = 0;
        if (Utils.hasValue(name)) {
            resID = context.getResources().getIdentifier(name.replaceAll("R.string.", ""), "string", context.getPackageName());
        }
        return resID;
    }

    public static int getDrawableId(Context context, String name) {
        int resID = 0;
        if (Utils.hasValue(name)) {
            resID = context.getResources().getIdentifier(name.replaceAll("R.drawable.", ""), "drawable", context.getPackageName());
        }
        return resID;
    }

    public static int getLayoutId(Context context, String name) {
        int resID = 0;
        if (Utils.hasValue(name)) {
            resID = context.getResources().getIdentifier(name.replaceAll("R.layout.", ""), "layout", context.getPackageName());
        }
        return resID;
    }

    public static String getString(Context context, String name) {
        int resId = getStringId(context, name);
        if (resId != 0) {
            return context.getResources().getString(resId);
        } else {
            return name;
        }
    }

    public static Drawable getDrawable(Context context, String name) {
        Drawable drawable = null;

        // Delete extension if exists
        if (name.contains(".")) {
            name = name.substring(0, name.lastIndexOf("."));
        }

        int resId = getDrawableId(context, name);
        if (resId != 0) {
            drawable = context.getResources().getDrawable(resId);
        }

        return drawable;
    }

}
