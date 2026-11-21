package com.raxdenstudios.commons.coroutines.test.time

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.time.Duration.Companion.seconds

internal class MutableClockTest {

    @Test
    fun `Given default constructor When reading Then instant is EPOCH and zone is UTC`() {
        val clock = MutableClock()

        assertThat(clock.instant()).isEqualTo(Instant.EPOCH)
        assertThat(clock.zone).isEqualTo(ZoneOffset.UTC)
    }

    @Test
    fun `Given java duration When advanceBy Then instant moves forward`() {
        val clock = MutableClock()

        clock.advanceBy(Duration.ofMinutes(2))

        assertThat(clock.instant()).isEqualTo(Instant.EPOCH.plusSeconds(120))
    }

    @Test
    fun `Given kotlin duration When advanceBy Then instant moves forward`() {
        val clock = MutableClock()

        clock.advanceBy(5.seconds)

        assertThat(clock.instant()).isEqualTo(Instant.EPOCH.plusSeconds(5))
    }

    @Test
    fun `Given an instant When set Then clock returns it`() {
        val clock = MutableClock()
        val target = Instant.parse("2024-01-01T00:00:00Z")

        clock.set(target)

        assertThat(clock.instant()).isEqualTo(target)
    }

    @Test
    fun `Given clock When withZone Then zone changes and instant is shared both ways`() {
        val clock = MutableClock()
        val zone = ZoneId.of("Europe/Madrid")

        val zoned = clock.withZone(zone) as MutableClock
        clock.advanceBy(Duration.ofSeconds(10))

        assertThat(zoned.zone).isEqualTo(zone)
        assertThat(clock.zone).isEqualTo(ZoneOffset.UTC)
        assertThat(zoned.instant()).isEqualTo(Instant.EPOCH.plusSeconds(10))

        zoned.set(Instant.EPOCH.plusSeconds(99))

        assertThat(clock.instant()).isEqualTo(Instant.EPOCH.plusSeconds(99))
    }

    @Test
    fun `Given advanced clock When millis Then matches instant`() {
        val clock = MutableClock(Instant.ofEpochMilli(1_000))

        clock.advanceBy(Duration.ofMillis(250))

        assertThat(clock.millis()).isEqualTo(1_250L)
    }
}
