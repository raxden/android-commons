package com.raxdenstudios.commons.android.intent

/** The system screens and actions an [IntentFactory] can build an intent for. */
sealed class IntentType {

    /** The system details screen of this app. */
    data object AppSettings : IntentType()

    /** The Play Store listing of this app; a `.debug` suffix on the package name is removed. */
    data object PlayStore : IntentType()

    /**
     * A plain-text share sheet.
     *
     * @property subject The subject sent with the text (`EXTRA_SUBJECT`).
     * @property text The text to share (`EXTRA_TEXT`).
     */
    data class ShareText(
        val subject: String,
        val text: String,
    ) : IntentType()

    /** The notification settings screen of this app. */
    data object NotificationSettings : IntentType()

    /**
     * The screen where the user grants exact alarms for this app.
     *
     * Only exists on API 31+. Check [com.raxdenstudios.commons.android.ext.isExactAlarmAllowed]
     * before offering it; below API 31 exact alarms need no grant.
     */
    data object ExactAlarmSettings : IntentType()

    /**
     * The screen where the user allows full-screen intents for this app.
     *
     * Only exists on API 34+. Check [com.raxdenstudios.commons.android.ext.isFullScreenIntentAllowed]
     * before offering it; below API 34 full-screen intents need no grant.
     */
    data object FullScreenIntentSettings : IntentType()
}
