package com.raxdenstudios.commons.core.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.core.Answer
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class AnswerExtensionTest {

    @Before
    fun setUp() {
        mockkStatic("com.raxdenstudios.commons.core.ext.AnswerExtensionKt")
    }

    @After
    fun after() {
        unmockkStatic("com.raxdenstudios.commons.core.ext.AnswerExtensionKt")
    }

    @Test
    fun `success runCatching`() {
        val result = runCatching { 1 }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success(1))
    }

    @Test
    fun `failure runCatching`() {
        val result = runCatching { error("error") }

        assertTrue(result.isFailure)
        assertThat(result).isInstanceOf(Answer.Failure::class.java)
    }


    @Test
    fun `use map when result is success`() {
        val result = Answer.Success("originalValue")
            .then { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("otherValue"))
    }

    @Test
    fun `use map when result is failure`() {
        val result = Answer.Failure("originalValue")
            .then { "otherValue" }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("originalValue"))
    }

    @Test
    fun `use mapFailure when result is success`() {
        val result = Answer.Success("originalValue")
            .thenFailure { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("originalValue"))
    }

    @Test
    fun `use mapFailure when result is failure`() {
        val result = Answer.Failure("originalValue")
            .thenFailure { "otherValue" }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("otherValue"))
    }

    @Test
    fun `use flatMap when result is success`() {
        val result = Answer.Success("originalValue")
            .flatMap { Answer.Success("otherValue") }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("otherValue"))
    }

    @Test
    fun `use flatMap when result is failure`() {
        val result = Answer.Failure("originalValue")
            .flatMap { Answer.Failure("otherValue") }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("originalValue"))
    }

    @Test
    fun `use flatMapFailure when result is success`() {
        val result = Answer.Success("originalValue")
            .flatMapFailure { Answer.Success("otherValue") }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("originalValue"))
    }

    @Test
    fun `use flatMapFailure when result is failure`() {
        val result = Answer.Failure("originalValue")
            .flatMapFailure { Answer.Failure("otherValue") }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("otherValue"))
    }

    @Test
    fun `use getValueOrNull when result is success`() {
        val answer = Answer.Success("randomValue")

        val result = answer.getValueOrNull()

        assertThat(result).isEqualTo("randomValue")
    }

    @Test
    fun `use getValueOrNull when result is failure`() {
        val answer = Answer.Failure("error")

        val result = answer.getValueOrNull<String, String>()

        assertThat(result).isNull()
    }

    @Test
    fun `use getValueOrDefault when result is success`() {
        val answer = Answer.Success("randomValue")

        val result = answer.getValueOrDefault("defaultValue")

        assertThat(result).isEqualTo("randomValue")
    }

    @Test
    fun `use getValueOrDefault when result is failure`() {
        val answer = Answer.Failure("error")

        val result = answer.getValueOrDefault("defaultValue")

        assertThat(result).isEqualTo("defaultValue")
    }

    @Test
    fun `use fold when result is success`() {
        val result = Answer.Success("originalValue")
            .then { "otherValue" }

        result.fold(
            onSuccess = { data ->
                assertThat(data).isEqualTo("otherValue")
            },
            onFailure = { }
        )
    }

    @Test
    fun `use fold when failure`() {
        val result = Answer.Failure("originalValue")
            .then { "otherValue" }

        result.fold(
            onSuccess = { },
            onFailure = { data ->
                assertThat(data).isEqualTo("originalValue")
            }
        )
    }

    @Test
    fun `use onSuccess when result is success`() {
        val answer = Answer.Success("originalValue")

        val result = answer.onSuccess { data ->
            assertThat(data).isEqualTo("originalValue")
        }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("originalValue"))
    }

    @Test
    fun `use onSuccess when result is failure`() {
        val answer = Answer.Failure("originalValue")

        val result = answer.onSuccess { }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("originalValue"))
    }


    @Test
    fun `use onFailure when result is success`() {
        val answer = Answer.Success("originalValue")

        val result = answer.onFailure { }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("originalValue"))
    }

    @Test
    fun `use onFailure when result is failure`() {
        val answer = Answer.Failure("originalValue")

        val result = answer.onFailure { data ->
            assertThat(data).isEqualTo("originalValue")
        }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("originalValue"))
    }

}
