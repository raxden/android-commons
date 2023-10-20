package com.raxdenstudios.commons.coroutines

import kotlinx.coroutines.CoroutineScope

interface CoroutineScopeProvider {
    val main: CoroutineScope
    val io: CoroutineScope
    val default: CoroutineScope
}
