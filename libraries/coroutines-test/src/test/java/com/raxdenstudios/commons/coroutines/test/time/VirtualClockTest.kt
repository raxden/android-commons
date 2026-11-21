package com.raxdenstudios.commons.coroutines.test.time

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

internal class VirtualClockTest {

    @Test
    fun `Given virtual clock When time advances and delay runs Then instant follows`() = runTest {
        val clock = virtualClock()

        assertThat(clock.instant()).isEqualTo(Instant.EPOCH)

        advanceTimeBy(1_000)
        assertThat(clock.instant()).isEqualTo(Instant.EPOCH.plusMillis(1_000))

        delay(500)
        assertThat(clock.millis()).isEqualTo(1_500L)
        assertThat(clock.zone).isEqualTo(ZoneOffset.UTC)
    }

    @Test
    fun `Given custom origin When time advances Then instant is origin plus virtual time`() = runTest {
        val origin = Instant.parse("2024-05-01T10:00:00Z")
        val clock = virtualClock(origin = origin)

        delay(2_000)

        assertThat(clock.instant()).isEqualTo(origin.plusSeconds(2))
    }

    @Test
    fun `Given virtual clock When withZone Then zone changes and origin and scheduler are kept`() = runTest {
        val origin = Instant.parse("2024-05-01T10:00:00Z")
        val zone = ZoneId.of("Asia/Tokyo")
        val clock = virtualClock(origin = origin)

        val zoned = clock.withZone(zone)
        delay(3_000)

        assertThat(zoned.zone).isEqualTo(zone)
        assertThat(zoned.instant()).isEqualTo(origin.plusSeconds(3))
        assertThat(clock.zone).isEqualTo(ZoneOffset.UTC)
    }
}
