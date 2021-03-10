package com.raxdenstudios.commons.android.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.raxdenstudios.commons.android.Event

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(
  liveData: L,
  body: (T) -> Unit
) {
  liveData.observe(this, Observer(body))
}

inline fun <T : Any, L : LiveData<Event<T>>> LifecycleOwner.observeEvent(
  liveData: L,
  crossinline body: (T) -> Unit
) {
  liveData.observe(this, { it?.getContentIfNotHandled()?.let(body) })
}
