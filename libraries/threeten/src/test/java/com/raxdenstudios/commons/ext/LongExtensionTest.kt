package com.raxdenstudios.commons.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.threeten.ext.toLocalDate
import com.raxdenstudios.commons.threeten.ext.toLocalDateTime
import com.raxdenstudios.commons.treeten.test.rules.ThreeTenRule
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
}
