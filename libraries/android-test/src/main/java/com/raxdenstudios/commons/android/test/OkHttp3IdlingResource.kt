package com.raxdenstudios.commons.android.test

import androidx.annotation.CheckResult
import androidx.test.espresso.IdlingResource
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

/**
 * Espresso [IdlingResource] that is busy while the [OkHttpClient] dispatcher has running calls, so Espresso
 * waits for network work to finish before it checks the UI.
 *
 * Creating it replaces the dispatcher's `idleCallback`, so only one such resource should exist per client.
 *
 * ## Usage
 *
 * ```kotlin
 * val idlingResource = OkHttp3IdlingResource.create("api", okHttpClient)
 * Espresso.registerIdlingResources(idlingResource)
 * // ... run the UI test ...
 * Espresso.unregisterIdlingResources(idlingResource)
 * ```
 */
class OkHttp3IdlingResource private constructor(
    private val name: String,
    private val dispatcher: Dispatcher,
) : IdlingResource {

    /** Set by Espresso when the resource is registered; notified when the dispatcher becomes idle. */
    @Volatile
    var callback: IdlingResource.ResourceCallback? = null

    init {
        dispatcher.idleCallback = Runnable {
            val callback = callback
            callback?.onTransitionToIdle()
        }
    }

    override fun getName(): String = name

    override fun isIdleNow(): Boolean = dispatcher.runningCallsCount() == 0

    override fun registerIdleTransitionCallback(
        callback: IdlingResource.ResourceCallback,
    ) {
        this.callback = callback
    }

    companion object {
        /**
         * Creates an [IdlingResource] named [name] for the dispatcher of [client]. Register the returned
         * instance with `Espresso.registerIdlingResources` before the UI interaction, otherwise Espresso does
         * not wait for it.
         */
        @CheckResult // Extra guards as a library.
        fun create(
            name: String,
            client: OkHttpClient,
        ): IdlingResource = OkHttp3IdlingResource(
            name,
            client.dispatcher,
        )
    }
}
