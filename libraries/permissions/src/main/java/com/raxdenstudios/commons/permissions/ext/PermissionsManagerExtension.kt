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
 * the caller explains the permission and calls this again. Otherwise the system dialog is shown.
 *
 * If the manager is not attached and created yet, returns [PermissionResult.Denied] instead of showing the
 * dialog. Cancelling the calling coroutine stops the wait. Calling this again while a previous request is
 * still pending replaces its callbacks, so the earlier call may never resume; do not run two requests at once.
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
): PermissionResult = suspendCancellableCoroutine { continuation ->
    val resume: (PermissionResult) -> Unit = { result ->
        if (continuation.isActive) continuation.resume(result)
    }
    requestPermission(
        PermissionsManager.Callbacks(
            onGranted = { resume(PermissionResult.Granted(permission)) },
            onRationale = { resume(PermissionResult.Rationale(permission)) },
            onDenied = { resume(PermissionResult.Denied(permission)) },
        ),
        permission,
    )
}

/**
 * Whether [permission] is currently granted to the attached activity. Returns `false` when no activity is
 * attached. Does not request the permission.
 */
suspend fun PermissionsManager.hasPermission(
    permission: Permission,
): Boolean = suspendCancellableCoroutine { continuation ->
    hasPermission(
        onGranted = { isGranted -> if (continuation.isActive) continuation.resume(isGranted) },
        permission = permission,
    )
}
