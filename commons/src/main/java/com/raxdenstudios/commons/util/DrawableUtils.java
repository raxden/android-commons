/*
 * Copyright 2014 Ángel Gómez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.raxdenstudios.commons.util;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 *
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
