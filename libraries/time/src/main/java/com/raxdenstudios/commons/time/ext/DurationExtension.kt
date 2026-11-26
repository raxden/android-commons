package com.raxdenstudios.commons.time.ext

import java.util.Locale
import kotlin.time.Duration

/**
 * Returns this duration as clock text: `mm:ss` below one hour and `h:mm:ss` from one hour.
 * From one hour the format switches to `h:mm:ss`, so 90 minutes is `1:30:00`, not `90:00`;
 * the minutes field never exceeds 59.
 *
 * [padMinutes] controls the leading zero of the minutes field below one hour. With `true` (the default)
 * minutes are always two digits, so 95 seconds is `01:35` and 5 seconds is `00:05`. With `false` the leading
 * zero is dropped, so 95 seconds is `1:35` and 5 seconds is `0:05`. The hour field is unaffected by
 * [padMinutes]: from one hour the minutes are always two digits.
 *
 * Only whole seconds are shown. The fraction is truncated, not rounded, so `1.999.seconds`
 * shows `00:01`. Negative durations show `00:00`. The hour field is not zero-padded and has no
 * upper limit (`100.hours` shows `100:00:00`), while minutes and seconds are always two digits.
 * Digits are ASCII because the text is formatted with [Locale.ROOT], whatever the device locale.
 *
 * ## Usage
 *
 * ```kotlin
 * 95.seconds.toClockText()                        // "01:35"
 * 95.seconds.toClockText(padMinutes = false)      // "1:35"
 * (1.hours + 2.minutes + 3.seconds).toClockText() // "1:02:03"
 * (-5).seconds.toClockText()                      // "00:00"
 * ```
 */
fun Duration.toClockText(
    padMinutes: Boolean = true,
): String {
    val totalSeconds = inWholeSeconds.coerceAtLeast(0L)
    val hours = totalSeconds / SECONDS_IN_HOUR
    val minutes = totalSeconds % SECONDS_IN_HOUR / SECONDS_IN_MINUTE
    val seconds = totalSeconds % SECONDS_IN_MINUTE
    return if (hours > 0) {
        String.format(Locale.ROOT, "%d:%02d:%02d", hours, minutes, seconds)
    } else if (padMinutes) {
        String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
    } else {
        String.format(Locale.ROOT, "%d:%02d", minutes, seconds)
    }
}

private const val SECONDS_IN_MINUTE = 60L
private const val SECONDS_IN_HOUR = 3600L
