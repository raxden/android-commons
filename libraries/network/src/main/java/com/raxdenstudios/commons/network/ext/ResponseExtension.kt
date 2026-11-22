package com.raxdenstudios.commons.network.ext

import com.raxdenstudios.commons.core.Answer
import com.raxdenstudios.commons.core.NetworkError
import retrofit2.Response

/**
 * Maps this Retrofit [Response] to an [Answer]: 2xx/3xx with a body is [Answer.Success], a null body
 * on a success code is [NetworkError.Unknown], and 4xx/5xx map to [NetworkError.Client] and
 * [NetworkError.Server].
 *
 * A `Response<Unit>` resolves to the [Unit] overload instead, which treats 204/205 without body as
 * success.
 */
fun <S : Any> Response<S>.toAnswer(
    errorMessage: String,
): Answer<S, NetworkError<S>> {
    val body = body()
    return code().toAnswer(body, body, errorMessage)
}

/**
 * Maps this Retrofit `Response<Unit>` to an [Answer] like the generic overload, except that a
 * 204 No Content or 205 Reset Content without body is [Answer.Success] with [Unit]: Retrofit never
 * delivers a body for those codes.
 */
@JvmName("toUnitAnswer")
fun Response<Unit>.toAnswer(
    errorMessage: String,
): Answer<Unit, NetworkError<Unit>> {
    val code = code()
    val body = body()
    val successBody = body ?: Unit.takeIf { code in NO_CONTENT_CODES }
    return code.toAnswer(successBody, body, errorMessage)
}

@Suppress("MagicNumber")
internal fun <S : Any, E : Any> Int.toAnswer(
    body: S?,
    bodyError: E? = null,
    message: String,
): Answer<S, NetworkError<E>> = when (this) {
    in (200..399) -> body?.let { Answer.Success(it) }
        ?: Answer.Failure(
            NetworkError.Unknown(
                code = this,
                body = bodyError,
                message = "Success response with null body",
            ),
        )

    in (400..499) -> Answer.Failure(
        NetworkError.Client(
            code = this,
            body = bodyError,
            message = message,
        ),
    )

    in (500..599) -> Answer.Failure(
        NetworkError.Server(
            code = this,
            body = bodyError,
            message = message,
        ),
    )

    else -> Answer.Failure(
        NetworkError.Unknown(
            code = this,
            body = bodyError,
            message = message,
        ),
    )
}

private val NO_CONTENT_CODES = setOf(204, 205)
