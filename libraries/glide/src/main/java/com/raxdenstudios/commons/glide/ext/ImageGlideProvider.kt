package com.raxdenstudios.commons.glide.ext

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(source: String) {
  if (getTag(id) == null || getTag(id) != (source)) {
    setImageBitmap(null)
    setTag(id, source)
    Glide.with(this).load(source).into(this)
  }
}
