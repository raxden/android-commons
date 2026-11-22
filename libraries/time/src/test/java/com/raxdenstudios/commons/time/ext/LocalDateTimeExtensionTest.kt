package com.raxdenstudios.commons.time.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.time.ext.toMilliseconds
import com.raxdenstudios.commons.time.ext.toSeconds
import org.junit.After
import org.junit.Test
import java.time.LocalDateTime
import java.util.TimeZone

internal class LocalDateTimeExtensionTest {

    private val timeZone = TimeZone.getTimeZone("Europe/Madrid")

    private val originalDefault: TimeZone = TimeZone.getDefault()

    @After
    fun restoreDefaultTimeZone() {
        TimeZone.setDefault(originalDefault)
    }

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

    @Test
    fun `Given a default time zone When toMilliseconds without zone Then uses the default zone`() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"))

        assertThat(LocalDateTime.of(2023, 9, 22, 11, 32, 7, 0).toMilliseconds()).isEqualTo(1695375127000)
    }

    @Test
    fun `Given a default time zone When toSeconds without zone Then uses the default zone`() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

        assertThat(LocalDateTime.of(2023, 9, 22, 11, 32, 7, 0).toSeconds()).isEqualTo(1695382327)
    }

    @Test
    fun `Given a DST gap When toMilliseconds Then the time is shifted later by the gap`() {
        val madrid = TimeZone.getTimeZone("Europe/Madrid")
        // 2023-03-26 02:30 does not exist in Madrid: shifted to 03:30 (+02:00) = 01:30 UTC.
        val result = LocalDateTime.of(2023, 3, 26, 2, 30).toMilliseconds(madrid)

        assertThat(result).isEqualTo(1679794200000)
    }

    @Test
    fun `Given a DST overlap When toMilliseconds Then the earlier offset is used`() {
        val madrid = TimeZone.getTimeZone("Europe/Madrid")
        // 2023-10-29 02:30 happens twice in Madrid: earlier offset +02:00 = 00:30 UTC.
        val result = LocalDateTime.of(2023, 10, 29, 2, 30).toMilliseconds(madrid)

        assertThat(result).isEqualTo(1698539400000)
    }
}
