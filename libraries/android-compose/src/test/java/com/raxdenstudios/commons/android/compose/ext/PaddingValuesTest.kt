package com.raxdenstudios.commons.android.compose.ext

import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class PaddingValuesTest {

    private val activity = Robolectric.buildActivity(ComponentActivity::class.java).setup().get()
    private val first = PaddingValues(start = 1.dp, top = 2.dp, end = 3.dp, bottom = 4.dp)
    private val second = PaddingValues(start = 10.dp, top = 20.dp, end = 30.dp, bottom = 40.dp)

    @Test
    fun `Given two paddings When plus Then top and bottom are summed`() {
        val result = first + second

        assertThat(result.calculateTopPadding()).isEqualTo(22.dp)
        assertThat(result.calculateBottomPadding()).isEqualTo(44.dp)
    }

    @Test
    fun `Given two paddings When plus in Ltr Then left is start sum and right is end sum`() {
        val result = first + second

        assertThat(result.calculateLeftPadding(LayoutDirection.Ltr)).isEqualTo(11.dp)
        assertThat(result.calculateRightPadding(LayoutDirection.Ltr)).isEqualTo(33.dp)
    }

    @Test
    fun `Given two paddings When plus in Rtl Then left is end sum and right is start sum`() {
        val result = first + second

        assertThat(result.calculateLeftPadding(LayoutDirection.Rtl)).isEqualTo(33.dp)
        assertThat(result.calculateRightPadding(LayoutDirection.Rtl)).isEqualTo(11.dp)
    }

    @Test
    fun `Given equal operands When plus Then results are equal with the same hashCode`() {
        val a = first + second
        val b = PaddingValues(start = 1.dp, top = 2.dp, end = 3.dp, bottom = 4.dp) +
            PaddingValues(start = 10.dp, top = 20.dp, end = 30.dp, bottom = 40.dp)

        assertThat(a).isEqualTo(b)
        assertThat(a.hashCode()).isEqualTo(b.hashCode())
    }

    @Test
    fun `Given different operands When plus Then results are not equal`() {
        assertThat(first + second).isNotEqualTo(second + second)
        assertThat(first + second).isNotEqualTo(first + first)
    }

    @Test
    fun `Given a state backed operand When it changes after plus Then the result follows`() {
        var top by mutableStateOf(5.dp)
        val stateBacked = object : PaddingValues {
            override fun calculateLeftPadding(
                layoutDirection: LayoutDirection,
            ): Dp = 0.dp
            override fun calculateRightPadding(
                layoutDirection: LayoutDirection,
            ): Dp = 0.dp
            override fun calculateTopPadding(): Dp = top
            override fun calculateBottomPadding(): Dp = 0.dp
        }
        val result = first + stateBacked

        assertThat(result.calculateTopPadding()).isEqualTo(7.dp)

        top = 15.dp

        assertThat(result.calculateTopPadding()).isEqualTo(17.dp)
    }

    @Test
    fun `Given Ltr When copy with only top Then start end and bottom are kept`() {
        val result = first.copy(layoutDirection = LayoutDirection.Ltr, top = 9.dp)

        assertThat(result.calculateStartPadding(LayoutDirection.Ltr)).isEqualTo(1.dp)
        assertThat(result.calculateTopPadding()).isEqualTo(9.dp)
        assertThat(result.calculateEndPadding(LayoutDirection.Ltr)).isEqualTo(3.dp)
        assertThat(result.calculateBottomPadding()).isEqualTo(4.dp)
    }

    @Test
    fun `Given Rtl When copy with only top Then start end and bottom are kept`() {
        val result = first.copy(layoutDirection = LayoutDirection.Rtl, top = 9.dp)

        assertThat(result.calculateStartPadding(LayoutDirection.Rtl)).isEqualTo(1.dp)
        assertThat(result.calculateTopPadding()).isEqualTo(9.dp)
        assertThat(result.calculateEndPadding(LayoutDirection.Rtl)).isEqualTo(3.dp)
        assertThat(result.calculateBottomPadding()).isEqualTo(4.dp)
    }

    @Test
    fun `Given Rtl When copy with start Then only start changes`() {
        val result = first.copy(layoutDirection = LayoutDirection.Rtl, start = 8.dp)

        assertThat(result.calculateStartPadding(LayoutDirection.Rtl)).isEqualTo(8.dp)
        assertThat(result.calculateEndPadding(LayoutDirection.Rtl)).isEqualTo(3.dp)
        assertThat(result.calculateRightPadding(LayoutDirection.Rtl)).isEqualTo(8.dp)
    }

    @Test
    fun `Given Ltr When copy with end Then only end changes`() {
        val result = first.copy(layoutDirection = LayoutDirection.Ltr, end = 8.dp)

        assertThat(result.calculateStartPadding(LayoutDirection.Ltr)).isEqualTo(1.dp)
        assertThat(result.calculateEndPadding(LayoutDirection.Ltr)).isEqualTo(8.dp)
        assertThat(result.calculateRightPadding(LayoutDirection.Ltr)).isEqualTo(8.dp)
    }

    @Test
    fun `Given Ltr When composable copy Then it matches the overload`() {
        assertComposableCopyMatchesOverload(LayoutDirection.Ltr)
    }

    @Test
    fun `Given Rtl When composable copy Then it matches the overload`() {
        assertComposableCopyMatchesOverload(LayoutDirection.Rtl)
    }

    @Test
    fun `Given Ltr When composable copy with start and bottom Then only those change`() {
        var composed: PaddingValues? = null
        activity.setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                composed = first.copy(start = 11.dp, bottom = 12.dp)
            }
        }
        shadowOf(Looper.getMainLooper()).idle()

        val actual = checkNotNull(composed)
        assertThat(actual.calculateStartPadding(LayoutDirection.Ltr)).isEqualTo(11.dp)
        assertThat(actual.calculateBottomPadding()).isEqualTo(12.dp)
        assertThat(actual.calculateTopPadding()).isEqualTo(first.calculateTopPadding())
        assertThat(actual.calculateEndPadding(LayoutDirection.Ltr))
            .isEqualTo(first.calculateEndPadding(LayoutDirection.Ltr))
    }

    private fun assertComposableCopyMatchesOverload(
        direction: LayoutDirection,
    ) {
        var composed: PaddingValues? = null
        activity.setContent {
            CompositionLocalProvider(LocalLayoutDirection provides direction) {
                composed = first.copy(top = 9.dp, end = 7.dp)
            }
        }
        shadowOf(Looper.getMainLooper()).idle()

        val expected = first.copy(layoutDirection = direction, top = 9.dp, end = 7.dp)
        val actual = checkNotNull(composed)
        assertThat(actual.calculateStartPadding(direction)).isEqualTo(expected.calculateStartPadding(direction))
        assertThat(actual.calculateEndPadding(direction)).isEqualTo(expected.calculateEndPadding(direction))
        assertThat(actual.calculateTopPadding()).isEqualTo(expected.calculateTopPadding())
        assertThat(actual.calculateBottomPadding()).isEqualTo(expected.calculateBottomPadding())
    }

    @Test
    fun `Given other with larger bottom When coerceBottomAtLeast Then bottom is the other one`() {
        val result = first.coerceBottomAtLeast(LayoutDirection.Ltr, second)

        assertThat(result.calculateBottomPadding()).isEqualTo(40.dp)
    }

    @Test
    fun `Given other with smaller bottom When coerceBottomAtLeast Then bottom is kept`() {
        val result = second.coerceBottomAtLeast(LayoutDirection.Ltr, first)

        assertThat(result.calculateBottomPadding()).isEqualTo(40.dp)
    }

    @Test
    fun `Given Ltr When coerceBottomAtLeast Then start top and end are kept`() {
        val result = first.coerceBottomAtLeast(LayoutDirection.Ltr, second)

        assertThat(result.calculateStartPadding(LayoutDirection.Ltr)).isEqualTo(1.dp)
        assertThat(result.calculateTopPadding()).isEqualTo(2.dp)
        assertThat(result.calculateEndPadding(LayoutDirection.Ltr)).isEqualTo(3.dp)
    }

    @Test
    fun `Given Rtl When coerceBottomAtLeast Then start and end resolve with the direction`() {
        val result = first.coerceBottomAtLeast(LayoutDirection.Rtl, second)

        assertThat(result.calculateStartPadding(LayoutDirection.Rtl)).isEqualTo(1.dp)
        assertThat(result.calculateEndPadding(LayoutDirection.Rtl)).isEqualTo(3.dp)
        assertThat(result.calculateLeftPadding(LayoutDirection.Rtl)).isEqualTo(3.dp)
        assertThat(result.calculateRightPadding(LayoutDirection.Rtl)).isEqualTo(1.dp)
        assertThat(result.calculateBottomPadding()).isEqualTo(40.dp)
    }

    @Test
    fun `Given Rtl When composable coerceBottomAtLeast Then it matches the overload`() {
        var composed: PaddingValues? = null
        activity.setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                composed = first.coerceBottomAtLeast(second)
            }
        }
        shadowOf(Looper.getMainLooper()).idle()

        val actual = checkNotNull(composed)
        assertThat(actual.calculateStartPadding(LayoutDirection.Rtl)).isEqualTo(1.dp)
        assertThat(actual.calculateEndPadding(LayoutDirection.Rtl)).isEqualTo(3.dp)
        assertThat(actual.calculateBottomPadding()).isEqualTo(40.dp)
    }
}
