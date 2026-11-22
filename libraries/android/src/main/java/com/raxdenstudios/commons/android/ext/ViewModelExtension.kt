package com.raxdenstudios.commons.android.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.commons.coroutines.ext.launch
import com.raxdenstudios.commons.coroutines.ext.safeLaunch
import kotlinx.coroutines.Job

/** Launches [block] on [viewModelScope], logging uncaught errors through the default coroutine logger. */
fun ViewModel.safeLaunch(
    block: suspend () -> Unit,
): Job = viewModelScope.safeLaunch { block() }

/** Launches [block] on [viewModelScope], logging uncaught errors and forwarding them to [onError]. */
fun ViewModel.launch(
    onError: (throwable: Throwable) -> Unit,
    block: suspend () -> Unit,
) {
    viewModelScope.launch(onError = onError) { block() }
}
