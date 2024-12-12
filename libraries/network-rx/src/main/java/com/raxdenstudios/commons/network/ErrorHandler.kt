package com.raxdenstudios.commons.network

interface ErrorHandler {

    fun httpError(exception: RetrofitException): Throwable
}
