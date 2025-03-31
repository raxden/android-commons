package com.raxdenstudios.commons.network.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.core.NetworkError
import com.raxdenstudios.commons.core.Answer
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import retrofit2.Response
import java.io.IOException

internal class ResponseExtensionKtText {

    @Test
    fun `toAnswer should be return a success result data`() {
        val result: Answer<String, NetworkError<String>> =
            mockResponse200.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(Answer.Success("originalValue"))
    }

    @Test
    fun `toAnswer should be return a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockResponseWithException.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Unknown(code = 200, body = null, message = "message"))
        )
    }

    @Test
    fun `Given a client error with code 4xx, When toResult is called, Then a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetwork400.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Client(code = 400, body = "", message = "message"))
        )
    }

    @Test
    fun `Given a server error with code 5xx, When toResult is called, Then a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetwork500.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Server(code = 500, body = "", message = "message"))
        )
    }

    @Test
    fun `Given a server error with a unknown code, When toResult is called, Then a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetworkWithoutCode.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Unknown(code = -1, body = "", message = "message"))
        )
    }

    companion object {

        private val mockResponse200 = mockk<Response<String>> {
            every { body() } returns "originalValue"
            every { code() } answers { 200 }
        }
        private val mockResponseWithException = mockk<Response<String>> {
            every { body() } answers { throw IOException("") }
            every { code() } answers { 200 }
        }
        private val mockNetwork400 = mockk<Response<String>> {
            every { body() } returns ""
            every { code() } answers { 400 }
        }
        private val mockNetwork500 = mockk<Response<String>> {
            every { body() } returns ""
            every { code() } answers { 500 }
        }
        private val mockNetworkWithoutCode = mockk<Response<String>> {
            every { body() } returns ""
            every { code() } answers { -1 }
        }
    }
}
