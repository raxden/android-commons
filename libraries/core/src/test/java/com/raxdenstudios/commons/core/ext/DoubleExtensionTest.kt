package com.raxdenstudios.commons.core.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

internal class DoubleExtensionTest {

    @Test
    fun `Given a Double, When toString is called with default params, Then return formatted string`() {
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
    fun `Given a Double with more fraction digits than maximum, When toString is called, Then round to maximum`() {
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

    @Test
    fun `Given USD and US locale, When toCurrencyString is called, Then return dollar format`() {
        val result = 1234.5.toCurrencyString(Currency.getInstance("USD"), Locale.US)

        assertThat(result).isEqualTo("$1,234.50")
    }

    @Test
    fun `Given EUR and German locale, When toCurrencyString is called, Then match NumberFormat output`() {
        val currency = Currency.getInstance("EUR")
        val expected = NumberFormat.getCurrencyInstance(Locale.GERMANY).apply { this.currency = currency }
            .format(1234.5)

        val result = 1234.5.toCurrencyString(currency, Locale.GERMANY)

        assertThat(result).isEqualTo(expected)
        assertThat(result).contains("1.234,50")
        assertThat(result).contains("€")
    }

    @Test
    fun `Given zero, When toCurrencyString is called, Then return formatted zero`() {
        val result = 0.0.toCurrencyString(Currency.getInstance("USD"), Locale.US)

        assertThat(result).isEqualTo("$0.00")
    }
}
