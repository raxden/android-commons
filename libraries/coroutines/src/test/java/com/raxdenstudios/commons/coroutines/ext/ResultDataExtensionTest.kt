package com.raxdenstudios.commons.coroutines.ext

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.core.ResultData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
internal class ResultDataExtensionTest {

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
