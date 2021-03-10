package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.ContextCompat

inline fun <reified T : Activity> Context.intentFor(params: Parcelable? = null) =
  Intent(this, T::class.java).apply {
    if (params != null) putExtra("params", params)
  }

fun Intent.startActivity(activity: Activity, options: Bundle? = null) {
  ContextCompat.startActivity(activity, this, options)
}
