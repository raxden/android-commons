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

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

/**
 *
 */
public class ResourceUtils {

    public static Uri getResourceUri(Context context, int resourceId) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resourceId) + '/' +
                context.getResources().getResourceTypeName(resourceId) + '/' +
                context.getResources().getResourceEntryName(resourceId));
    }

    public static Uri getDrawableUri(Context context, String name) {
        Uri uri = null;
        int resId = getDrawableId(context, name);
        if (resId != 0) {
            uri = getResourceUri(context, resId);
        }
        return uri;
    }

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
