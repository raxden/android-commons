package com.raxdenstudios.commons.network.ext

import com.raxdenstudios.commons.core.Answer
import com.raxdenstudios.commons.core.NetworkError
import retrofit2.Response

@Suppress("SwallowedException", "TooGenericExceptionCaught")
fun <S : Any> Response<S>.toAnswer(
    errorMessage: String,
): Answer<S, NetworkError<S>> {

    val code = code()
    var body: S? = null

    return try {
        body = body()
        code.toAnswer(body, body, errorMessage)
    } catch (e: Throwable) {
        Answer.Failure(
            NetworkError.Unknown(
                code = code,
                body = body,
                message = errorMessage
            )
        )
    }
}

@Suppress("MagicNumber")
internal fun <S : Any, E : Any> Int.toAnswer(
    body: S?,
    bodyError: E? = null,
    message: String,
): Answer<S, NetworkError<E>> = when (this) {
    in (200..399) -> Answer.Success(body!!)
    in (400..499) -> Answer.Failure(
        NetworkError.Client(
            code = this,
            body = bodyError,
            message = message
        )
    )

    in (500..599) -> Answer.Failure(
        NetworkError.Server(
            code = this,
            body = bodyError,
            message = message
        )
    )

    else -> Answer.Failure(
        NetworkError.Unknown(
            code = this,
            body = bodyError,
            message = message
        )
    )
}
