package com.raxdenstudios.commons.network.ext

import com.raxdenstudios.commons.core.NetworkError
import com.raxdenstudios.commons.core.ResultData
import retrofit2.Response

@Suppress("SwallowedException", "TooGenericExceptionCaught")
fun <S : Any> Response<S>.toResultData(
    errorMessage: String,
): ResultData<S, NetworkError<S>> {

    val code = code()
    var body: S? = null

    return try {
        body = body()
        code.toResultData(body, body, errorMessage)
    } catch (e: Throwable) {
        ResultData.Failure(
            NetworkError.Unknown(
                code = code,
                body = body,
                message = errorMessage
            )
        )
    }
}

@Suppress("MagicNumber")
internal fun <S : Any, E : Any> Int.toResultData(
    body: S?,
    bodyError: E? = null,
    message: String,
): ResultData<S, NetworkError<E>> = when (this) {
    in (200..399) -> ResultData.Success(body!!)
    in (400..499) -> ResultData.Failure(
        NetworkError.Client(
            code = this,
            body = bodyError,
            message = message
        )
    )

    in (500..599) -> ResultData.Failure(
        NetworkError.Server(
            code = this,
            body = bodyError,
            message = message
        )
    )

    else -> ResultData.Failure(
        NetworkError.Unknown(
            code = this,
            body = bodyError,
            message = message
        )
    )
}
