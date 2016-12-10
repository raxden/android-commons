package com.raxdenstudios.commons.util;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;


/**
 *
 * @author Angel Gomez
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

    public static Fragment getFragmentByTag(FragmentManager fm, String tag) {
        Fragment fragment = null;
        if (fm != null) {
            fragment = fm.findFragmentByTag(tag);
        }
        return fragment;
    }

    public static Fragment getFragmentFromContainerId(FragmentManager fm, int containerId) {
        Fragment fragment = null;
        if (fm != null) {
            fragment = fm.findFragmentById(containerId);
        }
        return fragment;
    }

    /* =========================  Add / Replace Fragment methods ========================= */

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
            addToBackStack(ft, fragment);
            return performTransaction(ft);
        }
        return 0;
    }

    /* ========================= Remove Fragment methods ========================= */

    public static int removeFragment(FragmentManager fm, String tag) {
        if (fm != null) {
            return removeFragment(fm, getFragmentByTag(fm, tag));
        }
        return 0;
    }

    public static int removeFragment(FragmentManager fm, int containerId) {
        if (fm != null) {
            return removeFragment(fm, getFragmentFromContainerId(fm, containerId));
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

    private static int getBackStackSize(FragmentManager fm) {
        if (fm != null) {
            return fm.getBackStackEntryCount();
        }
        return 0;
    }

    private static void setCustomAnimations(FragmentTransaction ft, int[] animations) {
        if (ft != null && (animations != null && animations.length == 2 || animations.length == 4)) {
            if (animations.length == 2) {
                ft.setCustomAnimations(animations[0], animations[1]);
            } else if (animations.length == 4) {
                ft.setCustomAnimations(animations[0], animations[1], animations[2], animations[3]);
            }
        }
    }

    private static void addToBackStack(FragmentTransaction ft, Fragment fragment) {
        if (ft != null && fragment != null) {
            ft.addToBackStack(fragment.getClass().getName());
        }
    }

    private static String getDefaultTagFromFragment(Fragment fragment) {
        String tag = null;
        if (fragment != null) {
            tag = fragment.getClass().getName();
        }
        return tag;
    }

    private static FragmentTransaction initFragmentTransaction(FragmentManager fm) {
        if (fm != null) {
            return fm.beginTransaction();
        }
        return null;
    }

    private static int performTransaction(FragmentTransaction ft) {
        try {
            return ft.commit();
        } catch (IllegalStateException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return 0;
    }

}
