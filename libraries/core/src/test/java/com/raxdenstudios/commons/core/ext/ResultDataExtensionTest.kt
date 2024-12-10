package com.raxdenstudios.commons.core.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.core.ResultData
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class ResultDataExtensionTest {

    @Before
    fun setUp() {
        mockkStatic("com.raxdenstudios.commons.core.ext.ResultDataExtensionKt")
    }

    @After
    fun after() {
        unmockkStatic("com.raxdenstudios.commons.core.ext.ResultDataExtensionKt")
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
            .then { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("otherValue"))
    }

    @Test
    fun `use map when result is failure`() {
        val result = ResultData.Failure("originalValue")
            .then { "otherValue" }

        assertTrue(result.isFailure)
        assertThat(result).isEqualTo(ResultData.Failure("originalValue"))
    }

    @Test
    fun `use mapFailure when result is success`() {
        val result = ResultData.Success("originalValue")
            .thenFailure { "otherValue" }

        assertTrue(result.isSuccess)
        assertThat(result).isEqualTo(ResultData.Success("originalValue"))
    }

    @Test
    fun `use mapFailure when result is failure`() {
        val result = ResultData.Failure("originalValue")
            .thenFailure { "otherValue" }

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
        val result = ResultData.Failure("originalValue")
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

}
