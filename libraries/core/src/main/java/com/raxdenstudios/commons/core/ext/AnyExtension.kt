package com.raxdenstudios.commons.core.ext

// Fluent type checks

inline fun <reified T> Any.castAs() = this as? T

inline fun <reified T> Any.requireAs() = this as T

// Typed scope functions

inline fun <reified T, R> Any.letAs(
    block: (T) -> R,
): R? = castAs<T>()?.let(block)

inline fun <reified T, R> Any.runAs(
    block: T.() -> R,
): R? = castAs<T>()?.run(block)

inline fun <reified T> Any.applyAs(
    block: T.() -> Unit,
): T? = castAs<T>()?.apply(block)

inline fun <reified T> Any.alsoAs(
    block: (T) -> Unit,
): T? = castAs<T>()?.also(block)

// Conditional scope functions

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use an if expression with apply instead.",
    level = DeprecationLevel.WARNING,
)
inline fun <reified T> T.applyWhen(
    condition: Boolean,
    block: T.() -> Unit,
): T = if (condition) apply(block) else this

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use an if expression with also instead.",
    level = DeprecationLevel.WARNING,
)
inline fun <reified T> T.alsoWhen(
    condition: Boolean,
    block: (T) -> Unit,
): T = if (condition) also(block) else this
