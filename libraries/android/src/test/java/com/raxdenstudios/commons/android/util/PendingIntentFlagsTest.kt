package com.raxdenstudios.commons.android.util

import android.app.PendingIntent
import org.junit.Assert.assertEquals
import org.junit.Test

internal class PendingIntentFlagsTest {

    @Test
    fun `Given UPDATE_IMMUTABLE When read Then it combines update current and immutable flags`() {
        val expected = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        assertEquals(expected, PendingIntentFlags.UPDATE_IMMUTABLE)
    }
}
