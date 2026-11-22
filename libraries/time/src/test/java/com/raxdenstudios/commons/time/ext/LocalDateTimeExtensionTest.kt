package com.raxdenstudios.commons.time.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDateTime
import java.util.TimeZone

internal class LocalDateTimeExtensionTest {

    private val timeZone = TimeZone.getTimeZone("Europe/Madrid")

    @Test
    fun `toMilliseconds should be return a right value`() {
        val aValidDate = LocalDateTime.of(2023, 9, 22, 11, 32, 7, 0)

        val result = aValidDate.toMilliseconds(timeZone)

        assertThat(result).isEqualTo(1695375127000)
    }

    @Test
    fun `toMilliseconds with midnight time should return correct value`() {
        val dateTime = LocalDateTime.of(2023, 9, 22, 0, 0, 0, 0)

        val result = dateTime.toMilliseconds(timeZone)

        assertThat(result).isEqualTo(1695333600000)
    }
}
