package com.raxdenstudios.commons.android.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

fun <T : Any, S : StateFlow<T>> LifecycleOwner.launchAndCollect(
    stateFlow: S,
    body: (T) -> Unit,
) {
    lifecycleScope.launchWhenStarted { stateFlow.collect(FlowCollector(body)) }
}

fun <T : Any, S : StateFlow<T>> Fragment.launchAndCollect(stateFlow: S, body: (T) -> Unit) {
    viewLifecycleOwner.launchAndCollect(stateFlow, body)
}
