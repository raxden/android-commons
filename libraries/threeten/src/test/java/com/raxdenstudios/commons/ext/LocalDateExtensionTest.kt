package com.raxdenstudios.commons.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.threeten.ext.isToday
import com.raxdenstudios.commons.threeten.ext.toFormat
import com.raxdenstudios.commons.threeten.ext.toMilliseconds
import com.raxdenstudios.commons.threeten.ext.toSeconds
import com.raxdenstudios.commons.treeten.test.rules.ThreeTenRule
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
}
