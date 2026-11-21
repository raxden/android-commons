package com.raxdenstudios.commons.android.compose.ext

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class PaddingValuesTest {

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
}
