package com.raxdenstudios.commons.android.ext

import android.graphics.drawable.Drawable
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Drawable.colorFilter with BlendModeColorFilterCompat directly.",
    level = DeprecationLevel.WARNING,
)
fun Drawable.setColorFilter(
    color: Int,
    blendModeCompat: BlendModeCompat = BlendModeCompat.SRC_ATOP,
) {
    colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, blendModeCompat)
}
