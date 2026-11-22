package com.raxdenstudios.commons.time.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.time.ext.toLocalDate
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

internal class StringExtensionTest {

    @Test
    fun `Given a simple date and several patterns, When toLocalDate is called, Then return a valid LocalDate`() {
        val aValidDate = "01/01/2000"
        val aSortOfPatterns = listOf(
            "dd/MM/yyyy",
            "dd-MM-yyyy",
        ).toTypedArray()

        val result = aValidDate.toLocalDate(*aSortOfPatterns)

        assertThat(result).isEqualTo(LocalDate.of(2000, 1, 1))
    }

    @Test
    fun `Given a simple date and several patterns, When toLocalDate finds no valid pattern, Then return null`() {
        val aValidDate = "01/01/2000"
        val aSortOfPatterns = listOf(
            "dd--MM--yyyy",
            "dd-MM-yyyy",
        ).toTypedArray()

        val result = aValidDate.toLocalDate(*aSortOfPatterns)

        assertThat(result).isNull()
    }

    @Test
    fun `Given a date string and formatter, When toLocalDate is called with valid formatter, Then return LocalDate`() {
        val dateString = "2023-12-25"
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        val result = dateString.toLocalDate(formatter)

        assertThat(result).isEqualTo(LocalDate.of(2023, 12, 25))
    }

    @Test
    fun `Given a date string and multiple formatters, When toLocalDate is called, Then return LocalDate`() {
        val dateString = "25/12/2023"
        val formatters = arrayOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),
        )

        val result = dateString.toLocalDate(*formatters)

        assertThat(result).isEqualTo(LocalDate.of(2023, 12, 25))
    }

    @Test
    fun `Given a date string and formatters, When toLocalDate finds no matching formatter, Then return null`() {
        val dateString = "25/12/2023"
        val formatters = arrayOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),
        )

        val result = dateString.toLocalDate(*formatters)

        assertThat(result).isNull()
    }

    @Test
    fun `Given empty string, When toLocalDate is called with patterns, Then return null`() {
        val emptyString = ""
        val patterns = arrayOf("dd/MM/yyyy")

        val result = emptyString.toLocalDate(*patterns)

        assertThat(result).isNull()
    }

    @Test
    fun `Given invalid date string, When toLocalDate is called, Then return null`() {
        val invalidDate = "not-a-date"
        val patterns = arrayOf("dd/MM/yyyy", "yyyy-MM-dd")

        val result = invalidDate.toLocalDate(*patterns)

        assertThat(result).isNull()
    }

    @Test
    fun `Given date with different format, When toLocalDate is called with patterns, Then return LocalDate`() {
        val dateString = "2023-09-22"
        val patterns = arrayOf(
            "dd/MM/yyyy",
            "yyyy-MM-dd",
            "MM-dd-yyyy",
        )

        val result = dateString.toLocalDate(*patterns)

        assertThat(result).isEqualTo(LocalDate.of(2023, 9, 22))
    }

    @Test
    fun `Given an impossible date, When toLocalDate is called, Then return null`() {
        assertThat("31/02/2024".toLocalDate("dd/MM/yyyy")).isNull()
    }

    @Test
    fun `Given a leap day, When toLocalDate is called, Then return the date`() {
        assertThat("29/02/2024".toLocalDate("dd/MM/yyyy")).isEqualTo(LocalDate.of(2024, 2, 29))
    }

    @Test
    fun `Given a leap day in a non leap year, When toLocalDate is called, Then return null`() {
        assertThat("29/02/2023".toLocalDate("dd/MM/yyyy")).isNull()
    }

    @Test
    fun `Given an invalid then a valid pattern, When toLocalDate is called, Then return the date`() {
        val result = "01/01/2000".toLocalDate("dd/MM/yyyy'", "dd/MM/yyyy")

        assertThat(result).isEqualTo(LocalDate.of(2000, 1, 1))
    }

    @Test
    fun `Given only invalid patterns, When toLocalDate is called, Then return null`() {
        assertThat("01/01/2000".toLocalDate("dd/MM/yyyy'", "bbbbbbbbbbbb")).isNull()
    }

    @Test
    fun `Given a pattern with era, When toLocalDate is called, Then parse the date`() {
        val default = Locale.getDefault(Locale.Category.FORMAT)
        Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH)
        try {
            val result = "01/01/2000 AD".toLocalDate("dd/MM/yyyy G")

            assertThat(result).isEqualTo(LocalDate.of(2000, 1, 1))
        } finally {
            Locale.setDefault(Locale.Category.FORMAT, default)
        }
    }

    @Test
    fun `Given a pattern with a quoted y literal, When toLocalDate is called, Then parse the date`() {
        val result = "y01/01/2000".toLocalDate("'y'dd/MM/yyyy")

        assertThat(result).isEqualTo(LocalDate.of(2000, 1, 1))
    }
}
