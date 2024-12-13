package com.raxdenstudios.commons.permissions

import androidx.lifecycle.DefaultLifecycleObserver
import com.raxdenstudios.commons.permissions.model.Permission

interface PermissionsManager: DefaultLifecycleObserver {

    /**
     * Request permission
     *
     * @param callbacks
     * @param permissions
     */
    fun requestPermission(
        callbacks: Callbacks,
        vararg permissions: Permission,
    )

    /**
     * Has permission
     *
     * @param onGranted
     * @param permission
     * @receiver
     */
    fun hasPermission(
        onGranted: (Boolean) -> Unit,
        permission: Permission
    )

    /**
     * Callbacks
     *
     * @property onGranted
     * @property onRationale
     * @property onDenied
     * @constructor Create empty Callbacks
     */
    open class Callbacks(
        open val onGranted: (Permission) -> Unit = {},
        open val onRationale: (Permission) -> Unit = {},
        open val onDenied: (Permission) -> Unit = {}
    )
}
