package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import com.raxdenstudios.commons.android.util.Network

/**
 * Returns the [PackageInfo] of this app. Uses the typed flags overload on API 33+ and the
 * deprecated `getPackageInfo(String, Int)` below it. Throws `NameNotFoundException` if the
 * package is not found, which is not expected for the app's own package.
 */
fun Context.getPackageInfo(): PackageInfo = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))

    else ->
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(packageName, 0)
}

/**
 * Returns the package name without [debugSuffix], for example to build Play Store or share links
 * to the release app from a debug build. [debugSuffix] defaults to `.debug`.
 *
 * Only a trailing suffix is removed, and only once: `com.example.app.debug.debug` becomes
 * `com.example.app.debug`. A name that does not end with [debugSuffix] is returned unchanged.
 */
fun Context.releasePackageName(
    debugSuffix: String = DEBUG_PACKAGE_SUFFIX,
): String = packageName.removeSuffix(debugSuffix)

private const val DEBUG_PACKAGE_SUFFIX = ".debug"

/** Epoch millis when this app was first installed (kept across updates), or `0` when it cannot be read. */
fun Context.firstInstallTimeMillis(): Long = runCatching { getPackageInfo().firstInstallTime }
    .getOrDefault(0L)

/**
 * Whether exact alarms may be scheduled. Always `true` below Android 12 (API 31); from API 31 it
 * reflects [AlarmManager.canScheduleExactAlarms], which the user can revoke at any time.
 *
 * When this returns `false`, open `IntentType.ExactAlarmSettings` so the user can grant it.
 */
fun Context.isExactAlarmAllowed(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
    getSystemService(AlarmManager::class.java).canScheduleExactAlarms()

/**
 * Whether full-screen intents may be used. Always `true` below Android 14 (API 34); from API 34 it
 * reflects [NotificationManager.canUseFullScreenIntent].
 *
 * When this returns `false`, open `IntentType.FullScreenIntentSettings` so the user can grant it.
 */
fun Context.isFullScreenIntentAllowed(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE ||
    getSystemService(NotificationManager::class.java).canUseFullScreenIntent()

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.getConnectivityManager(): ConnectivityManager =
    this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.isNetworkConnected(): Boolean = Network.isNetworkConnected(this)
