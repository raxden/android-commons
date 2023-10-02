package com.raxdenstudios.commons.android.ext

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

fun SwipeRefreshLayout.addProgressViewEndTarget(scale: Boolean = false, end: Int) {
    setProgressViewEndTarget(scale, progressViewEndOffset + end)
}
