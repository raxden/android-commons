package com.raxdenstudios.commons.android.compose.ext

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * Returns a copy of these paddings with only the given sides replaced. Sides left at their default keep the
 * current value. The defaults for [start] and [end] read the current [LocalLayoutDirection], so they follow RTL.
 */
@Composable
fun PaddingValues.copy(
    start: Dp = calculateStartPadding(LocalLayoutDirection.current),
    top: Dp = calculateTopPadding(),
    end: Dp = calculateEndPadding(LocalLayoutDirection.current),
    bottom: Dp = calculateBottomPadding(),
): PaddingValues = PaddingValues(
    start = start,
    top = top,
    end = end,
    bottom = bottom,
)

/** Sums two paddings side by side: each side of the result is the sum of the same side in both values. */
operator fun PaddingValues.plus(
    that: PaddingValues,
): PaddingValues = object : PaddingValues {

    override fun calculateBottomPadding(): Dp = this@plus.calculateBottomPadding() + that.calculateBottomPadding()

    override fun calculateLeftPadding(
        layoutDirection: LayoutDirection,
    ): Dp = this@plus.calculateLeftPadding(layoutDirection) + that.calculateLeftPadding(layoutDirection)

    override fun calculateRightPadding(
        layoutDirection: LayoutDirection,
    ): Dp = this@plus.calculateRightPadding(layoutDirection) + that.calculateRightPadding(
        layoutDirection,
    )

    override fun calculateTopPadding(): Dp = this@plus.calculateTopPadding() + that.calculateTopPadding()
}
