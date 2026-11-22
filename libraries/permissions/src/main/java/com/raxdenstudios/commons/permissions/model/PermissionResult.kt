package com.raxdenstudios.commons.permissions.model

/**
 * Outcome of requesting a [Permission], returned by the `requestPermission` extension.
 *
 * Each subtype carries the [permission] it refers to, so one handler can serve several requests.
 */
sealed class PermissionResult {

    /** The permission this result is about. */
    abstract val permission: Permission

    /** The permission is granted. */
    data class Granted(
        override val permission: Permission,
    ) : PermissionResult()

    /** The user denied the permission. */
    data class Denied(
        override val permission: Permission,
    ) : PermissionResult()

    /** The app should explain why it needs the permission before asking again. */
    data class Rationale(
        override val permission: Permission,
    ) : PermissionResult()
}
