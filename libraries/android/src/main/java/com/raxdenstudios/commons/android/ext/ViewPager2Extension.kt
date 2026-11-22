@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.android.ext

import androidx.viewpager2.widget.ViewPager2

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Jetpack Compose Pager or the ViewPager2 API directly.",
    level = DeprecationLevel.WARNING,
)
fun ViewPager2.isFirstPage() = currentItem == 0

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Jetpack Compose Pager or the ViewPager2 API directly.",
    level = DeprecationLevel.WARNING,
)
fun ViewPager2.isLastPage() = currentItem == adapter?.let { it.itemCount - 1 } ?: false

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Jetpack Compose Pager or the ViewPager2 API directly.",
    level = DeprecationLevel.WARNING,
)
fun ViewPager2.previousPage(): Boolean = if (isFirstPage()) {
    false
} else {
    currentItem -= 1
    true
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Jetpack Compose Pager or the ViewPager2 API directly.",
    level = DeprecationLevel.WARNING,
)
fun ViewPager2.nextPage(): Boolean = if (isLastPage()) {
    false
} else {
    currentItem += 1
    true
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Jetpack Compose Pager or the ViewPager2 API directly.",
    level = DeprecationLevel.WARNING,
)
fun ViewPager2.registerOnPageChangeCallback(
    onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit = { _, _, _ -> },
    onPageSelected: (position: Int) -> Unit = {},
    onPageScrollStateChanged: (state: Int) -> Unit = {},
) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int,
        ) {
            onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(
            position: Int,
        ) {
            onPageSelected(position)
        }

        override fun onPageScrollStateChanged(
            state: Int,
        ) {
            onPageScrollStateChanged(state)
        }
    })
}
