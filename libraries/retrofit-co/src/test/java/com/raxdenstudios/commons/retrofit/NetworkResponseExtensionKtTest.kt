package com.raxdenstudios.commons.retrofit

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.onFailure
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

internal class NetworkResponseExtensionKtTest {

    @Test
    fun `Given a client error with code 4xx, When toResult is called, Then a ResultData_Error with NetworkException_Client is returned`() {
        val networkResponse: NetworkResponse.ServerError<String, String> = mockk {
            every { body } returns ""
            every { code } returns 400
        }

        val result: ResultData<String, NetworkError> = networkResponse.toResultData(
            errorMessage = "message",
            transformFailure = { "" }
        )

        assertEquals(
            ResultData.Failure(
                NetworkError.Client(code = 400, body = "", message = "message")
            ),
            result
        )
    }

    @Test
    fun `Given a server error with code 5xx, When toResult is called, Then a ResultData_Error with NetworkException_Server is returned`() {
        val networkResponse: NetworkResponse.ServerError<String, String> = mockk {
            every { body } returns ""
            every { code } returns 500
        }

        val result: ResultData<String, NetworkError> = networkResponse.toResultData(
            errorMessage = "message",
            transformFailure = { "" }
        )

        assertEquals(
            ResultData.Failure(
                NetworkError.Server(code = 500, body = "", message = "message")
            ),
            result
        )
    }

    @Test
    fun `Given a network error, When toResult is called, Then a ResultData_Error with NetworkException_Network is returned`() {
        val networkResponse: NetworkResponse.NetworkError<String, String> = mockk {
            every { body } returns ""
            every { error } returns IOException("")
        }

        val result: ResultData<String, NetworkError> = networkResponse.toResultData("message") { }

        result.onFailure { error ->
            assertEquals(
                NetworkError.Network("message"),
                error
            )
        }
    }

    @Test
    fun `Given a unknown error, When toResult is called, Then a ResultData_Error with NetworkException_Unknown is returned`() {
        val networkResponse: NetworkResponse.UnknownError<String, String> = mockk {
            every { code } returns -1
            every { body } returns ""
        }

        val result: ResultData<String, NetworkError> = networkResponse.toResultData(
            errorMessage = "message",
            transformFailure = { "" }
        )

        result.onFailure { error ->
            assertEquals(
                NetworkError.Unknown(code = -1, body = "", message = "message"),
                error
            )
        }
    }
}
