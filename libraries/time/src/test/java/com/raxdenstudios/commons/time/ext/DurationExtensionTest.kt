package com.raxdenstudios.commons.time.ext

import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Test
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

internal class DurationExtensionTest {

    private val originalLocale: Locale = Locale.getDefault()

    @After
    fun restoreDefaultLocale() {
        Locale.setDefault(originalLocale)
    }

    @Test
    fun `Given zero duration When toClockText Then returns 00 00`() {
        assertThat(Duration.ZERO.toClockText()).isEqualTo("00:00")
    }

    @Test
    fun `Given 59 point 9 seconds When toClockText Then truncates to 00 59`() {
        assertThat(59.9.seconds.toClockText()).isEqualTo("00:59")
    }

    @Test
    fun `Given 61 seconds When toClockText Then returns 01 01`() {
        assertThat(61.seconds.toClockText()).isEqualTo("01:01")
    }

    @Test
    fun `Given 59 minutes 59 seconds When toClockText Then returns 59 59`() {
        assertThat((59.minutes + 59.seconds).toClockText()).isEqualTo("59:59")
    }

    @Test
    fun `Given exactly one hour When toClockText Then returns 1 00 00`() {
        assertThat(1.hours.toClockText()).isEqualTo("1:00:00")
    }

    @Test
    fun `Given 25 hours When toClockText Then returns 25 00 00`() {
        assertThat(25.hours.toClockText()).isEqualTo("25:00:00")
    }

    @Test
    fun `Given negative duration When toClockText Then clamps to 00 00`() {
        assertThat((-5).seconds.toClockText()).isEqualTo("00:00")
    }

    @Test
    fun `Given sub second duration When toClockText Then returns 00 00`() {
        assertThat(999.milliseconds.toClockText()).isEqualTo("00:00")
    }

    @Test
    fun `Given 90 minutes When toClockText Then returns 1 30 00`() {
        assertThat(90.minutes.toClockText()).isEqualTo("1:30:00")
    }

    @Test
    fun `Given default locale with non ascii digits When toClockText Then still returns ascii digits`() {
        Locale.setDefault(Locale.forLanguageTag("ar-EG"))

        assertThat((1.hours + 2.minutes + 3.seconds).toClockText()).isEqualTo("1:02:03")
        assertThat(65.seconds.toClockText()).isEqualTo("01:05")
    }
}
