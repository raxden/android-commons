package com.raxdenstudios.commons.network.ext

import com.google.common.truth.Truth.assertThat
import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.core.NetworkError
import com.raxdenstudios.commons.core.Answer
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.io.IOException

internal class NetworkResponseExtensionKtTest {

    @Test
    fun `toAnswer should be return a success result data`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetworkResponse200.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(Answer.Success("originalValue"))
    }

    @Test
    fun `Given a client error with code 4xx, When toResult is called, Then a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetworkResponse400.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Client(code = 400, body = "", message = "message"))
        )
    }

    @Test
    fun `Given a server error with code 5xx, When toResult is called, Then a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetworkResponse500.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Server(code = 500, body = "", message = "message"))
        )
    }

    @Test
    fun `Given a server error with code null, When toResult is called, Then a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetworkResponseWithoutCode.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Unknown(code = -1, body = "", message = "message"))
        )
    }

    @Test
    fun `Given a network error, When toResult is called, Then a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetworkResponseError.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Network(body = null, message = "message"))
        )
    }

    @Test
    fun `Given a unknown error, When toResult is called, Then a failure`() {
        val result: Answer<String, NetworkError<String>> =
            mockNetworkResponseUnknownError.toAnswer(errorMessage = "message")

        assertThat(result).isEqualTo(
            Answer.Failure(NetworkError.Unknown(code = -1, body = "", message = "message"))
        )
    }

    companion object {

        private val mockNetworkResponse200 = mockk<NetworkResponse.Success<String, String>> {
            every { body } returns "originalValue"
            every { code } returns 200
        }
        private val mockNetworkResponse400 = mockk<NetworkResponse.ServerError<String, String>> {
            every { body } returns ""
            every { code } returns 400
        }
        private val mockNetworkResponse500 = mockk<NetworkResponse.ServerError<String, String>> {
            every { body } returns ""
            every { code } returns 500
        }
        private val mockNetworkResponseWithoutCode = mockk<NetworkResponse.ServerError<String, String>> {
            every { body } returns ""
            every { code } returns null
        }
        private val mockNetworkResponseError = mockk<NetworkResponse.NetworkError<String, String>> {
            every { body } returns ""
            every { error } returns IOException("")
        }
        private val mockNetworkResponseUnknownError =
            mockk<NetworkResponse.UnknownError<String, String>> {
                every { code } returns -1
                every { body } returns ""
            }
    }
}
