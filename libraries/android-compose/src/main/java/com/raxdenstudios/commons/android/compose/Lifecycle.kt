package com.raxdenstudios.commons.android.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Adds [observer] to [lifeCycleOwner] while this composable is in the composition and removes it when the
 * composable leaves.
 *
 * The owner defaults to [LocalLifecycleOwner], the owner of the screen hosting the composable. Changing the
 * owner removes the observer from the old one and adds it to the new one. Passing a different [observer] on
 * recomposition does not re-register: later events go to the latest observer.
 *
 * ## Usage
 *
 * ```kotlin
 * ComposableLifecycle(
 *     observer = LifecycleEventObserver { _, event ->
 *         if (event == Lifecycle.Event.ON_RESUME) viewModel.refresh()
 *     },
 * )
 * ```
 */
@Composable
fun ComposableLifecycle(
    observer: LifecycleEventObserver,
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    // The registered observer forwards to the latest one, so a new observer is honoured without re-registering.
    val currentObserver by rememberUpdatedState(observer)
    DisposableEffect(lifeCycleOwner) {
        val forwardingObserver = LifecycleEventObserver { owner, event ->
            currentObserver.onStateChanged(owner, event)
        }
        lifeCycleOwner.lifecycle.addObserver(forwardingObserver)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(forwardingObserver)
        }
    }
}
