package com.raxdenstudios.commons.android.ext

import androidx.recyclerview.widget.RecyclerView

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use RecyclerView.addOnScrollListener directly or Jetpack Compose lists.",
    level = DeprecationLevel.WARNING,
)
fun RecyclerView.addOnScrolledListener(
    onScrolled: (RecyclerView, Int, Int) -> Unit = { _, _, _ -> },
): RecyclerView.OnScrollListener {
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(
            recyclerView: RecyclerView,
            dx: Int,
            dy: Int,
        ) {
            onScrolled(recyclerView, dx, dy)
        }
    }
    addOnScrollListener(listener)
    return listener
}
