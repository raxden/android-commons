package com.raxdenstudios.commons.permissions

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.raxdenstudios.commons.permissions.model.Permission

class PermissionsManagerImpl(
    private val activity: ComponentActivity
) : PermissionsManager {

    private val registry: ActivityResultRegistry
        get() = activity.activityResultRegistry

    private lateinit var permissionResultLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var permissionsCallbacks: PermissionsManager.Callbacks

    init {
        activity.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        registerPermissionsResultContract(owner)
    }

    private fun registerPermissionsResultContract(owner: LifecycleOwner) {
        permissionResultLauncher = registry.register(
            REQUEST_PERMISSIONS_KEY,
            owner,
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result -> handleRegisterPermissionsResult(result) }
    }

    private fun handleRegisterPermissionsResult(
        result: Map<String, Boolean>
    ) {
        result.entries.map { entryPermissions ->
            val isGranted = entryPermissions.value
            val permission = Permission.fromValue(entryPermissions.key)
            when {
                isGranted -> permissionsCallbacks.onGranted(permission)
                else -> permissionsCallbacks.onDenied(permission)
            }
        }
    }

    override fun requestPermission(
        callbacks: PermissionsManager.Callbacks,
        vararg permissions: Permission
    ) {
        permissionsCallbacks = callbacks

        val permissionsToRequest = permissions.filter { permission ->
            when {
                hasPermission(permission) -> {
                    permissionsCallbacks.onGranted(permission)
                    false
                }

                shouldShowRequestPermissionRationale(permission) -> {
                    permissionsCallbacks.onRationale(permission)
                    false
                }

                else -> true
            }
        }.toList()

        performRequestPermission(permissionsToRequest)
    }

    private fun performRequestPermission(permissions: List<Permission>) {
        val arrayPermissions = permissions.map { it.value }.toTypedArray()
        if (this::permissionResultLauncher.isInitialized) {
            permissionResultLauncher.launch(arrayPermissions)
        }
    }

    private fun shouldShowRequestPermissionRationale(permission: Permission) =
        activity.shouldShowRequestPermissionRationale(permission.value)

    override fun hasPermission(
        onGranted: (Boolean) -> Unit,
        permission: Permission
    ) {
        onGranted(hasPermission(permission))
    }

    private fun hasPermission(permission: Permission) =
        checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    private fun checkSelfPermission(permission: Permission) =
        ContextCompat.checkSelfPermission(activity, permission.value)

    private companion object {
        const val REQUEST_PERMISSIONS_KEY = "requestPermissions"
    }
}
