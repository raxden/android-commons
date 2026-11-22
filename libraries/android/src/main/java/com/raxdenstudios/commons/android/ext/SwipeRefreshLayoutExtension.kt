package com.raxdenstudios.commons.android.ext

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Jetpack Compose pull-to-refresh or SwipeRefreshLayout.setProgressViewEndTarget directly.",
    level = DeprecationLevel.WARNING,
)
fun SwipeRefreshLayout.addProgressViewEndTarget(
    scale: Boolean = false,
    end: Int,
) {
    setProgressViewEndTarget(scale, progressViewEndOffset + end)
}
