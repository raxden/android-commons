package com.raxdenstudios.commons.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.threetenabp.ext.toLocalDate
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

internal class StringExtensionTest {

    @Test
    fun `Given a simple date and a sort of possible patterns, When toLocalDate is called, Then return a valid LocalDate`() {
        val aValidDate = "01/01/2000"
        val aSortOfPatterns = listOf(
            "dd/MM/yyyy",
            "dd-MM-yyyy"
        ).toTypedArray()

        val result = aValidDate.toLocalDate(*aSortOfPatterns)

        assertThat(result).isEqualTo(LocalDate.of(2000, 1, 1))
    }

    @Test
    fun `Given a simple date and a sort of possible patterns, When toLocalDate is called and not found any valid patterm, Then return null`() {
        val aValidDate = "01/01/2000"
        val aSortOfPatterns = listOf(
            "dd--MM--yyyy",
            "dd-MM-yyyy"
        ).toTypedArray()

        val result = aValidDate.toLocalDate(*aSortOfPatterns)

        assertThat(result).isNull()
    }

    @Test
    fun `Given a date string and DateTimeFormatter, When toLocalDate is called with valid formatter, Then return LocalDate`() {
        val dateString = "2023-12-25"
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        val result = dateString.toLocalDate(formatter)

        assertThat(result).isEqualTo(LocalDate.of(2023, 12, 25))
    }

    @Test
    fun `Given a date string and multiple DateTimeFormatters, When toLocalDate is called, Then return LocalDate with first matching formatter`() {
        val dateString = "25/12/2023"
        val formatters = arrayOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy")
        )

        val result = dateString.toLocalDate(*formatters)

        assertThat(result).isEqualTo(LocalDate.of(2023, 12, 25))
    }

    @Test
    fun `Given a date string and DateTimeFormatters, When toLocalDate is called with no matching formatter, Then return null`() {
        val dateString = "25/12/2023"
        val formatters = arrayOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy")
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
    fun `Given date with different format, When toLocalDate is called with multiple patterns, Then return LocalDate with correct pattern`() {
        val dateString = "2023-09-22"
        val patterns = arrayOf(
            "dd/MM/yyyy",
            "yyyy-MM-dd",
            "MM-dd-yyyy"
        )

        val result = dateString.toLocalDate(*patterns)

        assertThat(result).isEqualTo(LocalDate.of(2023, 9, 22))
    }
}
