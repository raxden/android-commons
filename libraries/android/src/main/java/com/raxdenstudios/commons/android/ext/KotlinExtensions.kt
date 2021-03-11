package com.raxdenstudios.commons.android.ext

inline val <reified T> T?.exhaustive: T?
  get() = this
