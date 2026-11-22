package com.raxdenstudios.commons.android.compose.ext

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * Returns a copy of these paddings with only the given sides replaced. Sides left at their default keep the
 * current value. The defaults for [start] and [end] read the current [LocalLayoutDirection], so they follow RTL.
 *
 * Outside composition, use the overload that takes a [LayoutDirection].
 */
@Composable
fun PaddingValues.copy(
    start: Dp = calculateStartPadding(LocalLayoutDirection.current),
    top: Dp = calculateTopPadding(),
    end: Dp = calculateEndPadding(LocalLayoutDirection.current),
    bottom: Dp = calculateBottomPadding(),
): PaddingValues = copy(
    layoutDirection = LocalLayoutDirection.current,
    start = start,
    top = top,
    end = end,
    bottom = bottom,
)

/**
 * Returns a copy of these paddings with only the given sides replaced. Sides left at their default keep the
 * current value. The defaults for [start] and [end] are resolved with [layoutDirection], so they follow RTL.
 *
 * Unlike the composable overload, this one can be called anywhere (e.g. from a `remember` block or a
 * non-composable helper) because the layout direction is passed explicitly.
 */
fun PaddingValues.copy(
    layoutDirection: LayoutDirection,
    start: Dp = calculateStartPadding(layoutDirection),
    top: Dp = calculateTopPadding(),
    end: Dp = calculateEndPadding(layoutDirection),
    bottom: Dp = calculateBottomPadding(),
): PaddingValues = PaddingValues(
    start = start,
    top = top,
    end = end,
    bottom = bottom,
)

/**
 * Sums two paddings side by side: each side of the result is the sum of the same side in both values.
 *
 * The sum is resolved lazily, so paddings backed by state (e.g. window insets) stay up to date. Two sums of
 * operands are equal whenever the operands themselves are equal (`PaddingValues(...)` compares by value;
 * inset-backed paddings compare by identity), which lets composables receiving the result skip recomposition.
 */
operator fun PaddingValues.plus(
    that: PaddingValues,
): PaddingValues = SumPaddingValues(first = this, second = that)

@Stable
private data class SumPaddingValues(
    private val first: PaddingValues,
    private val second: PaddingValues,
) : PaddingValues {

    override fun calculateBottomPadding(): Dp = first.calculateBottomPadding() + second.calculateBottomPadding()

    override fun calculateLeftPadding(
        layoutDirection: LayoutDirection,
    ): Dp = first.calculateLeftPadding(layoutDirection) + second.calculateLeftPadding(layoutDirection)

    override fun calculateRightPadding(
        layoutDirection: LayoutDirection,
    ): Dp = first.calculateRightPadding(layoutDirection) + second.calculateRightPadding(layoutDirection)

    override fun calculateTopPadding(): Dp = first.calculateTopPadding() + second.calculateTopPadding()
}
