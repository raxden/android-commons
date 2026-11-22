@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.android.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use repeatOnLifecycle(Lifecycle.State.STARTED) with collect, or collectAsStateWithLifecycle in Compose.",
    level = DeprecationLevel.WARNING,
)
fun <T : Any, S : StateFlow<T>> LifecycleOwner.launchAndCollect(
    stateFlow: S,
    body: (T) -> Unit,
) {
    lifecycleScope.launchWhenStarted { stateFlow.collect(FlowCollector(body)) }
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use repeatOnLifecycle(Lifecycle.State.STARTED) with collect, or collectAsStateWithLifecycle in Compose.",
    level = DeprecationLevel.WARNING,
)
fun <T : Any, S : StateFlow<T>> Fragment.launchAndCollect(
    stateFlow: S,
    body: (T) -> Unit,
) {
    viewLifecycleOwner.launchAndCollect(stateFlow, body)
}
