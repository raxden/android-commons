package com.raxdenstudios.commons.time.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.time.ext.toMilliseconds
import com.raxdenstudios.commons.time.ext.toSeconds
import org.junit.After
import org.junit.Test
import java.time.LocalDate
import java.util.TimeZone

internal class LocalDateExtensionTest {

    private val timeZone = TimeZone.getTimeZone("Europe/Madrid")

    private val originalDefault: TimeZone = TimeZone.getDefault()

    @After
    fun restoreDefaultTimeZone() {
        TimeZone.setDefault(originalDefault)
    }

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
    fun `toSeconds with different timezone should return different value`() {
        val date = LocalDate.of(2023, 9, 22)
        val utcTimeZone = TimeZone.getTimeZone("UTC")

        val result = date.toSeconds(utcTimeZone)

        assertThat(result).isEqualTo(1695340800)
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

    @Test
    fun `Given a default time zone When toMilliseconds without zone Then uses the default zone`() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"))

        assertThat(LocalDate.of(2023, 9, 22).toMilliseconds()).isEqualTo(1695333600000)
    }

    @Test
    fun `Given a default time zone When toSeconds without zone Then uses the default zone`() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"))

        assertThat(LocalDate.of(2023, 9, 22).toSeconds()).isEqualTo(1695333600)
    }

    @Test
    fun `Given a zone whose midnight is skipped by DST When toMilliseconds Then starts at the first valid time`() {
        val sao = TimeZone.getTimeZone("America/Sao_Paulo")
        // 2018-11-04: Sao Paulo skipped 00:00 and went straight to 01:00 (-02:00).
        val result = LocalDate.of(2018, 11, 4).toMilliseconds(sao)

        assertThat(result).isEqualTo(1541300400000)
    }
}
