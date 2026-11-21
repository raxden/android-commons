package com.raxdenstudios.commons.coroutines.test.time

import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.time.toJavaDuration

/**
 * Test [Clock] whose instant changes only when the test moves it with [advanceBy] or [set].
 *
 * Use it wherever production code reads the time, so tests can control timestamps without real
 * waiting. Views created with [withZone] share the same instant, so advancing one advances all of
 * them. The clock is not synchronized: concurrent calls to [advanceBy] can lose updates, so drive it
 * from a single thread.
 *
 * ## Usage
 *
 * ```kotlin
 * val clock = MutableClock(initial = Instant.parse("2024-01-01T00:00:00Z"))
 *
 * clock.advanceBy(5.minutes)
 * check(clock.instant() == Instant.parse("2024-01-01T00:05:00Z"))
 * ```
 *
 * @param initial the instant the clock starts at. Defaults to [Instant.EPOCH].
 * @param zone the zone used by [getZone]. Defaults to [ZoneOffset.UTC].
 */
class MutableClock private constructor(
    private val state: State,
    private val zone: ZoneId,
) : Clock() {

    constructor(
        initial: Instant = Instant.EPOCH,
        zone: ZoneId = ZoneOffset.UTC,
    ) : this(State(initial), zone)

    fun advanceBy(
        duration: Duration,
    ) {
        state.instant = state.instant.plus(duration)
    }

    fun advanceBy(
        duration: kotlin.time.Duration,
    ) {
        advanceBy(duration.toJavaDuration())
    }

    fun set(
        instant: Instant,
    ) {
        state.instant = instant
    }

    override fun getZone(): ZoneId = zone

    override fun withZone(
        zone: ZoneId,
    ): Clock = MutableClock(state, zone)

    override fun instant(): Instant = state.instant

    private class State(
        @Volatile var instant: Instant,
    )
}
