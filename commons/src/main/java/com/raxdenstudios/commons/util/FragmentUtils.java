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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;


/**
 *
 */
public class FragmentUtils {

    private static final String TAG = FragmentUtils.class.getSimpleName();

    private static int[] animations = new int[] {
            android.R.animator.fade_in,
            android.R.animator.fade_out,
            android.R.animator.fade_in,
            android.R.animator.fade_out
    };

    /* ========================= Stack methods =========================== */



    /* ========================= Get Fragment methods =========================== */

    public static Fragment getFragment(Activity activity, String tag) {
        return getFragment(activity.getFragmentManager(), tag);
    }

    public static Fragment getFragment(Activity activity, int containerId) {
        return getFragment(activity.getFragmentManager(), containerId);
    }

    public static Fragment getFragment(FragmentManager fm, String tag) {
        Fragment fragment = null;
        if (fm != null) {
            fragment = fm.findFragmentByTag(tag);
        }
        return fragment;
    }

    public static Fragment getFragment(FragmentManager fm, int containerId) {
        Fragment fragment = null;
        if (fm != null) {
            fragment = fm.findFragmentById(containerId);
        }
        return fragment;
    }

    /* =========================  Add / Replace Fragment methods ========================= */

    public static int loadFragment(Activity activity, int containerId, Fragment fragment) {
        return loadFragment(activity.getFragmentManager(), containerId, fragment, animations);
    }

    public static int loadFragment(Activity activity, int containerId, Fragment fragment, boolean addToBackStack) {
        return loadFragment(activity.getFragmentManager(), containerId, fragment, animations, addToBackStack);
    }

    public static int loadFragment(Activity activity, int containerId, Fragment fragment, int[] animations) {
        return loadFragment(activity.getFragmentManager(), containerId, fragment, animations, false);
    }

    public static int loadFragment(Activity activity, int containerId, Fragment fragment, int[] animations, boolean addToBackStack) {
        return loadFragment(activity.getFragmentManager(), containerId, fragment, getDefaultTagFromFragment(fragment), animations, addToBackStack);
    }

    public static int loadFragment(Activity activity, int containerId, Fragment fragment, String tag) {
        return loadFragment(activity.getFragmentManager(), containerId, fragment, tag, animations);
    }

    public static int loadFragment(Activity activity, int containerId, Fragment fragment, String tag, boolean addToBackStack) {
        return loadFragment(activity.getFragmentManager(), containerId, fragment, tag, animations, addToBackStack);
    }

    public static int loadFragment(Activity activity, int containerId, Fragment fragment, String tag, int[] animations) {
        return loadFragment(activity.getFragmentManager(), containerId, fragment, tag, animations, false);
    }

    public static int loadFragment(Activity activity, int containerId, Fragment fragment, String tag, int[] animations, boolean addToBackStack) {
        return loadFragment(activity.getFragmentManager(), containerId, fragment, tag, animations, addToBackStack);
    }

    public static int loadFragment(FragmentManager fm, int containerId, Fragment fragment) {
        return loadFragment(fm, containerId, fragment, animations);
    }

    public static int loadFragment(FragmentManager fm, int containerId, Fragment fragment, boolean addToBackStack) {
        return loadFragment(fm, containerId, fragment, animations, addToBackStack);
    }

    public static int loadFragment(FragmentManager fm, int containerId, Fragment fragment, int[] animations) {
        return loadFragment(fm, containerId, fragment, animations, false);
    }

    public static int loadFragment(FragmentManager fm, int containerId, Fragment fragment, int[] animations, boolean addToBackStack) {
        if (fm != null && containerId > 0 && fragment != null) {
            return loadFragment(fm, containerId, fragment, getDefaultTagFromFragment(fragment), animations, addToBackStack);
        }
        return 0;
    }

    public static int loadFragment(FragmentManager fm, int containerId, Fragment fragment, String tag) {
        return loadFragment(fm, containerId, fragment, tag, animations);
    }

