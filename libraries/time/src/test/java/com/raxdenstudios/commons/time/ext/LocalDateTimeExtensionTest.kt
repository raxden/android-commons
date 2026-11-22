@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.time.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.time.ext.toFormat
import com.raxdenstudios.commons.time.ext.toMilliseconds
import com.raxdenstudios.commons.time.ext.toSeconds
import org.junit.Test
import java.time.LocalDateTime
import java.util.TimeZone

internal class LocalDateTimeExtensionTest {

    private val timeZone = TimeZone.getTimeZone("Europe/Madrid")

    @Test
    fun `toSeconds should be return a right value`() {
        val aValidDate = LocalDateTime.of(2023, 9, 22, 11, 32, 7, 0)

        val result = aValidDate.toSeconds(timeZone)

        assertThat(result).isEqualTo(1695375127)
    }

    @Test
    fun `toMilliseconds should be return a right value`() {
        val aValidDate = LocalDateTime.of(2023, 9, 22, 11, 32, 7, 0)

        val result = aValidDate.toMilliseconds(timeZone)

        assertThat(result).isEqualTo(1695375127000)
    }

    @Test
    fun `toFormat should be return a right value`() {
        val aValidDate = LocalDateTime.of(2023, 9, 22, 11, 32, 7, 0)

        val result = aValidDate.toFormat()

        assertThat(result).isEqualTo("22 Sep 2023")
    }

    @Test
    fun `toFormat with custom pattern including time should return formatted string`() {
        val dateTime = LocalDateTime.of(2023, 9, 22, 11, 32, 7, 0)

        val result = dateTime.toFormat("yyyy-MM-dd HH:mm:ss")

        assertThat(result).isEqualTo("2023-09-22 11:32:07")
    }

    @Test
    fun `toFormat with time pattern should return formatted time`() {
        val dateTime = LocalDateTime.of(2023, 9, 22, 11, 32, 7, 0)

        val result = dateTime.toFormat("HH:mm")

        assertThat(result).isEqualTo("11:32")
    }

    @Test
    fun `toSeconds with different timezone should return different value`() {
        val dateTime = LocalDateTime.of(2023, 9, 22, 11, 32, 7, 0)
        val utcTimeZone = TimeZone.getTimeZone("UTC")

        val result = dateTime.toSeconds(utcTimeZone)

        assertThat(result).isEqualTo(1695382327)
    }

    @Test
    fun `toMilliseconds with midnight time should return correct value`() {
        val dateTime = LocalDateTime.of(2023, 9, 22, 0, 0, 0, 0)

        val result = dateTime.toMilliseconds(timeZone)

        assertThat(result).isEqualTo(1695333600000)
    }

    @Test
    fun `Given a date-time, When toHourMinutes is called, Then return zero padded 24-hour time`() {
        assertThat(LocalDateTime.of(2024, 3, 15, 9, 5, 59).toHourMinutes()).isEqualTo("09:05")
        assertThat(LocalDateTime.of(2024, 3, 15, 23, 45).toHourMinutes()).isEqualTo("23:45")
    }

    @Test
    fun `Given a pre epoch date time with fraction, When toSeconds is called, Then floor toward negative infinity`() {
        val aDate = LocalDateTime.of(1969, 12, 31, 23, 59, 59, 500_000_000)

        val result = aDate.toSeconds(TimeZone.getTimeZone("UTC"))

        assertThat(result).isEqualTo(-1)
    }

    @Test
    fun `Given a post epoch date time, When toSeconds is called, Then value is unchanged`() {
        val aDate = LocalDateTime.of(2023, 9, 22, 11, 32, 7, 999_000_000)

        val result = aDate.toSeconds(TimeZone.getTimeZone("UTC"))

        assertThat(result).isEqualTo(1695382327)
    }
}
