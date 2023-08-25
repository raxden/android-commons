package com.raxdenstudios.commons.ext

import org.junit.Assert.assertEquals
import org.junit.Test

internal class StringExtensionTest {

    @Test
    fun `null orDefault should return default value`() {
        val nullValue: String? = null

        assertEquals(String.EMPTY, nullValue.orDefault())
        assertEquals("empty", nullValue.orDefault("empty"))
    }

    @Test
    fun `ifEmptyThen should return default value`() {
        val emptyValue: String = String.EMPTY

        assertEquals("default", emptyValue.ifEmptyThen("default"))
    }

    @Test
    fun `toMD5 should return value`() {
        val value = "Generando un MD5 de un texto"

        assertEquals("5df9f63916ebf8528697b629022993e8", value.toMD5())
    }

    @Test
    fun `toSHA256 should return value`() {
        val value = "Generando un SHA256 de un texto"

        assertEquals(
            "b2303cd72f71b642f9c57d2b053ecd6521608b4b2f3045cae612090c472c9eff",
            value.toSHA256()
        )
    }

    @Test
    @Suppress("MaxLineLength")
    fun `toSHA512 should return value`() {
        val value = "Generando un SHA512 de un texto"

        assertEquals(
            "98096d5bf12e56948667b96ef96561a1813b38353b8434eaf950c61603b80e14a023b66dcdeb80c484b19902cb0818fb40607a15f67b0ca4efa7f37291353c59",
            value.toSHA512()
        )
    }

    @Test
    fun `Given a random text, When toMD5 extension is called, Then random text is codified to MD5`() {
        val result = "Generando un MD5 de un texto".toMD5()

        assertEquals("5df9f63916ebf8528697b629022993e8", result)
    }
}
