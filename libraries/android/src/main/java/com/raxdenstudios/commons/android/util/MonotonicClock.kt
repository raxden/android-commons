package com.raxdenstudios.commons.android.util

import android.os.SystemClock

/**
 * Source of monotonic time for measuring intervals such as timers, play time or debounce.
 *
 * Readings are not wall-clock times. Only the difference between two readings is meaningful, and
 * changes to the wall clock (by the user or the network) do not affect them, so an interval
 * measured across such a change stays correct. For the current date or time, use `java.time.Clock`.
 *
 * This is a `fun interface`, so tests can pass a lambda instead of a fake class. Production code
 * injects [MonotonicClock.System].
 *
 * ## Usage
 *
 * ```kotlin
 * class PlayTimer(private val clock: MonotonicClock = MonotonicClock.System) {
 *     private val startedAt = clock.elapsedRealtimeMillis()
 *     fun elapsedMillis(): Long = clock.elapsedRealtimeMillis() - startedAt
 * }
 *
 * // In a test, driven by virtual time:
 * val timer = PlayTimer(clock = MonotonicClock { testScheduler.currentTime })
 * ```
 */
fun interface MonotonicClock {

    /**
     * Returns the milliseconds since the device booted, including time spent in deep sleep.
     *
     * Only the difference between two readings has a meaning; the absolute value is not a date.
     */
    fun elapsedRealtimeMillis(): Long

    companion object {

        /** The production clock, backed by `SystemClock.elapsedRealtime()`. */
        val System: MonotonicClock = MonotonicClock { SystemClock.elapsedRealtime() }
    }
}
