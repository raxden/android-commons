package com.raxdenstudios.commons.glide

import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageGlideProvider {

  fun loadImage(view: ImageView, source: String) {
    if (view.getTag(view.id) == null || view.getTag(view.id) != (source)) {
      view.setImageBitmap(null)
      view.setTag(view.id, source)
      Glide.with(view).load(source).into(view)
    }
  }
}
