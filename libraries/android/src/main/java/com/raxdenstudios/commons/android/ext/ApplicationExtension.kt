package com.raxdenstudios.commons.android.ext

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

fun Application.initCompatVector() {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
}
