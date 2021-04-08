package com.raxdenstudios.retrofit.rx

import com.raxdenstudios.commons.retrofit.RetrofitException

interface ErrorHandler {

  fun httpError(exception: RetrofitException): Throwable
}
