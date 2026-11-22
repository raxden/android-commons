package com.raxdenstudios.commons.android.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Adds [observer] to [lifeCycleOwner] while this composable is in the composition and removes it when the
 * composable leaves.
 *
 * The owner defaults to [LocalLifecycleOwner], the owner of the screen hosting the composable. Changing the
 * owner removes the observer from the old one and adds it to the new one.
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
    DisposableEffect(lifeCycleOwner) {
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
