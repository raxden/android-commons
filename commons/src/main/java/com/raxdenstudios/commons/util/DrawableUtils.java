package com.raxdenstudios.commons.util;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 *
 * @author Angel Gomez
 */
public class DrawableUtils {

    public static int size(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable)drawable).getBitmap();
        } else {
            bitmap = BitmapUtils.fromDrawable(drawable);
        }
        if (bitmap != null) {
            return BitmapUtils.size(bitmap);
        }
        return -1;
    }

    public static Drawable tinted(Drawable drawable, int color) {
        PorterDuffColorFilter pdcf = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
        if (pdcf != null) {
            drawable.setColorFilter(pdcf);
        }
        return drawable;
    }

}
