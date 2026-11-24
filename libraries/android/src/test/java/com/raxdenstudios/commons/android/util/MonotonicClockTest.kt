package com.raxdenstudios.commons.android.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowSystemClock
import java.time.Duration

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class MonotonicClockTest {

    @Test
    fun `Given system clock When time advances Then elapsed realtime advances by the same amount`() {
        val before = MonotonicClock.System.elapsedRealtimeMillis()

        ShadowSystemClock.advanceBy(Duration.ofMillis(1_500))

        val after = MonotonicClock.System.elapsedRealtimeMillis()
        assertThat(after - before).isEqualTo(1_500)
    }
}
