package com.raxdenstudios.commons.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 *
 * @author Angel Gomez
 */
public class DrawableUtils {

    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable == null) return null;
        if (drawable instanceof BitmapDrawable) return ((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static int size(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable)drawable).getBitmap();
        } else {
            bitmap = toBitmap(drawable);
        }
        if (bitmap != null) {
            return BitmapUtils.size(bitmap);
        }
        return -1;
    }

    public static Drawable tinted(Drawable drawable, int color) {
        if (drawable != null) drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

}
