package com.raxdenstudios.commons.android.ext

import android.annotation.SuppressLint
import android.content.Context

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use WindowInsetsCompat with navigationBars() instead.",
    level = DeprecationLevel.WARNING,
)
fun Context.hasVirtualNavigationBar(): Boolean {
    val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return id > 0 && resources.getBoolean(id)
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use WindowInsetsCompat with navigationBars() instead.",
    level = DeprecationLevel.WARNING,
)
@SuppressLint("InternalInsetResource")
fun Context.getVirtualNavigationBarHeight(): Int {
    val id = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (id > 0) {
        resources.getDimensionPixelSize(id)
    } else {
        0
    }
}
