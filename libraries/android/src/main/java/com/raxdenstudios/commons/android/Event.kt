package com.raxdenstudios.commons.android

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use StateFlow/SharedFlow (or Channel) to expose one-shot UI events instead of LiveData wrappers.",
    level = DeprecationLevel.WARNING,
)
open class Event<out T>(
    private val content: T,
) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
