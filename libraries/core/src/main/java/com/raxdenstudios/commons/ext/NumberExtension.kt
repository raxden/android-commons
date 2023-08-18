package com.raxdenstudios.commons.ext

fun Byte?.orDefault(default: Byte = Byte.ZERO) = this ?: default

fun Float?.orDefault(default: Float = Float.ZERO) = this ?: default

fun Double?.orDefault(default: Double = Double.ZERO) = this ?: default

fun Int?.orDefault(default: Int = Int.ZERO) = this ?: default

fun Short?.orDefault(default: Short = Short.ZERO) = this ?: default

fun Long?.orDefault(default: Long = Long.ZERO) = this ?: default

val Byte.Companion.ZERO: Byte
    get() = 0

val Float.Companion.ZERO: Float
    get() = 0.0f

val Double.Companion.ZERO: Double
    get() = 0.0

val Int.Companion.ZERO: Int
    get() = 0

val Short.Companion.ZERO: Short
    get() = 0

val Long.Companion.ZERO: Long
    get() = 0
