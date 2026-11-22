package com.raxdenstudios.commons.pagination.model

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

internal class PageSizeTest {

    @Test
    fun `Given a positive value, When PageSize is created, Then it keeps the value`() {
        assertThat(PageSize(1).value).isEqualTo(1)
    }

    @Test
    fun `Given zero, When PageSize is created, Then throw IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) { PageSize(0) }
    }

    @Test
    fun `Given a negative value, When PageSize is created, Then throw IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) { PageSize(-1) }
    }
}