    public static int loadFragment(FragmentManager fm, int containerId, Fragment fragment, String tag, boolean addToBackStack) {
        return loadFragment(fm, containerId, fragment, tag, animations, addToBackStack);
    }

    public static int loadFragment(FragmentManager fm, int containerId, Fragment fragment, String tag, int[] animations) {
        return loadFragment(fm, containerId, fragment, tag, animations, false);
    }

    public static int loadFragment(FragmentManager fm, int containerId, Fragment fragment, String tag, int[] animations, boolean addToBackStack) {
        if (fm != null && containerId > 0 && fragment != null) {
            return loadFragment(initFragmentTransaction(fm), containerId, fragment, tag, animations, addToBackStack);
        }
        return 0;
    }

    public static int loadFragment(FragmentTransaction ft, int containerId, Fragment fragment, String tag) {
        return loadFragment(ft, containerId, fragment, tag, animations);
    }

    public static int loadFragment(FragmentTransaction ft, int containerId, Fragment fragment, String tag, boolean addToBackStack) {
        return loadFragment(ft, containerId, fragment, tag, animations, addToBackStack);
    }

    public static int loadFragment(FragmentTransaction ft, int containerId, Fragment fragment, String tag, int[] animations) {
        return loadFragment(ft, containerId, fragment, tag, animations, false);
    }

    public static int loadFragment(FragmentTransaction ft, int containerId, Fragment fragment, String tag, int[] animations, boolean addToBackStack) {
        if (fragment != null && ft != null) {
            if (Utils.hasValue(tag)) {
                ft = ft.replace(containerId, fragment, tag);
            } else {
                ft = ft.replace(containerId, fragment);
            }
            setCustomAnimations(ft, animations);
            if (addToBackStack) {
                addToBackStack(ft, fragment);
            }
            return performTransaction(ft);
        }
        return 0;
    }

    /* ========================= Remove Fragment methods ========================= */

    public static int removeFragment(FragmentManager fm, String tag) {
        if (fm != null) {
            return removeFragment(fm, getFragment(fm, tag));
        }
        return 0;
    }

    public static int removeFragment(FragmentManager fm, int containerId) {
        if (fm != null) {
            return removeFragment(fm, getFragment(fm, containerId));
        }
        return 0;
    }

    public static int removeFragment(FragmentManager fm, Fragment fragment) {
        if (fm != null && fragment != null) {
            return removeFragment(initFragmentTransaction(fm), fragment);
        }
        return 0;
    }

    public static int removeFragment(FragmentTransaction ft, Fragment fragment) {
        if (ft != null && fragment != null) {
            ft = ft.remove(fragment);
            ft.addToBackStack(null);
            return performTransaction(ft);
        }
        return 0;
    }

    /* =========================  Support methods ========================= */

    public static int getBackStackSize(FragmentManager fm) {
        if (fm != null) {
            return fm.getBackStackEntryCount();
        }
        return 0;
    }

    public static void setCustomAnimations(FragmentTransaction ft, int[] animations) {
        if (ft != null && (animations != null && animations.length == 2 || animations.length == 4)) {
            if (animations.length == 2) {
                ft.setCustomAnimations(animations[0], animations[1]);
            } else if (animations.length == 4) {
                ft.setCustomAnimations(animations[0], animations[1], animations[2], animations[3]);
            }
        }
    }

    public static void addToBackStack(FragmentTransaction ft, Fragment fragment) {
        if (ft != null && fragment != null) {
            ft.addToBackStack(fragment.getClass().getName());
        }
    }

    public static String getDefaultTagFromFragment(Fragment fragment) {
        String tag = null;
        if (fragment != null) {
            tag = fragment.getClass().getName();
        }
        return tag;
    }

    public static FragmentTransaction initFragmentTransaction(FragmentManager fm) {
        if (fm != null) {
            return fm.beginTransaction();
        }
        return null;
    }

    public static int performTransaction(FragmentTransaction ft) {
        try {
            return ft.commit();
        } catch (IllegalStateException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return 0;
    }

}
