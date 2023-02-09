package com.raxdenstudios.commons.retrofit

sealed interface NetworkError {

    val message: String

    data class Client<T>(
        val code: Int,
        val body: T? = null,
        override val message: String,
    ) : NetworkError

    data class Server<T>(
        val code: Int,
        val body: T? = null,
        override val message: String,
    ) : NetworkError

    data class Network(
        override val message: String,
    ) : NetworkError

    data class Unknown<T>(
        val code: Int? = null,
        val body: T? = null,
        override val message: String,
    ) : NetworkError
}
