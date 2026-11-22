package com.raxdenstudios.commons.core.ext

import org.junit.Assert.assertEquals
import org.junit.Test

internal class NumberExtensionTest {

    @Test
    fun `Given a Double null, When orDefault is called Then return default value`() {
        val nullValue: Double? = null

        assertEquals(Double.ZERO, Double.ZERO, nullValue.orDefault())
        assertEquals(1.0, 1.0, nullValue.orDefault(1.0))
        assertEquals(1.0, 1.0, 1.0.orDefault(2.0))
    }

    @Test
    fun `Given a Int null, When orDefault is called Then return default value`() {
        val nullValue: Int? = null

        assertEquals(Int.ZERO, nullValue.orDefault())
        assertEquals(1, nullValue.orDefault(1))
        assertEquals(1, 1.orDefault(2))
    }
}
