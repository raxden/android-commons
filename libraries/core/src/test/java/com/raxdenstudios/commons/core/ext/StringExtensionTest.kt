package com.raxdenstudios.commons.core.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

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
    fun `Given a string with US decimal format, When toDouble is called with US locale, Then return correct Double`() {
        val value = "123.45"

        val result = value.toDouble(Locale.US)

        assertThat(result).isEqualTo(123.45)
    }

    @Test
    fun `Given a string with German decimal format, When toDouble is called with German locale, Then return correct Double`() {
        val value = "123,45"

        val result = value.toDouble(Locale.GERMANY)

        assertThat(result).isEqualTo(123.45)
    }

    @Test
    fun `Given a string with surrounding whitespace, When toDouble is called, Then trim and return correct Double`() {
        val value = "  123.45  "

        val result = value.toDouble(Locale.US)

        assertThat(result).isEqualTo(123.45)
    }

    @Test
    fun `Given an invalid string, When toDouble is called, Then return 0_0`() {
        val value = "not-a-number"

        val result = value.toDouble(Locale.US)

        assertThat(result).isEqualTo(0.0)
    }

    @Test
    fun `Given an empty string, When toDouble is called, Then return 0_0`() {
        val value = ""

        val result = value.toDouble(Locale.US)

        assertThat(result).isEqualTo(0.0)
    }

    @Test
    fun `Given a string in scientific notation, When toDouble is called, Then fallback to toDoubleOrNull and return correct Double`() {
        val value = "1.5e2"

        val result = value.toDouble(Locale.US)

        assertThat(result).isEqualTo(1.5)
    }
}
