package com.raxdenstudios.commons.util;

import android.os.Build;

public class SDKUtils {

    /**
    * Checks if the device has Froyo or higher version.
    * @return <code>true</code> if device is a tablet.
    */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Checks if the device has Gingerbread or higher version.
     * @return <code>true</code> if device is a tablet.
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Checks if the device has Honeycomb or higher version.
     * @return <code>true</code> if device is a tablet.
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Checks if the device has HoneycombMR1 or higher version.
     * @return <code>true</code> if device is a tablet.
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Checks if the device has HoneycombMR2 or higher version.
     * @return <code>true</code> if device is a tablet.
     */
    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    /**
     * Checks if the device has IceCreamSandwich or higher version.
     * @return <code>true</code> if device is a tablet.
     */
    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * Checks if the device has JellyBean or higher version.
     * @return <code>true</code> if device is a tablet.
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Checks if the device has KitKat or higher version.
     * @return <code>true</code> if device is a tablet.
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * Checks if the device has Lolllipop or higher version.
     * @return <code>true</code> if device is a tablet.
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Checks if the device has Marshmallow or higher version.
     * @return <code>true</code> if device is a tablet.
     */
    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Checks if the device has Marshmallow or higher version.
     * @return <code>true</code> if device is a tablet.
     */
    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

}
