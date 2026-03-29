package com.raxdenstudios.commons.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.threetenabp.ext.toLocalDate
import com.raxdenstudios.commons.threetenabp.ext.toLocalDateTime
import com.raxdenstudios.commons.threetenabp.test.rules.ThreeTenRule
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.TimeZone

internal class LongExtensionTest {

    @get:Rule
    val threeTenRule = ThreeTenRule()

    private val timeZone = TimeZone.getTimeZone("Europe/Madrid")

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
}
