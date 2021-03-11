package com.raxdenstudios.commons.android.ext

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.raxdenstudios.commons.android.util.SDK

fun Application.initCompatVector() {
  if (SDK.hasLollipop()) AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
}
