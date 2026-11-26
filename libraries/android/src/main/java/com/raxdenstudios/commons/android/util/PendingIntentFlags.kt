package com.raxdenstudios.commons.android.util

import android.app.PendingIntent

/**
 * Flag combinations for creating [PendingIntent]s, so callers do not have to combine platform constants by hand.
 *
 * ## Usage
 *
 * ```kotlin
 * val pendingIntent = PendingIntent.getActivity(
 *     context,
 *     requestCode,
 *     intent,
 *     PendingIntentFlags.UPDATE_IMMUTABLE,
 * )
 * ```
 */
object PendingIntentFlags {

    /**
     * Updates the extras of an existing [PendingIntent] with the same request code and intent, and makes it
     * immutable so the system does not let other apps change it. Mutability flags are required from API 31, and
     * minSdk is 28, so this constant can be used without an SDK check.
     */
    const val UPDATE_IMMUTABLE: Int = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
}
