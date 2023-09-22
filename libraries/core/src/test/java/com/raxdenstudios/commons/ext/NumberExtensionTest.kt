package com.raxdenstudios.commons.ext

import org.junit.Assert.assertEquals
import org.junit.Test

internal class NumberExtensionTest {

    @Test
    fun `Given a Byte null, When orDefault is called Then return default value`() {
        val nullValue: Byte? = null

        assertEquals(Byte.ZERO, nullValue.orDefault())
        assertEquals(1.toByte(), nullValue.orDefault(1.toByte()))
        assertEquals(1.toByte(), 1.toByte().orDefault(2.toByte()))
    }

    @Test
    fun `Given a Double null, When orDefault is called Then return default value`() {
        val nullValue: Double? = null

        assertEquals(Double.ZERO, Double.ZERO, nullValue.orDefault())
        assertEquals(1.0, 1.0, nullValue.orDefault(1.0))
        assertEquals(1.0, 1.0, 1.0.orDefault(2.0))
    }

    @Test
    fun `Given a Float null, When orDefault is called Then return default value`() {
        val nullValue: Float? = null

        assertEquals(Float.ZERO, nullValue.orDefault())
        assertEquals(1.0f, nullValue.orDefault(1.0f))
        assertEquals(1.0f, 1.0f.orDefault(2.0f))
    }

    @Test
    fun `Given a Int null, When orDefault is called Then return default value`() {
        val nullValue: Int? = null

        assertEquals(Int.ZERO, nullValue.orDefault())
        assertEquals(1, nullValue.orDefault(1))
        assertEquals(1, 1.orDefault(2))
    }

    @Test
    fun `Given a Long null, When orDefault is called Then return default value`() {
        val nullValue: Long? = null

        assertEquals(Long.ZERO, nullValue.orDefault())
        assertEquals(1, nullValue.orDefault(1))
        assertEquals(1, 1.orDefault(2))
    }

    @Test
    fun `Given a Short null, When orDefault is called Then return default value`() {
        val nullValue: Short? = null

        assertEquals(Short.ZERO, nullValue.orDefault())
        assertEquals(1.toShort(), nullValue.orDefault(1.toShort()))
        assertEquals(1.toShort(), 1.toShort().orDefault(2.toShort()))
    }
}
