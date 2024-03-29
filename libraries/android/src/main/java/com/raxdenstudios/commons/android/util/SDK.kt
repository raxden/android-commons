package com.raxdenstudios.commons.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import com.raxdenstudios.commons.android.ext.getPackageInfo

object SDK {

    val isEmulator: Boolean
        get() = (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)

    fun hasVirtualNavigationBar(context: Context): Boolean {
        val id = context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
        return (id > 0 && context.resources.getBoolean(id)) || isEmulator
    }

    @SuppressLint("InternalInsetResource")
    fun getVirtualNavigationBarHeight(context: Context): Int {
        if (!hasVirtualNavigationBar(context)) return 0
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    /**
     * Only works when is called in onCreate
     */
    @SuppressLint("InternalInsetResource")
    fun getStatusBarHeight(context: Context): Int {
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    fun getPackageName(context: Context): String =
        runCatching { context.getPackageInfo().packageName }.getOrDefault("")

    fun getVersionName(context: Context): String =
        runCatching { context.getPackageInfo().versionName }.getOrDefault("")

    fun getVersionCode(context: Context): Long = when {
        hasPie() -> runCatching { context.getPackageInfo().longVersionCode }.getOrDefault(0L)
        else -> runCatching { context.getPackageInfo().versionCode.toLong() }.getOrDefault(0L)
    }

    /**
     * Checks if the device has Marshmallow or higher version.
     * @return `true` if device is a tablet.
     */
    fun hasMarshmallow(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    /**
     * Checks if the device has Marshmallow or higher version.
     * @return `true` if device is a tablet.
     */
    fun hasNougat(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

    fun hasPie(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
}
