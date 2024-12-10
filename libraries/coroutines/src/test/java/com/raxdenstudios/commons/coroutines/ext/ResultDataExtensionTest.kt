package com.raxdenstudios.commons.coroutines.ext

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.core.ResultData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
internal class ResultDataExtensionTest {

    @Test
    fun `success coRunCatching`() = runTest {
        val result = coRunCatching { 1 }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success(1))
    }

    @Test
    fun `failure coRunCatching`() = runTest {
        val result = coRunCatching { error("error") }

        assertTrue(result.isFailure)
        assertThat(result).isInstanceOf(ResultData.Failure::class.java)
    }

    @Test
    fun `use coMap when result is success`() = runTest {
        val result = ResultData.Success("originalValue")
            .coThen { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("otherValue"))
    }

    @Test
    fun `use coMap when result is failure`() = runTest {
        val result = ResultData.Failure("originalValue")
            .coThen { "otherValue" }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("originalValue"))
    }

    @Test
    fun `use coMapFailure when result is success`() = runTest {
        val result = ResultData.Success("originalValue")
            .coThenFailure { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("originalValue"))
    }

    @Test
    fun `use coMapFailure when result is failure`() = runTest {
        val result = ResultData.Failure("originalValue")
            .coThenFailure { "otherValue" }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("otherValue"))
    }

    @Test
    fun `use coFlatMap when result is success`() = runTest {
        val result = ResultData.Success("originalValue")
            .coFlatMap { ResultData.Success("otherValue") }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("otherValue"))
    }

    @Test
    fun `use coFlatMap when result is failure`() = runTest {
        val result = ResultData.Failure("originalValue")
            .coFlatMap { ResultData.Failure("otherValue") }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("originalValue"))
    }

    @Test
    fun `use coFlatMapFailure when result is success`() = runTest {
        val result = ResultData.Success("originalValue")
            .coFlatMapFailure { ResultData.Success("otherValue") }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("originalValue"))
    }

    @Test
    fun `use coFlatMapFailure when result is failure`() = runTest {
        val result = ResultData.Failure("originalValue")
            .coFlatMapFailure { ResultData.Failure("otherValue") }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("otherValue"))
    }

    @Test
    fun `use onCoSuccess when result is success`() = runTest {
        val resultData = ResultData.Success("originalValue")

        val result = resultData.onCoSuccess { data ->
            assertThat(data).isEqualTo("originalValue")
        }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("originalValue"))
    }

    @Test
    fun `use onCoSuccess when result is failure`() = runTest {
        val resultData = ResultData.Failure("originalValue")

        val result = resultData.onCoSuccess { }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("originalValue"))
    }

    @Test
    fun `use onCoFailure when result is success`() = runTest {
        val resultData = ResultData.Success("originalValue")

        val result = resultData.onCoFailure { }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("originalValue"))
    }

    @Test
    fun `use onCoFailure when result is failure`() = runTest {
        val resultData = ResultData.Failure("originalValue")

        val result = resultData.onCoFailure { data ->
            assertThat(data).isEqualTo("originalValue")
        }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("originalValue"))
    }

    @Test
    fun `use then when result is success`() = runTest {
        val flowData = flowOf(ResultData.Success("originalValue"))

        flowData.then { "otherValue" }.test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result).isEqualTo(ResultData.Success("otherValue"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `use then when result is failure`() = runTest {
        val flowData = flowOf(ResultData.Failure("originalValue"))

        flowData.then { "otherValue" }.test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result).isEqualTo(ResultData.Failure("originalValue"))
            cancelAndIgnoreRemainingEvents()
        }
    }
}
