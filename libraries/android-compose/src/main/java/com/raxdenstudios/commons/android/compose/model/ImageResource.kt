package com.raxdenstudios.commons.android.compose.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource

/**
 * Describes an image without loading it, so a ViewModel can emit it. Call [toPainter] inside composition to
 * get the [Painter] to draw.
 *
 * Every implementation is [Immutable]: once created, its values never change. Composables taking an
 * [ImageResource] are therefore stable and skip recomposition when they receive an equal value.
 */
@Immutable
sealed interface ImageResource {

    /** A drawable resource, given by its `R.drawable` id. */
    @Immutable
    data class Drawable(
        @DrawableRes val image: Int,
    ) : ImageResource

    /** An already built [ImageVector], e.g. from an icon set. */
    @Immutable
    data class Vector(
        val image: ImageVector,
    ) : ImageResource

    /** Returns the [Painter] for this image, remembering vector images across recompositions. */
    @Composable
    fun toPainter(): Painter = when (this) {
        is Drawable -> painterResource(id = image)
        is Vector -> rememberVectorPainter(image)
    }
}
