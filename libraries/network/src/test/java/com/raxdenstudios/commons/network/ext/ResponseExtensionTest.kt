package com.raxdenstudios.commons.network.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.core.Answer
import com.raxdenstudios.commons.core.NetworkError
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import retrofit2.Response

internal class ResponseExtensionTest {

    @Test
    fun `toAnswer should be return a success result data`() {
        val result: Answer<String, NetworkError<String>> =
            mockResponse200.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(Answer.Success("originalValue"))
    }

    @Test
    fun `Given a client error with code 4xx, When toResult is called, Then a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetwork400.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Client(code = 400, body = "", message = "message")),
        )
    }

    @Test
    fun `Given a server error with code 5xx, When toResult is called, Then a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetwork500.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Server(code = 500, body = "", message = "message")),
        )
    }

    @Test
    fun `Given a server error with a unknown code, When toResult is called, Then a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetworkWithoutCode.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Unknown(code = -1, body = "", message = "message")),
        )
    }

    @Test
    fun `Given a Unit response 204 without body, When toAnswer is called, Then return Success Unit`() {
        val result = unitResponse(code = 204).toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(Answer.Success(Unit))
    }

    @Test
    fun `Given a Unit response 205 without body, When toAnswer is called, Then return Success Unit`() {
        val result = unitResponse(code = 205).toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(Answer.Success(Unit))
    }

    @Test
    fun `Given a Unit response 200 without body, When toAnswer is called, Then return Failure Unknown`() {
        val result = unitResponse(code = 200).toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Unknown(code = 200, body = null, message = "Success response with null body")),
        )
    }

    @Test
    fun `Given a String response 204 without body, When toAnswer is called, Then return Failure Unknown`() {
        val response = mockk<Response<String>> {
            every { body() } returns null
            every { code() } returns 204
        }

        val result = response.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Unknown(code = 204, body = null, message = "Success response with null body")),
        )
    }

    @Test
    fun `Given a Unit response 404, When toAnswer is called, Then return Failure Client`() {
        val result = unitResponse(code = 404).toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Client(code = 404, body = null, message = "message")),
        )
    }

    private fun unitResponse(
        code: Int,
    ): Response<Unit> = mockk {
        every { body() } returns null
        every { code() } returns code
    }

    companion object {

        private val mockResponse200 = mockk<Response<String>> {
            every { body() } returns "originalValue"
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
