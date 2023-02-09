package com.raxdenstudios.commons.retrofit

sealed interface NetworkError {

    val message: String

    data class Client(
        val code: Int,
        override val message: String,
    ) : NetworkError

    data class Server(
        val code: Int,
        override val message: String,
    ) : NetworkError

    data class Network(
        override val message: String,
    ) : NetworkError

    data class Unknown(
        override val message: String,
    ) : NetworkError
}
