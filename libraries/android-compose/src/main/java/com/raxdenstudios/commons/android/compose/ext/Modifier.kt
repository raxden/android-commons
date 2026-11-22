package com.raxdenstudios.commons.android.compose.ext

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.Modifier

/** Fixes the aspect ratio to 1:1 (square). */
fun Modifier.aspectRatio11() = aspectRatio(ASPECT_RATIO_1_1)

private const val ASPECT_RATIO_1_1 = 1f / 1f
