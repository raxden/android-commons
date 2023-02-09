package com.raxdenstudios.commons.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.ResultData
import org.junit.Test

internal class ResultDataExtensionTest {

    @Test
    fun `success runCatching`() {
        val resultData = runCatching { 1 }
        assertThat(resultData).isEqualTo(ResultData.Success(1))
    }

    @Test
    fun `failure runCatching`() {
        val resultData = runCatching { error("error") }
        assertThat(resultData).isInstanceOf(ResultData.Failure::class.java)
    }

    @Test
    fun `use map`() {
        val resultData = ResultData.Success("randomValue")
        val resultData2 = resultData.map { it.length }
        assertThat(resultData2).isEqualTo(ResultData.Success(11))
    }

    @Test
    fun `use mapFailure`() {
        val resultData = ResultData.Failure("randomValue")
        val resultData2 = resultData.mapFailure { it.length }
        assertThat(resultData2).isEqualTo(ResultData.Failure(11))
    }

    @Test
    fun `use flatMap`() {
        val resultData = ResultData.Success("randomValue")
        val resultData2 = resultData.flatMap { ResultData.Failure(it.length) }
        assertThat(resultData2).isEqualTo(ResultData.Failure(11))
    }

    @Test
    fun `use flatMapFailure`() {
        val resultData = ResultData.Failure("randomValue")
        val resultData2 = resultData.flatMapFailure { ResultData.Success(it.length) }
        assertThat(resultData2).isEqualTo(ResultData.Success(11))
    }

    @Test
    fun `use getValueOrNull`() {
        val resultData = ResultData.Success("randomValue")
        val result = resultData.getValueOrNull()
        assertThat(result).isEqualTo("randomValue")
    }

    @Test
    fun `use getValueOrNull when error`() {
        val resultData = ResultData.Failure(IllegalStateException("error"))
        val result = resultData.getValueOrNull<String, Throwable>()
        assertThat(result).isNull()
    }

    @Test
    fun `use getValueOrDefault`() {
        val resultData = ResultData.Success("randomValue")
        val result = resultData.getValueOrDefault("defaultValue")
        assertThat(result).isEqualTo("randomValue")
    }

    @Test
    fun `use getValueOrDefault when error`() {
        val resultData = ResultData.Failure(IllegalStateException("error"))
        val result = resultData.getValueOrDefault("defaultValue")
        assertThat(result).isEqualTo("defaultValue")
    }
}
