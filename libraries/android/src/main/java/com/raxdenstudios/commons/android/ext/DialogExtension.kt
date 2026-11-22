package com.raxdenstudios.commons.android.ext

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Dialog.window directly or a Jetpack Compose dialog.",
    level = DeprecationLevel.WARNING,
)
fun Dialog.setTransparentBackground() {
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}
