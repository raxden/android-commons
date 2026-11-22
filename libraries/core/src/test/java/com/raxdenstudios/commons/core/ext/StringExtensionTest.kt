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
    fun `Given a string with German decimal format, When toDouble is called, Then return correct Double`() {
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
    fun `Given a string in scientific notation, When toDouble is called, Then fallback and return correct Double`() {
        val value = "1.5e2"

        val result = value.toDouble(Locale.US)

        assertThat(result).isEqualTo(150.0)
    }

    @Test
    fun `Given a string with diacritics, When removeDiacritics is called, Then marks are removed`() {
        assertThat("Canción".removeDiacritics()).isEqualTo("Cancion")
        assertThat("Ñandú àéîöü".removeDiacritics()).isEqualTo("Nandu aeiou")
    }

    @Test
    fun `Given a string without diacritics, When removeDiacritics is called, Then it is unchanged`() {
        assertThat("Song".removeDiacritics()).isEqualTo("Song")
    }

    @Test
    fun `Given a string with symbols, When alphanumericOnly is called, Then only letters and digits remain`() {
        assertThat("Rock & Roll 80's".alphanumericOnly()).isEqualTo("RockRoll80s")
    }

    @Test
    fun `Given a string with no alphanumerics, When alphanumericOnly is called, Then return empty`() {
        assertThat("!? -_".alphanumericOnly()).isEmpty()
    }

    @Test
    fun `Given runs of whitespace, When collapseWhitespace is called, Then runs collapse and ends are trimmed`() {
        assertThat("  a \t b\n\nc  ".collapseWhitespace()).isEqualTo("a b c")
    }

    @Test
    fun `Given a blank string, When collapseWhitespace is called, Then return empty`() {
        assertThat("   ".collapseWhitespace()).isEmpty()
    }

    @Test
    fun `Given a lowercase country code, When toFlagEmoji is called, Then return the flag`() {
        assertThat("es".toFlagEmoji()).isEqualTo("🇪🇸")
    }

    @Test
    fun `Given a padded country code, When toFlagEmoji is called, Then it is trimmed and returns the flag`() {
        assertThat(" US ".toFlagEmoji()).isEqualTo("🇺🇸")
    }

    @Test
    fun `Given an invalid country code, When toFlagEmoji is called, Then return empty`() {
        assertThat("".toFlagEmoji()).isEmpty()
        assertThat("ESP".toFlagEmoji()).isEmpty()
        assertThat("1A".toFlagEmoji()).isEmpty()
    }

    @Test
    fun `Given a string with US grouping separator, When toDouble is called, Then return correct Double`() {
        val result = "1,234.5".toDouble(Locale.US)

        assertThat(result).isEqualTo(1234.5)
    }

    @Test
    fun `Given a decimal comma string, When toDouble is called with US locale, Then return 0_0`() {
        val result = "1,5".toDouble(Locale.US)

        assertThat(result).isEqualTo(0.0)
    }

    @Test
    fun `Given a string with trailing letters, When toDouble is called, Then return 0_0`() {
        val result = "12abc".toDouble(Locale.US)

        assertThat(result).isEqualTo(0.0)
    }

    @Test
    fun `Given a German grouped string, When toDouble is called with GERMANY locale, Then return correct Double`() {
        val result = "1.234,5".toDouble(Locale.GERMANY)

        assertThat(result).isEqualTo(1234.5)
    }

    @Test
    fun `Given a US decimal string, When toDouble is called with GERMANY locale, Then return correct Double`() {
        val result = "1.5".toDouble(Locale.GERMANY)

        assertThat(result).isEqualTo(1.5)
    }
}
