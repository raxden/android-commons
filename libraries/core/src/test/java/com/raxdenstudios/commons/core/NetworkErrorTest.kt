package com.raxdenstudios.commons.core

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class NetworkErrorTest {

    @Test
    fun `Client error should have correct properties`() {
        val error = NetworkError.Client<String>(
            code = 400,
            body = "Bad Request",
            message = "Invalid input"
        )

        assertThat(error.code).isEqualTo(400)
        assertThat(error.body).isEqualTo("Bad Request")
        assertThat(error.message).isEqualTo("Invalid input")
    }

    @Test
    fun `Client error with null body should work`() {
        val error = NetworkError.Client<String>(
            code = 401,
            body = null,
            message = "Unauthorized"
        )

        assertThat(error.code).isEqualTo(401)
        assertThat(error.body).isNull()
        assertThat(error.message).isEqualTo("Unauthorized")
    }

    @Test
    fun `Server error should have correct properties`() {
        val error = NetworkError.Server<String>(
            code = 500,
            body = "Internal Server Error",
            message = "Server crashed"
        )

        assertThat(error.code).isEqualTo(500)
        assertThat(error.body).isEqualTo("Internal Server Error")
        assertThat(error.message).isEqualTo("Server crashed")
    }

    @Test
    fun `Server error with null body should work`() {
        val error = NetworkError.Server<String>(
            code = 503,
            body = null,
            message = "Service Unavailable"
        )

        assertThat(error.code).isEqualTo(503)
        assertThat(error.body).isNull()
        assertThat(error.message).isEqualTo("Service Unavailable")
    }

    @Test
    fun `Network error should have correct properties`() {
        val error = NetworkError.Network<String>(
            body = "Connection timeout",
            message = "Network timeout"
        )

        assertThat(error.body).isEqualTo("Connection timeout")
        assertThat(error.message).isEqualTo("Network timeout")
    }

    @Test
    fun `Network error with null body should work`() {
        val error = NetworkError.Network<String>(
            body = null,
            message = "No internet connection"
        )

        assertThat(error.body).isNull()
        assertThat(error.message).isEqualTo("No internet connection")
    }

    @Test
    fun `Unknown error should have correct properties`() {
        val error = NetworkError.Unknown<String>(
            code = 999,
            body = "Unknown error body",
            message = "Unknown error occurred"
        )

        assertThat(error.code).isEqualTo(999)
        assertThat(error.body).isEqualTo("Unknown error body")
        assertThat(error.message).isEqualTo("Unknown error occurred")
    }

    @Test
    fun `Unknown error with null code and body should work`() {
        val error = NetworkError.Unknown<String>(
            code = null,
            body = null,
            message = "Unknown error"
        )

        assertThat(error.code).isNull()
        assertThat(error.body).isNull()
        assertThat(error.message).isEqualTo("Unknown error")
    }

    @Test
    fun `NetworkError should work with different body types`() {
        data class ErrorBody(val errorCode: String, val details: String)

        val errorBody = ErrorBody("ERR_001", "Validation failed")
        val error = NetworkError.Client(
            code = 400,
            body = errorBody,
            message = "Validation error"
        )

        assertThat(error.body).isEqualTo(errorBody)
        assertThat(error.body?.errorCode).isEqualTo("ERR_001")
        assertThat(error.body?.details).isEqualTo("Validation failed")
    }

    @Test
    fun `NetworkError sealed interface should allow polymorphic usage`() {
        val errors: List<NetworkError<String>> = listOf(
            NetworkError.Client(code = 400, message = "Bad Request"),
            NetworkError.Server(code = 500, message = "Server Error"),
            NetworkError.Network(message = "Network Error"),
            NetworkError.Unknown(message = "Unknown Error")
        )

        assertThat(errors).hasSize(4)
        assertThat(errors[0]).isInstanceOf(NetworkError.Client::class.java)
        assertThat(errors[1]).isInstanceOf(NetworkError.Server::class.java)
        assertThat(errors[2]).isInstanceOf(NetworkError.Network::class.java)
        assertThat(errors[3]).isInstanceOf(NetworkError.Unknown::class.java)
    }

    @Test
    fun `NetworkError can be used in when expression`() {
        val error: NetworkError<String> = NetworkError.Client(
            code = 404,
            message = "Not Found"
        )

        val result = when (error) {
            is NetworkError.Client -> "Client error: ${error.code}"
            is NetworkError.Server -> "Server error: ${error.code}"
            is NetworkError.Network -> "Network error"
            is NetworkError.Unknown -> "Unknown error"
        }

        assertThat(result).isEqualTo("Client error: 404")
    }
}
