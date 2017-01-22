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

import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

/**

 */
public class ViewUtils {

    /**
     * Set the background of a view
     * @param view
     * @param drawable
     */
    public static void setBackground(View view, Drawable drawable) {
        ViewCompat.setBackground(view, drawable);
    }

    public static void setVisibility(View view, int visibility) {
        if (view instanceof ViewGroup) {
            ViewGroup rl = ((ViewGroup) view);
            for (int count = 0; count < rl.getChildCount(); count++) {
                setVisibility(rl.getChildAt(count), visibility);
            }
            rl.setVisibility(visibility);
        } else {
            view.setVisibility(visibility);
        }
    }

    public static String dump(View view) {
        if (view == null) {
            return "";
        }
        return "[" + view.getLeft() + "," + view.getTop() + ", w=" + view.getWidth() + ", h="
                + view.getHeight() + "] mw=" + view.getMeasuredWidth() + ", mh="
                + view.getMeasuredHeight() + ", scroll[" + view.getScrollX() + ","
                + view.getScrollY() + "]";
    }

}
