@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.time.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.time.ext.isToday
import com.raxdenstudios.commons.time.ext.toFormat
import com.raxdenstudios.commons.time.ext.toMilliseconds
import com.raxdenstudios.commons.time.ext.toSeconds
import org.junit.Test
import java.time.LocalDate
import java.time.format.FormatStyle
import java.util.Locale
import java.util.TimeZone

internal class LocalDateExtensionTest {

    private val timeZone = TimeZone.getTimeZone("Europe/Madrid")

    @Test
    fun `toSeconds should be return a right value`() {
        val aValidDate = LocalDate.of(2023, 9, 22)

        val result = aValidDate.toSeconds(timeZone)

        assertThat(result).isEqualTo(1695333600)
    }

    @Test
    fun `toMilliseconds should be return a right value`() {
        val aValidDate = LocalDate.of(2023, 9, 22)

        val result = aValidDate.toMilliseconds(timeZone)

        assertThat(result).isEqualTo(1695333600000)
    }

    @Test
    fun `toFormat should be return a right value`() {
        val aValidDate = LocalDate.of(2023, 9, 22)

        val result = aValidDate.toFormat()

        assertThat(result).isEqualTo("22 Sep 2023")
    }

    @Test
    fun `isToday should be return a right value`() {
        val aValidDate = LocalDate.now()

        val result = aValidDate.isToday()

        assertThat(result).isTrue()
    }

    @Test
    fun `isToday should return false for past date`() {
        val pastDate = LocalDate.now().minusDays(1)

        val result = pastDate.isToday()

        assertThat(result).isFalse()
    }

    @Test
    fun `isToday should return false for future date`() {
        val futureDate = LocalDate.now().plusDays(1)

        val result = futureDate.isToday()

        assertThat(result).isFalse()
    }

    @Test
    fun `toFormat with custom pattern should return formatted string`() {
        val date = LocalDate.of(2023, 9, 22)

        val result = date.toFormat("yyyy/MM/dd")

        assertThat(result).isEqualTo("2023/09/22")
    }

    @Test
    fun `toFormat with different pattern should return formatted string`() {
        val date = LocalDate.of(2023, 9, 22)

        val result = date.toFormat("dd-MM-yyyy")

        assertThat(result).isEqualTo("22-09-2023")
    }

    @Test
    fun `toSeconds with different timezone should return different value`() {
        val date = LocalDate.of(2023, 9, 22)
        val utcTimeZone = TimeZone.getTimeZone("UTC")

        val result = date.toSeconds(utcTimeZone)

        assertThat(result).isEqualTo(1695340800)
    }

    @Test
    fun `Given a date, When toLocalizedString is called with LONG and US locale, Then return long format`() {
        val result = LocalDate.of(2024, 3, 15).toLocalizedString(FormatStyle.LONG, Locale.US)

        assertThat(result).isEqualTo("March 15, 2024")
    }

    @Test
    fun `Given a date, When toLocalizedString is called with SHORT and US locale, Then return short format`() {
        val result = LocalDate.of(2024, 3, 15).toLocalizedString(FormatStyle.SHORT, Locale.US)

        assertThat(result).isEqualTo("3/15/24")
    }

    @Test
    fun `Given a pre epoch date, When toSeconds is called, Then return minus one day in seconds`() {
        val result = LocalDate.of(1969, 12, 31).toSeconds(TimeZone.getTimeZone("UTC"))

        assertThat(result).isEqualTo(-86400)
    }

    @Test
    fun `Given a post epoch date, When toSeconds is called, Then value is unchanged`() {
        val result = LocalDate.of(1970, 1, 2).toSeconds(TimeZone.getTimeZone("UTC"))

        assertThat(result).isEqualTo(86400)
    }
}
