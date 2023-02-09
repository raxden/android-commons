package com.raxdenstudios.commons.retrofit

sealed interface NetworkError<T> {

    val message: String
    val body: T?

    data class Client<T>(
        val code: Int,
        override val body: T? = null,
        override val message: String,
    ) : NetworkError<T>

    data class Server<T>(
        val code: Int,
        override val body: T? = null,
        override val message: String,
    ) : NetworkError<T>

    data class Network<T>(
        override val body: T? = null,
        override val message: String,
    ) : NetworkError<T>

    data class Unknown<T>(
        val code: Int? = null,
        override val body: T? = null,
        override val message: String,
    ) : NetworkError<T>
}
