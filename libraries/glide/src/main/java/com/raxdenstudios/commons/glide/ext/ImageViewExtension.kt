package com.raxdenstudios.commons.glide.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.raxdenstudios.commons.glide.GlideUtil
import com.raxdenstudios.commons.glide.ScaleType

fun ImageView.loadImage(
    source: String,
    scaleType: ScaleType = ScaleType.CENTER_CROP
) {
    if (getTag(id) == null || getTag(id) != (source)) {
        setImageBitmap(null)
        setTag(id, source)
        GlideUtil.loadImage(source, this, scaleType = scaleType)
    }
}

fun ImageView.loadImage(
    source: Drawable,
    scaleType: ScaleType = ScaleType.CENTER_CROP
) {
    if (getTag(id) == null || getTag(id) != (source)) {
        setImageBitmap(null)
        setTag(id, source)
        GlideUtil.loadImage(source, this, scaleType = scaleType)
    }
}
