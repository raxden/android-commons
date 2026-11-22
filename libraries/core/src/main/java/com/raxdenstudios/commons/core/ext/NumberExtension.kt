package com.raxdenstudios.commons.core.ext

fun Double?.orDefault(
    default: Double = Double.ZERO,
) = this ?: default

fun Int?.orDefault(
    default: Int = Int.ZERO,
) = this ?: default

val Double.Companion.ZERO: Double
    get() = 0.0

val Int.Companion.ZERO: Int
    get() = 0
