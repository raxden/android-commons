package com.raxdenstudios.commons.android.ext

import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager

/**
 * Enqueues a one-time request of [W] as unique work named [uniqueName], resolving conflicts with [policy].
 *
 * [uniqueName] defaults to the worker class name, so one unique chain exists per worker type. The default
 * [ExistingWorkPolicy.KEEP] keeps pending work instead of replacing it; use `REPLACE` to restart it.
 * [configure] applies extra settings such as constraints or input data to the request builder.
 *
 * ## Usage
 *
 * ```kotlin
 * WorkManager.getInstance(context).enqueueUniqueOneTime<SyncWorker>(
 *     policy = ExistingWorkPolicy.REPLACE,
 * ) {
 *     setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
 * }
 * ```
 */
inline fun <reified W : ListenableWorker> WorkManager.enqueueUniqueOneTime(
    policy: ExistingWorkPolicy = ExistingWorkPolicy.KEEP,
    uniqueName: String = W::class.java.name,
    configure: OneTimeWorkRequest.Builder.() -> Unit = {},
): Operation = enqueueUniqueWork(
    uniqueName,
    policy,
    OneTimeWorkRequestBuilder<W>().apply(configure).build(),
)
