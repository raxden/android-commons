package com.raxdenstudios.commons.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.ResultData
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class ResultDataExtensionTest {

    @Before
    fun setUp() {
        mockkStatic("com.raxdenstudios.commons.ext.ResultDataExtensionKt")
    }

    @After
    fun after() {
        unmockkStatic("com.raxdenstudios.commons.ext.ResultDataExtensionKt")
    }

    @Test
    fun `success runCatching`() {
        val result = runCatching { 1 }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success(1))
    }

    @Test
    fun `failure runCatching`() {
        val result = runCatching { error("error") }

        assertTrue(result.isFailure)
        assertThat(result).isInstanceOf(ResultData.Failure::class.java)
    }

    @Test
    fun `use map when result is success`() {
        val result = ResultData.Success("originalValue")
            .map { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("otherValue"))
    }

    @Test
    fun `use map when result is failure`() {
        val result = ResultData.Failure("originalValue")
            .map { "otherValue" }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("originalValue"))
    }

    @Test
    fun `use coMap when result is success`() = runTest {
        val result = ResultData.Success("originalValue")
            .coMap { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("otherValue"))
    }

    @Test
    fun `use coMap when result is failure`() = runTest {
        val result = ResultData.Failure("originalValue")
            .coMap { "otherValue" }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("originalValue"))
    }

    @Test
    fun `use mapFailure when result is success`() {
        val result = ResultData.Success("originalValue")
            .mapFailure { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("originalValue"))
    }

    @Test
    fun `use mapFailure when result is failure`() {
        val result = ResultData.Failure("originalValue")
            .mapFailure { "otherValue" }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("otherValue"))
    }

    @Test
    fun `use coMapFailure when result is success`() = runTest {
        val result = ResultData.Success("originalValue")
            .coMapFailure { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("originalValue"))
    }

    @Test
    fun `use coMapFailure when result is failure`() = runTest {
        val result = ResultData.Failure("originalValue")
            .coMapFailure { "otherValue" }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("otherValue"))
    }

    @Test
    fun `use flatMap when result is success`() {
        val result = ResultData.Success("originalValue")
            .flatMap { ResultData.Success("otherValue") }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("otherValue"))
    }

    @Test
    fun `use flatMap when result is failure`() {
        val result = ResultData.Failure("originalValue")
            .flatMap { ResultData.Failure("otherValue") }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("originalValue"))
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
    fun `use flatMapFailure when result is success`() {
        val result = ResultData.Success("originalValue")
            .flatMapFailure { ResultData.Success("otherValue") }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("originalValue"))
    }

    @Test
    fun `use flatMapFailure when result is failure`() {
        val result = ResultData.Failure("originalValue")
            .flatMapFailure { ResultData.Failure("otherValue") }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("otherValue"))
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
    fun `use getValueOrNull when result is success`() {
        val resultData = ResultData.Success("randomValue")

        val result = resultData.getValueOrNull()

        assertThat(result).isEqualTo("randomValue")
    }

    @Test
    fun `use getValueOrNull when result is failure`() {
        val resultData = ResultData.Failure("error")

        val result = resultData.getValueOrNull<String, String>()

        assertThat(result).isNull()
    }

    @Test
    fun `use getValueOrDefault when result is success`() {
        val resultData = ResultData.Success("randomValue")

        val result = resultData.getValueOrDefault("defaultValue")

        assertThat(result).isEqualTo("randomValue")
    }

    @Test
    fun `use getValueOrDefault when result is failure`() {
        val resultData = ResultData.Failure("error")

        val result = resultData.getValueOrDefault("defaultValue")

        assertThat(result).isEqualTo("defaultValue")
    }

    @Test
    fun `use fold when result is success`() {
        val result = ResultData.Success("originalValue")
            .map { "otherValue" }

        result.fold(
            onSuccess = { data ->
                assertThat(data).isEqualTo("otherValue")
            },
            onFailure = { }
        )
    }

    @Test
    fun `use fold when failure`() {
        val result = ResultData.Failure("originalValue")
            .map { "otherValue" }

        result.fold(
            onSuccess = { },
            onFailure = { data ->
                assertThat(data).isEqualTo("originalValue")
            }
        )
    }

    @Test
    fun `use onSuccess when result is success`() {
        val resultData = ResultData.Success("originalValue")

        val result = resultData.onSuccess { data ->
            assertThat(data).isEqualTo("originalValue")
        }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("originalValue"))
    }

    @Test
    fun `use onSuccess when result is failure`() {
        val resultData = ResultData.Failure("originalValue")

        val result = resultData.onSuccess { }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("originalValue"))
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
    fun `use onFailure when result is success`() {
        val resultData = ResultData.Success("originalValue")

        val result = resultData.onFailure { }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("originalValue"))
    }

    @Test
    fun `use onFailure when result is failure`() {
        val resultData = ResultData.Failure("originalValue")

        val result = resultData.onFailure { data ->
            assertThat(data).isEqualTo("originalValue")
        }

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
}
