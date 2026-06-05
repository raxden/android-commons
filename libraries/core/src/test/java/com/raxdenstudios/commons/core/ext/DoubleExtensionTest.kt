package com.raxdenstudios.commons.core.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.Locale

internal class DoubleExtensionTest {

    @Test
    fun `Given a Double, When toString is called with default params, Then return formatted string with 3 fraction digits`() {
        val value = 1.5

        val result = value.toString(Locale.US)

        assertThat(result).isEqualTo("1.500")
    }

    @Test
    fun `Given a Double, When toString is called with German locale, Then use comma as decimal separator`() {
        val value = 1.5

        val result = value.toString(Locale.GERMANY)

        assertThat(result).isEqualTo("1,500")
    }

    @Test
    fun `Given a Double, When toString is called with custom fraction digits, Then return formatted string`() {
        val value = 1.5

        val result = value.toString(Locale.US, minimumFractionDigits = 1, maximumFractionDigits = 1)

        assertThat(result).isEqualTo("1.5")
    }

    @Test
    fun `Given a Double with more fraction digits than maximum, When toString is called, Then round to maximum fraction digits`() {
        val value = 1.5555

        val result = value.toString(Locale.US, maximumFractionDigits = 3)

        assertThat(result).isEqualTo("1.556")
    }

    @Test
    fun `Given a Double with grouping, When toString is called with US locale, Then include grouping separator`() {
        val value = 1234.5

        val result = value.toString(Locale.US)

        assertThat(result).isEqualTo("1,234.500")
    }
}
