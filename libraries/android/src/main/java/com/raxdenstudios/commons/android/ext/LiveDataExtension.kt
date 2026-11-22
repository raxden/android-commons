@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.android.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.raxdenstudios.commons.android.Event

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use StateFlow with repeatOnLifecycle(Lifecycle.State.STARTED) or collectAsStateWithLifecycle in Compose.",
    level = DeprecationLevel.WARNING,
)
fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(
    liveData: L,
    body: (T) -> Unit,
) {
    liveData.observe(this, Observer(body))
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use StateFlow with repeatOnLifecycle(Lifecycle.State.STARTED) or collectAsStateWithLifecycle in Compose.",
    level = DeprecationLevel.WARNING,
)
fun <T : Any, L : LiveData<T>> Fragment.observe(
    liveData: L,
    body: (T) -> Unit,
) {
    liveData.observe(viewLifecycleOwner, Observer(body))
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use StateFlow with repeatOnLifecycle(Lifecycle.State.STARTED) or collectAsStateWithLifecycle in Compose.",
    level = DeprecationLevel.WARNING,
)
fun <T : Any, L : LiveData<T>> View.observe(
    liveData: L,
    body: (T) -> Unit,
) {
    liveData.observe(requireLifecycleOwner(), Observer(body))
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use StateFlow with repeatOnLifecycle(Lifecycle.State.STARTED) or collectAsStateWithLifecycle in Compose.",
    level = DeprecationLevel.WARNING,
)
inline fun <T : Any, L : LiveData<Event<T>>> LifecycleOwner.observeEvent(
    liveData: L,
    crossinline body: (T) -> Unit,
) {
    liveData.observe(this) { it?.getContentIfNotHandled()?.let(body) }
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use StateFlow with repeatOnLifecycle(Lifecycle.State.STARTED) or collectAsStateWithLifecycle in Compose.",
    level = DeprecationLevel.WARNING,
)
inline fun <T : Any, L : LiveData<Event<T>>> Fragment.observeEvent(
    liveData: L,
    crossinline body: (T) -> Unit,
) {
    liveData.observe(viewLifecycleOwner) { it?.getContentIfNotHandled()?.let(body) }
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use StateFlow with repeatOnLifecycle(Lifecycle.State.STARTED) or collectAsStateWithLifecycle in Compose.",
    level = DeprecationLevel.WARNING,
)
inline fun <T : Any, L : LiveData<Event<T>>> View.observeEvent(
    liveData: L,
    crossinline body: (T) -> Unit,
) {
    liveData.observe(requireLifecycleOwner()) { it?.getContentIfNotHandled()?.let(body) }
}

/**
 * The [LifecycleOwner] of the view tree this view belongs to, falling back to its context when that is a
 * [LifecycleOwner] (for example a view not attached yet inside an activity).
 */
@PublishedApi
internal fun View.requireLifecycleOwner(): LifecycleOwner = findViewTreeLifecycleOwner()
    ?: context as? LifecycleOwner
    ?: error("View $this has no LifecycleOwner: attach it to a lifecycle-aware host before observing")

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use StateFlow with repeatOnLifecycle(Lifecycle.State.STARTED) or collectAsStateWithLifecycle in Compose.",
    level = DeprecationLevel.WARNING,
)
fun <T> MutableLiveData<T>.update(
    function: (T) -> T,
) {
    this.value = function(this.value!!)
}
