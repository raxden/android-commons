package com.raxdenstudios.commons.android.util

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * [Application.ActivityLifecycleCallbacks] whose callbacks all do nothing, so a subclass only
 * overrides the ones it needs.
 *
 * The seven callbacks of [Application.ActivityLifecycleCallbacks] are abstract on every API level;
 * API 29 only added default `onActivityPre*` / `onActivityPost*` methods. This module's minSdk is
 * 28, and neither the platform nor androidx ship a public no-op implementation, hence this class.
 *
 * ## Usage
 *
 * ```kotlin
 * class ActivityHolderInitializer : Initializer<ActivityHolder> {
 *
 *     override fun create(context: Context): ActivityHolder {
 *         val holder = ActivityHolder()
 *         context.registerActivityLifecycleCallbacksOnApplication(
 *             object : ActivityLifecycleCallbacksAdapter() {
 *                 override fun onActivityResumed(activity: Activity) {
 *                     (activity as? ComponentActivity)?.let(holder::attach)
 *                 }
 *
 *                 override fun onActivityPaused(activity: Activity) {
 *                     (activity as? ComponentActivity)?.let(holder::detach)
 *                 }
 *             },
 *         )
 *         return holder
 *     }
 *
 *     override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
 * }
 * ```
 */
open class ActivityLifecycleCallbacksAdapter : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?,
    ) = Unit

    override fun onActivityStarted(
        activity: Activity,
    ) = Unit

    override fun onActivityResumed(
        activity: Activity,
    ) = Unit

    override fun onActivityPaused(
        activity: Activity,
    ) = Unit

    override fun onActivityStopped(
        activity: Activity,
    ) = Unit

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle,
    ) = Unit

    override fun onActivityDestroyed(
        activity: Activity,
    ) = Unit
}
