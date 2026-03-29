package com.raxdenstudios.commons.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.threetenabp.ext.isToday
import com.raxdenstudios.commons.threetenabp.ext.toFormat
import com.raxdenstudios.commons.threetenabp.ext.toMilliseconds
import com.raxdenstudios.commons.threetenabp.ext.toSeconds
import com.raxdenstudios.commons.threetenabp.test.rules.ThreeTenRule
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDate
import java.util.TimeZone

internal class LocalDateExtensionTest {

    @get:Rule
    val threeTenRule = ThreeTenRule()

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
}
