package com.raxdenstudios.commons.time.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.DayOfWeek
import java.util.Locale

internal class DayOfWeekExtensionTest {

    @Test
    fun `Given MONDAY in English When shortName Then returns Mon`() {
        assertThat(DayOfWeek.MONDAY.shortName(Locale.ENGLISH)).isEqualTo("Mon")
    }

    @Test
    fun `Given MONDAY in English When narrowName Then returns M`() {
        assertThat(DayOfWeek.MONDAY.narrowName(Locale.ENGLISH)).isEqualTo("M")
    }

    @Test
    fun `Given MONDAY in Spanish When shortName Then returns the localized name`() {
        assertThat(DayOfWeek.MONDAY.shortName(Locale("es"))).isEqualTo("lun")
    }
}
