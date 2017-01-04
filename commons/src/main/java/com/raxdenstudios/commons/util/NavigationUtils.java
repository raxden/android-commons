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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.Map;

/**
 *
 */
public class NavigationUtils {

    private static final String TAG = NavigationUtils.class.getSimpleName();

    private static int[] animations = new int[] {
            android.R.animator.fade_in,
            android.R.animator.fade_out
    };

    public static void navigateToActivity(Context context, Class<?> classToStartIntent) {
        performNavigationToActivity(context, classToStartIntent, null, 0);
    }

    public static void navigateToActivity(Context context, Class<?> classToStartIntent, int[] transitions) {
        performNavigationToActivity(context, classToStartIntent, null, 0, transitions);
    }

    public static void navigateToActivity(Context context, Class<?> classToStartIntent, Bundle extras) {
        performNavigationToActivity(context, classToStartIntent, extras, 0);
    }

    public static void navigateToActivity(Context context, Class<?> classToStartIntent, Bundle extras, int[] transitions) {
        performNavigationToActivity(context, classToStartIntent, extras, 0, transitions);
    }

    public static void navigateToActivity(Context context, Class<?> classToStartIntent, Parcelable parcelable) {
        performNavigationToActivity(context, classToStartIntent, createExtras(parcelable), 0);
    }

    public static void navigateToActivity(Context context, Class<?> classToStartIntent, Parcelable parcelable, int[] transitions) {
        performNavigationToActivity(context, classToStartIntent, createExtras(parcelable), 0, transitions);
    }

    public static void navigateToActivity(Context context, Class<?> classToStartIntent, Map<String, Parcelable> parcelableMap) {
        performNavigationToActivity(context, classToStartIntent, createExtras(parcelableMap), 0);
    }

    public static void navigateToActivity(Context context, Class<?> classToStartIntent, Map<String, Parcelable> parcelableMap, int[] transitions) {
        performNavigationToActivity(context, classToStartIntent, createExtras(parcelableMap), 0, transitions);
    }

    public static void navigateToActivity(Context context, Intent intent) {
        performNavigationToActivity(context, intent, 0);
    }

    public static void navigateToActivity(Context context, Intent intent, int[] transitions) {
        performNavigationToActivity(context, intent, 0, transitions);
    }

    public static void navigateToActivityForResult(Context context, Class<?> classToStartIntent, int requestCode) {
        performNavigationToActivity(context, classToStartIntent, null, requestCode);
    }

    public static void navigateToActivityForResult(Context context, Class<?> classToStartIntent, int requestCode, int[] transitions) {
        performNavigationToActivity(context, classToStartIntent, null, requestCode, transitions);
    }

    public static void navigateToActivityForResult(Context context, Class<?> classToStartIntent, Bundle extras, int requestCode) {
        performNavigationToActivity(context, createIntent(context, classToStartIntent, extras), requestCode);
    }

    public static void navigateToActivityForResult(Context context, Class<?> classToStartIntent, Bundle extras, int requestCode, int[] transitions) {
        performNavigationToActivity(context, createIntent(context, classToStartIntent, extras), requestCode, transitions);
    }

    public static void navigateToActivityForResult(Context context, Class<?> classToStartIntent, Parcelable parcelable, int requestCode) {
        performNavigationToActivity(context, classToStartIntent, createExtras(parcelable), requestCode);
    }

    public static void navigateToActivityForResult(Context context, Class<?> classToStartIntent, Parcelable parcelable, int requestCode, int[] transitions) {
        performNavigationToActivity(context, classToStartIntent, createExtras(parcelable), requestCode, transitions);
    }

    public static void navigateToActivityForResult(Context context, Class<?> classToStartIntent, Map<String, Parcelable> parcelableMap, int requestCode) {
        performNavigationToActivity(context, classToStartIntent, createExtras(parcelableMap), requestCode);
    }

    public static void navigateToActivityForResult(Context context, Class<?> classToStartIntent, Map<String, Parcelable> parcelableMap, int requestCode, int[] transitions) {
        performNavigationToActivity(context, classToStartIntent, createExtras(parcelableMap), requestCode, transitions);
    }

    public static void navigateToActivityForResult(Context context, Intent intent, int requestCode) {
        performNavigationToActivity(context, intent, requestCode);
    }

    public static void navigateToActivityForResult(Context context, Intent intent, int requestCode, int[] transitions) {
        performNavigationToActivity(context, intent, requestCode, transitions);
    }

    // Support methods

    private static Bundle createExtras(Map<String, Parcelable> map) {
        Bundle extras = new Bundle();
        for (Map.Entry<String, Parcelable> entry : map.entrySet()) {
            extras.putParcelable(entry.getKey(), entry.getValue());
        }
        return extras;
    }

    private static Bundle createExtras(Parcelable parcelable) {
        Bundle extras = new Bundle();
        extras.putParcelable(parcelable.getClass().getSimpleName(), parcelable);
        return extras;
    }

    public static Intent createIntent(Context context, Class classToStartIntent, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(context, classToStartIntent);
        if (extras != null) {
            intent.putExtras(extras);
        }
        return intent;
    }

    private static void performNavigationToActivity(Context context, Class<?> classToStartIntent, Bundle extras, int requestCode) {
        performNavigationToActivity(context, createIntent(context, classToStartIntent, extras), requestCode);
    }

    private static void performNavigationToActivity(Context context, Class<?> classToStartIntent, Bundle extras, int requestCode, int[] transitions) {
        performNavigationToActivity(context, createIntent(context, classToStartIntent, extras), requestCode, transitions);
    }

    private static void performNavigationToActivity(Context context, Intent intent, int requestCode) {
        performNavigationToActivity(context, intent, requestCode, new int[] {animations[0], animations[1]});
    }

    private static void performNavigationToActivity(Context context, Intent intent, int requestCode, int[] transitions) {
        if (context instanceof Activity) {
            Activity activity = ((Activity) context);
            if (requestCode > 0) {
                activity.startActivityForResult(intent, requestCode);
            } else {
                activity.startActivity(intent);
            }
            if (transitions.length > 1) {
                activity.overridePendingTransition(transitions[0], transitions[1]);
            }
        } else {
            context.startActivity(intent);
        }
    }

}
