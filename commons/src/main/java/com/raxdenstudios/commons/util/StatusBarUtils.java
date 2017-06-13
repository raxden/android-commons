package com.raxdenstudios.commons.util;

import android.app.Activity;
import android.view.View;

/**
 * Created by Angel on 15/05/2017.
 */

public class StatusBarUtils {

    public static void setLightStatusBar(Activity activity) {
        setLightStatusBar(activity, 0);
    }

    public static void setLightStatusBar(Activity activity, int color) {
        if (SDKUtils.hasMarshmallow()) {
            View decorView = activity.getWindow().getDecorView();
            int flags = decorView.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(flags);
        }
        setStatusBarColor(activity, color);
    }

    public static void clearLightStatusBar(Activity activity) {
        clearLightStatusBar(activity, 0);
    }

    public static void clearLightStatusBar(Activity activity, int color) {
        if (SDKUtils.hasMarshmallow()) {
            View decorView = activity.getWindow().getDecorView();
            int flags = decorView.getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(flags);
        }
        setStatusBarColor(activity, color);
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (SDKUtils.hasLollipop() && color != 0) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

}
