package com.raxdenstudios.commons.android

import androidx.activity.ComponentActivity
import com.raxdenstudios.commons.android.ext.isAtLeastResumed

/**
 * The attached activity only while it is resumed (in the foreground); `null` when none is attached or it is
 * paused, stopped or destroyed. Use it before showing UI or launching work that needs a visible screen.
 */
fun ActivityHolder.resumedActivity(): ComponentActivity? = activity?.takeIf { it.isAtLeastResumed() }
