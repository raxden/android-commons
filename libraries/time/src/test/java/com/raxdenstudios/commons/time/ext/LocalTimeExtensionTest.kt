package com.raxdenstudios.commons.time.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

internal class LocalTimeExtensionTest {

    private val time = LocalTime.of(9, 5)

    @Test
    fun `Given a locale When toShortText Then matches the localized SHORT formatter`() {
        listOf(Locale.UK, Locale.US, Locale.GERMANY).forEach { locale ->
            val expected = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale).format(time)

            assertThat(time.toShortText(locale)).isEqualTo(expected)
        }
    }

    @Test
    fun `Given UK locale When toShortText 09 05 Then returns 09 05`() {
        assertThat(time.toShortText(Locale.UK)).isEqualTo("09:05")
    }
}
