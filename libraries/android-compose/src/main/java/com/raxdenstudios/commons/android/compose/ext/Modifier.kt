package com.raxdenstudios.commons.android.compose.ext

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

/** Flips the content horizontally, e.g. to mirror an image for right-to-left layouts or a front camera preview. */
@Stable
fun Modifier.mirror(): Modifier = this.scale(scaleX = -1f, scaleY = 1f)

/** Fixes the aspect ratio to 1:1 (square). */
fun Modifier.aspectRatio11() = aspectRatio(ASPECT_RATIO_1_1)

/** Fixes the aspect ratio to 4:3 (width:height). */
fun Modifier.aspectRatio43() = aspectRatio(ASPECT_RATIO_4_3)

/** Fixes the aspect ratio to 16:9 (width:height), the common widescreen video ratio. */
fun Modifier.aspectRatio169() = aspectRatio(ASPECT_RATIO_16_9)

/** Fixes the aspect ratio to 2:3 (width:height), the portrait photo ratio. */
fun Modifier.aspectRatio23() = aspectRatio(ASPECT_RATIO_2_3)

private const val ASPECT_RATIO_16_9 = 16f / 9f
private const val ASPECT_RATIO_4_3 = 4f / 3f
private const val ASPECT_RATIO_2_3 = 2f / 3f
private const val ASPECT_RATIO_1_1 = 1f / 1f
