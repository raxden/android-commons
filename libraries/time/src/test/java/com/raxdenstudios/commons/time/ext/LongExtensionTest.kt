package com.raxdenstudios.commons.time.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.time.ext.toLocalDate
import com.raxdenstudios.commons.time.ext.toLocalDateTime
import org.junit.After
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.TimeZone

internal class LongExtensionTest {

    private val timeZone = TimeZone.getTimeZone("Europe/Madrid")

    private val originalDefault: TimeZone = TimeZone.getDefault()

    @After
    fun restoreDefaultTimeZone() {
        TimeZone.setDefault(originalDefault)
    }

    @Test
    fun `toLocalDate should be return a localDate`() {
        val aValidDate = 1695375127000L

        val result = aValidDate.toLocalDate(timeZone)

        assertThat(result).isEqualTo(LocalDate.of(2023, 9, 22))
    }

    @Test
    fun `toLocalDateTime should be return a localDateTime`() {
        val aValidDate = 1695375127000L

        val result = aValidDate.toLocalDateTime(timeZone)

        assertThat(result).isEqualTo(LocalDateTime.of(2023, 9, 22, 11, 32, 7, 0))
    }

    @Test
    fun `toLocalDate with different timezone should return correct date`() {
        val timestamp = 1695375127000L
        val utcTimeZone = TimeZone.getTimeZone("UTC")

        val result = timestamp.toLocalDate(utcTimeZone)

        assertThat(result).isEqualTo(LocalDate.of(2023, 9, 22))
    }

    @Test
    fun `toLocalDateTime with different timezone should return correct datetime`() {
        val timestamp = 1695375127000L
        val utcTimeZone = TimeZone.getTimeZone("UTC")

        val result = timestamp.toLocalDateTime(utcTimeZone)

        assertThat(result).isEqualTo(LocalDateTime.of(2023, 9, 22, 9, 32, 7, 0))
    }

    @Test
    fun `toLocalDate with epoch zero should return correct date`() {
        val epochZero = 0L

        val result = epochZero.toLocalDate(timeZone)

        assertThat(result).isEqualTo(LocalDate.of(1970, 1, 1))
    }

    @Test
    fun `toLocalDateTime with epoch zero should return correct datetime`() {
        val epochZero = 0L

        val result = epochZero.toLocalDateTime(timeZone)

        assertThat(result).isEqualTo(LocalDateTime.of(1970, 1, 1, 1, 0, 0, 0))
    }

    @Test
    fun `Given a default time zone When toLocalDate without zone Then uses the default zone`() {
        TimeZone.setDefault(TimeZone.getTimeZone("Pacific/Auckland"))

        val result = 0L.toLocalDate()

        assertThat(result).isEqualTo(LocalDate.of(1970, 1, 1))
        assertThat(1695375127000L.toLocalDate()).isEqualTo(LocalDate.of(2023, 9, 22))
        assertThat(1695340800000L.toLocalDate()).isEqualTo(LocalDate.of(2023, 9, 22))
    }

    @Test
    fun `Given a default time zone When toLocalDateTime without zone Then uses the default zone`() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"))

        val result = 1695375127000L.toLocalDateTime()

        assertThat(result).isEqualTo(LocalDateTime.of(2023, 9, 22, 11, 32, 7, 0))
    }

    @Test
    fun `Given a negative epoch When toLocalDateTime Then returns a pre epoch value`() {
        val result = (-1L).toLocalDateTime(TimeZone.getTimeZone("UTC"))

        assertThat(result).isEqualTo(LocalDateTime.of(1969, 12, 31, 23, 59, 59, 999_000_000))
    }
}
