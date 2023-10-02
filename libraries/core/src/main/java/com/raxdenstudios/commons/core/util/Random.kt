package com.raxdenstudios.commons.core.util

import kotlin.random.Random

object Random {

    fun generateStringWithSize(size: Int) = (1..size)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
}
