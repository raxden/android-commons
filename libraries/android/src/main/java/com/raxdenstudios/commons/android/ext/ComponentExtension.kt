package com.raxdenstudios.commons.android.ext

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle

fun ComponentActivity.isAtLeastCreated() = lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)

fun ComponentActivity.isAtLeastStarted() = lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

fun ComponentActivity.isAtLeastResumed() = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
