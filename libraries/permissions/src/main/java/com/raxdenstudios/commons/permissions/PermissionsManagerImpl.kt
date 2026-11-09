package com.raxdenstudios.commons.permissions

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.raxdenstudios.commons.android.ActivityHolder
import com.raxdenstudios.commons.permissions.model.Permission

@Suppress("TooManyFunctions")
class PermissionsManagerImpl : PermissionsManager {

    private val activityHolder: ActivityHolder = ActivityHolder()
    private var permissionResultLauncher: ActivityResultLauncher<Array<String>>? = null
    private var permissionsCallbacks: PermissionsManager.Callbacks = PermissionsManager.Callbacks()

    override fun attach(activity: ComponentActivity) {
        activityHolder.attach(activity)
        activity.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        registerPermissionsResultContract(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        activityHolder.activity?.run { activityHolder.detach(this) }
        super.onDestroy(owner)
    }

    private fun registerPermissionsResultContract(owner: LifecycleOwner) {
        val activity = activityHolder.activity ?: error("You must call attach() before onCreate()")

        permissionResultLauncher = activity.activityResultRegistry.register(
            REQUEST_PERMISSIONS_KEY,
            owner,
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { result -> handleRegisterPermissionsResult(result) }
    }

    private fun handleRegisterPermissionsResult(result: Map<String, Boolean>) {
        result.entries.forEach { entryPermissions ->
            val isGranted = entryPermissions.value
            val permission = Permission.fromValue(entryPermissions.key)
            when {
                isGranted -> permissionsCallbacks.onGranted(permission)
                else -> permissionsCallbacks.onDenied(permission)
            }
        }
    }

    override fun requestPermission(callbacks: PermissionsManager.Callbacks, vararg permissions: Permission) {
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
        if (permissions.isEmpty()) return

        val arrayPermissions = permissions.map { it.value }.toTypedArray()
        permissionResultLauncher?.launch(arrayPermissions)
    }

    private fun shouldShowRequestPermissionRationale(permission: Permission) =
        activityHolder.activity?.shouldShowRequestPermissionRationale(permission.value) ?: false

    override fun hasPermission(onGranted: (Boolean) -> Unit, permission: Permission) {
        onGranted(hasPermission(permission))
    }

    private fun hasPermission(permission: Permission) =
        checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    private fun checkSelfPermission(permission: Permission) = activityHolder.activity?.let {
        ContextCompat.checkSelfPermission(it, permission.value)
    } ?: PackageManager.PERMISSION_DENIED

    private companion object {
        const val REQUEST_PERMISSIONS_KEY = "requestPermissions"
    }
}
