package com.raxdenstudios.commons.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

fun <T : Any, S : StateFlow<T>> LifecycleOwner.launchAndCollect(
  stateFlow: S,
  body: (T) -> Unit
) {
  lifecycleScope.launchWhenStarted {
    stateFlow.collect(FlowCollector(body))
  }
}
