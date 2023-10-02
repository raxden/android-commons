package com.raxdenstudios.commons.core.ext

import com.raxdenstudios.commons.core.ext.EMPTY
import com.raxdenstudios.commons.core.ext.ifEmptyOrNullThen
import com.raxdenstudios.commons.core.ext.ifEmptyThen
import com.raxdenstudios.commons.core.ext.orDefault
import com.raxdenstudios.commons.core.ext.toMD5
import com.raxdenstudios.commons.core.ext.toSHA256
import com.raxdenstudios.commons.core.ext.toSHA512
import org.junit.Assert.assertEquals
import org.junit.Test

internal class StringExtensionTest {

    @Test
    fun `null orDefault should return default value`() {
        val nullValue: String? = null

        assertEquals(String.EMPTY, nullValue.orDefault())
        assertEquals("empty", nullValue.orDefault("empty"))
        assertEquals("empty", "empty".orDefault("empty2"))
    }

    @Test
    fun `ifEmptyThen should return default value`() {
        val emptyValue: String = String.EMPTY

        assertEquals("default", emptyValue.ifEmptyThen("default"))
    }

    @Test
    fun `ifEmptyOrNullThen should return default value, When value is null`() {
        val emptyValue: String? = null

        assertEquals("default", emptyValue.ifEmptyOrNullThen("default"))
    }

    @Test
    fun `ifEmptyOrNullThen should return default value, When value is empty`() {
        val emptyValue: String = String.EMPTY

        assertEquals("default", emptyValue.ifEmptyOrNullThen("default"))
    }

    @Test
    fun `ifEmptyOrNullThen should return value, When value is not empty`() {
        val notEmptyValue = "notEmptyValue"

        assertEquals(notEmptyValue, notEmptyValue.ifEmptyOrNullThen("default"))
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
    fun `toSHA256 with UTF_16 should return value`() {
        val value = "Generando un SHA256 de un texto"

        assertEquals(
            "018e20b7fb568de01403bec144096b34a6bda4c581127bb3298765b2f6fbfa92",
            value.toSHA256(Charsets.UTF_16)
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
