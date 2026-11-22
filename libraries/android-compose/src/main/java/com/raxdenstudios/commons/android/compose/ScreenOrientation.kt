package com.raxdenstudios.commons.android.compose

/** Orientation a screen requests from its host activity while it is composed, see [LockScreenOrientation]. */
enum class ScreenOrientation {
    /** Lets the system decide, using the sensor and the user's rotation setting. */
    Unspecified,

    /** Forces portrait. */
    Portrait,

    /** Forces landscape. */
    Landscape,
}
