package com.raxdenstudios.commons.permissions.ext

import com.raxdenstudios.commons.permissions.PermissionsManager
import com.raxdenstudios.commons.permissions.model.Permission
import com.raxdenstudios.commons.permissions.model.PermissionResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Requests [permission] and suspends until the outcome is known, then returns it as a [PermissionResult].
 *
 * If the permission is already granted, returns [PermissionResult.Granted] without showing a dialog. If the
 * system says a rationale is needed, returns [PermissionResult.Rationale] without showing the system dialog:
 * the caller explains the permission and then calls [requestPermissionAfterRationale] to show the dialog.
 * Otherwise the system dialog is shown.
 *
 * If the manager is not attached and created yet, returns [PermissionResult.Denied] instead of showing the
 * dialog. Cancelling the calling coroutine stops the wait. Starting another request that shows the dialog
 * while this one is still waiting resumes this one with [PermissionResult.Denied], as does destroying the
 * activity for a reason other than a configuration change.
 *
 * ## Usage
 *
 * ```kotlin
 * when (permissionsManager.requestPermission(Permission.RecordAudio)) {
 *     is PermissionResult.Granted -> startRecording()
 *     is PermissionResult.Rationale -> showRationale()
 *     is PermissionResult.Denied -> showDeniedMessage()
 * }
 * ```
 */
suspend fun PermissionsManager.requestPermission(
    permission: Permission,
): PermissionResult = awaitPermissionResult(permission) { callbacks ->
    requestPermission(callbacks, permission)
}

/**
 * Like [requestPermission], but shows the system dialog even when a rationale should be shown, so it never
 * returns [PermissionResult.Rationale]. Call it once the user has seen the rationale.
 *
 * ## Usage
 *
 * ```kotlin
 * // From the rationale dialog's "continue" button:
 * val result = permissionsManager.requestPermissionAfterRationale(Permission.RecordAudio)
 * ```
 */
suspend fun PermissionsManager.requestPermissionAfterRationale(
    permission: Permission,
): PermissionResult = awaitPermissionResult(permission) { callbacks ->
    requestPermissionAfterRationale(callbacks, permission)
}

private suspend fun awaitPermissionResult(
    permission: Permission,
    request: (PermissionsManager.Callbacks) -> Unit,
): PermissionResult = suspendCancellableCoroutine { continuation ->
    val resume: (PermissionResult) -> Unit = { result ->
        if (continuation.isActive) continuation.resume(result)
    }
    request(
        PermissionsManager.Callbacks(
            onGranted = { resume(PermissionResult.Granted(permission)) },
            onRationale = { resume(PermissionResult.Rationale(permission)) },
            onDenied = { resume(PermissionResult.Denied(permission)) },
        ),
    )
}

/**
 * Whether [permission] is currently granted, checked with the attached activity or else the manager's
 * application context. Returns `false` when neither is available. Does not request the permission.
 */
suspend fun PermissionsManager.hasPermission(
    permission: Permission,
): Boolean = suspendCancellableCoroutine { continuation ->
    hasPermission(
        onGranted = { isGranted -> if (continuation.isActive) continuation.resume(isGranted) },
        permission = permission,
    )
}
