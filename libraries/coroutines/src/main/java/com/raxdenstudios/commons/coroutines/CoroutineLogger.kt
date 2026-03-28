package com.raxdenstudios.commons.coroutines

interface CoroutineLogger {
    fun logError(tag: String, message: String?, throwable: Throwable)
}