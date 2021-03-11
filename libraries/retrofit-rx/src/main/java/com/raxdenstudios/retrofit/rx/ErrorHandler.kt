package com.raxdenstudios.retrofit.rx

interface ErrorHandler {

  fun apiError(exception: RetrofitException): Throwable
}
