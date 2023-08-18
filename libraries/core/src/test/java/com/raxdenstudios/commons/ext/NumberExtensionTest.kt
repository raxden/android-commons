package com.raxdenstudios.commons.ext

import org.junit.Assert.assertEquals
import org.junit.Test

internal class NumberExtensionTest {

    @Test
    fun `Given a Byte null, When orDefault is called Then return default value`() {
        val nullValue: Byte? = null

        assertEquals(Byte.ZERO, nullValue.orDefault())
        assertEquals(1.toByte(), nullValue.orDefault(1.toByte()))
    }

    @Test
    fun `Given a Double null, When orDefault is called Then return default value`() {
        val nullValue: Double? = null

        assertEquals(Double.ZERO, Double.ZERO, nullValue.orDefault())
        assertEquals(1.0, 1.0, nullValue.orDefault(1.0))
    }

    @Test
    fun `Given a Float null, When orDefault is called Then return default value`() {
        val nullValue: Float? = null

        assertEquals(Float.ZERO, nullValue.orDefault())
        assertEquals(1.0f, nullValue.orDefault(1.0f))
    }

    @Test
    fun `Given a Int null, When orDefault is called Then return default value`() {
        val nullValue: Int? = null

        assertEquals(Int.ZERO, nullValue.orDefault())
        assertEquals(1, nullValue.orDefault(1))
    }

    @Test
    fun `Given a Long null, When orDefault is called Then return default value`() {
        val nullValue: Long? = null

        assertEquals(Long.ZERO, nullValue.orDefault())
        assertEquals(1, nullValue.orDefault(1))
    }

    @Test
    fun `Given a Short null, When orDefault is called Then return default value`() {
        val nullValue: Short? = null

        assertEquals(Short.ZERO, nullValue.orDefault())
        assertEquals(1.toShort(), nullValue.orDefault(1.toShort()))
    }
}
