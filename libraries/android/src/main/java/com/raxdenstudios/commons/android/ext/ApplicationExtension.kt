package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.raxdenstudios.commons.android.util.ActivityLifecycleCallbacksAdapter

fun Application.initCompatVector() {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
}

/**
 * Registers activity lifecycle callbacks that call [action] for every activity created from now on
 * that is an [A] (an instance of [A] or of a subclass). Other activities are ignored, and activities
 * created before this call are not replayed.
 *
 * Returns the registered callbacks; pass them to [Application.unregisterActivityLifecycleCallbacks]
 * to stop. [action] runs on the main thread while the activity's `super.onCreate` runs, before the
 * rest of its `onCreate` (for example before `setContent`), so keep it short.
 *
 * ## Usage
 *
 * ```kotlin
 * class EdgeToEdgeInitializer : Initializer<Unit> {
 *
 *     override fun create(context: Context) {
 *         (context.applicationContext as? Application)
 *             ?.doOnActivityCreated<ComponentActivity> { activity -> activity.enableEdgeToEdge() }
 *     }
 *
 *     override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
 * }
 * ```
 *
 * @param A the activity type to match; subclasses of [A] match too.
 * @param action called with each newly created activity that is an [A].
 * @return the registered callbacks, to pass to [Application.unregisterActivityLifecycleCallbacks].
 */
inline fun <reified A : Activity> Application.doOnActivityCreated(
    crossinline action: (A) -> Unit,
): Application.ActivityLifecycleCallbacks {
    val callbacks = object : ActivityLifecycleCallbacksAdapter() {
        override fun onActivityCreated(
            activity: Activity,
            savedInstanceState: Bundle?,
        ) {
            if (activity is A) action(activity)
        }
    }
    registerActivityLifecycleCallbacks(callbacks)
    return callbacks
}
