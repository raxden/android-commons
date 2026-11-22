package com.raxdenstudios.commons.android.ext

import android.content.res.Resources

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Compose Dp or TypedValue.applyDimension instead.",
    level = DeprecationLevel.WARNING,
)
fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Compose Dp or TypedValue.applyDimension instead.",
    level = DeprecationLevel.WARNING,
)
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
