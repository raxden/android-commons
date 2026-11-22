@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.android.ext

import android.view.View
import androidx.core.widget.NestedScrollView

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use NestedScrollView.fullScroll directly or Jetpack Compose scroll state.",
    level = DeprecationLevel.WARNING,
)
const val SHORT_DELAY: Long = 250

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use NestedScrollView.fullScroll directly or Jetpack Compose scroll state.",
    level = DeprecationLevel.WARNING,
)
fun NestedScrollView.scrollToBottom() {
    post { fullScroll(View.FOCUS_DOWN) }
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use NestedScrollView.fullScroll directly or Jetpack Compose scroll state.",
    level = DeprecationLevel.WARNING,
)
fun NestedScrollView.scrollToBottomWithDelay(
    delayInMillis: Long = SHORT_DELAY,
) {
    postDelayed(
        { fullScroll(View.FOCUS_DOWN) },
        delayInMillis,
    )
}
