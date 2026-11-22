package com.raxdenstudios.commons.core.util

import kotlin.random.Random

object Random {

    @Deprecated(
        message = "Will be removed in the next major version. " +
            "Use kotlin.random.Random with a character pool instead.",
        level = DeprecationLevel.WARNING,
    )
    fun generateStringWithSize(
        size: Int,
    ) = (1..size)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
}
