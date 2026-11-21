package com.raxdenstudios.commons.coroutines.test.time

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Returns a [Clock] whose instant is [origin] plus the virtual time of this scope's scheduler.
 *
 * The clock follows the [TestScope]'s virtual time: it moves when the test advances time (for
 * example with `advanceTimeBy`, `advanceUntilIdle` or skipped `delay` calls) and stays put
 * otherwise. Use it when production code reads the wall clock and the test must control elapsed
 * time. Precision is one millisecond, the resolution of the test scheduler. Zone views created with
 * `withZone` read the same virtual time.
 *
 * @param origin the instant that corresponds to virtual time zero. Defaults to [Instant.EPOCH].
 * @param zone the zone used by `getZone`. Defaults to [ZoneOffset.UTC].
 */
fun TestScope.virtualClock(
    origin: Instant = Instant.EPOCH,
    zone: ZoneId = ZoneOffset.UTC,
): Clock = VirtualClock(testScheduler, origin, zone)

private class VirtualClock(
    private val scheduler: TestCoroutineScheduler,
    private val origin: Instant,
    private val zone: ZoneId,
) : Clock() {

    override fun getZone(): ZoneId = zone

    override fun withZone(
        zone: ZoneId,
    ): Clock = VirtualClock(scheduler, origin, zone)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun instant(): Instant = origin.plusMillis(scheduler.currentTime)
}
