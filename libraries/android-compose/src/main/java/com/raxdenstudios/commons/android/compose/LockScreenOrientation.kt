package com.raxdenstudios.commons.android.compose

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

/**
 * Locks the host activity to [orientation] while this composable is in the composition, and restores the
 * orientation the activity had before once it leaves.
 *
 * The lock is applied to the activity that hosts the composition, found through the context. If no activity
 * is found (for example in a preview), nothing changes. When [orientation] changes, the previous value is
 * restored and the new one applied.
 *
 * ## Usage
 *
 * ```kotlin
 * @Composable
 * fun VideoScreen() {
 *     LockScreenOrientation(orientation = ScreenOrientation.Landscape)
 *     // ...
 * }
 * ```
 */
@Composable
fun LockScreenOrientation(
    orientation: ScreenOrientation,
) {
    val context = LocalContext.current
    DisposableEffect(context, orientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation.toActivityInfoOrientation()
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}

private fun ScreenOrientation.toActivityInfoOrientation(): Int = when (this) {
    ScreenOrientation.Unspecified -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    ScreenOrientation.Portrait -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    ScreenOrientation.Landscape -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
