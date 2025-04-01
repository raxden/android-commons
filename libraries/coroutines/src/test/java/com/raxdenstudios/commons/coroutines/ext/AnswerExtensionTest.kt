package com.raxdenstudios.commons.coroutines.ext

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.core.Answer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
internal class AnswerExtensionTest {

    @Test
    fun `success coRunCatching`() = runTest {
        val result = coRunCatching { 1 }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success(1))
    }

    @Test
    fun `failure coRunCatching`() = runTest {
        val result = coRunCatching { error("error") }

        assertTrue(result.isFailure)
        assertThat(result).isInstanceOf(Answer.Failure::class.java)
    }

    @Test
    fun `use coMap when result is success`() = runTest {
        val result = Answer.Success("originalValue")
            .coThen { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("otherValue"))
    }

    @Test
    fun `use coMap when result is failure`() = runTest {
        val result = Answer.Failure("originalValue")
            .coThen { "otherValue" }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("originalValue"))
    }

    @Test
    fun `use coMapFailure when result is success`() = runTest {
        val result = Answer.Success("originalValue")
            .coThenFailure { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("originalValue"))
    }

    @Test
    fun `use coMapFailure when result is failure`() = runTest {
        val result = Answer.Failure("originalValue")
            .coThenFailure { "otherValue" }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("otherValue"))
    }

    @Test
    fun `use coFlatMap when result is success`() = runTest {
        val result = Answer.Success("originalValue")
            .coFlatMap { Answer.Success("otherValue") }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("otherValue"))
    }

    @Test
    fun `use coFlatMap when result is failure`() = runTest {
        val result = Answer.Failure("originalValue")
            .coFlatMap { Answer.Failure("otherValue") }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("originalValue"))
    }

    @Test
    fun `use coFlatMapFailure when result is success`() = runTest {
        val result = Answer.Success("originalValue")
            .coFlatMapFailure { Answer.Success("otherValue") }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("originalValue"))
    }

    @Test
    fun `use coFlatMapFailure when result is failure`() = runTest {
        val result = Answer.Failure("originalValue")
            .coFlatMapFailure { Answer.Failure("otherValue") }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("otherValue"))
    }

    @Test
    fun `use onCoSuccess when result is success`() = runTest {
        val answer = Answer.Success("originalValue")

        val result = answer.onCoSuccess { data ->
            assertThat(data).isEqualTo("originalValue")
        }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("originalValue"))
    }

    @Test
    fun `use onCoSuccess when result is failure`() = runTest {
        val answer = Answer.Failure("originalValue")

        val result = answer.onCoSuccess { }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("originalValue"))
    }

    @Test
    fun `use onCoFailure when result is success`() = runTest {
        val answer = Answer.Success("originalValue")

        val result = answer.onCoFailure { }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(Answer.Success("originalValue"))
    }

    @Test
    fun `use onCoFailure when result is failure`() = runTest {
        val answer = Answer.Failure("originalValue")

        val result = answer.onCoFailure { data ->
            assertThat(data).isEqualTo("originalValue")
        }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(Answer.Failure("originalValue"))
    }

    @Test
    fun `use then when result is success`() = runTest {
        val flowData = flowOf(Answer.Success("originalValue"))

        flowData.then { "otherValue" }.test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result).isEqualTo(Answer.Success("otherValue"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `use then when result is failure`() = runTest {
        val flowData = flowOf(Answer.Failure("originalValue"))

        flowData.then { "otherValue" }.test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result).isEqualTo(Answer.Failure("originalValue"))
            cancelAndIgnoreRemainingEvents()
        }
    }
}
