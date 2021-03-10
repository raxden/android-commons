package com.raxdenstudios.commons.android.ext

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.raxdenstudios.commons.android.property.ViewBindingDelegate

fun View.doItVisible() {
  if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.doItGone() {
  if (visibility != View.GONE) visibility = View.GONE
}

fun ViewGroup.inflateView(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
  LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

inline fun <reified T : ViewBinding> ViewGroup.viewBinding() = ViewBindingDelegate(T::class.java)

@Suppress("MagicNumber")
fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
  var lastTimeClicked: Long = 0
  setOnClickListener {
    if (SystemClock.elapsedRealtime() - lastTimeClicked >= 500) {
      lastTimeClicked = SystemClock.elapsedRealtime()
      onSafeClick(it)
    }
  }
}
