package com.raxdenstudios.commons.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

class ErrorResponseInterceptor : Interceptor {

    @Suppress("MagicNumber")
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!response.isSuccessful) {
            val errorBody = response.body?.string()
            throw when (response.code) {
                401 -> UnauthorizedException(errorBody)
                403 -> ForbiddenException(errorBody)
                404 -> NotFoundException(errorBody)
                500 -> ServerException(errorBody)
                else -> HttpException(response.code, errorBody)
            }
        }

        return response
    }
}

open class HttpException(val code: Int, val errorBody: String?) :
    IOException("HTTP $code: ${errorBody ?: "Unknown error"}")

class UnauthorizedException(errorBody: String?) : HttpException(HTTP_UNAUTHORIZED, errorBody)
class ForbiddenException(errorBody: String?) : HttpException(HTTP_FORBIDDEN, errorBody)
class NotFoundException(errorBody: String?) : HttpException(HTTP_NOT_FOUND, errorBody)
class ServerException(errorBody: String?) : HttpException(HTTP_INTERNAL_ERROR, errorBody)
