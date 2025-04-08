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
    private lateinit var _permissionResultLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var _permissionsCallbacks: PermissionsManager.Callbacks

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

        _permissionResultLauncher = activity.activityResultRegistry.register(
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
                isGranted -> _permissionsCallbacks.onGranted(permission)
                else -> _permissionsCallbacks.onDenied(permission)
            }
        }
    }

    override fun requestPermission(
        callbacks: PermissionsManager.Callbacks,
        vararg permissions: Permission
    ) {
        _permissionsCallbacks = callbacks

        val permissionsToRequest = permissions.filter { permission ->
            when {
                hasPermission(permission) -> {
                    _permissionsCallbacks.onGranted(permission)
                    false
                }

                shouldShowRequestPermissionRationale(permission) -> {
                    _permissionsCallbacks.onRationale(permission)
                    false
                }

                else -> true
            }
        }.toList()

        performRequestPermission(permissionsToRequest)
    }

    private fun performRequestPermission(permissions: List<Permission>) {
        val arrayPermissions = permissions.map { it.value }.toTypedArray()
        if (this::_permissionResultLauncher.isInitialized) {
            _permissionResultLauncher.launch(arrayPermissions)
        }
    }

    private fun shouldShowRequestPermissionRationale(permission: Permission) =
        activityHolder.activity?.shouldShowRequestPermissionRationale(permission.value) ?: false

    override fun hasPermission(
        onGranted: (Boolean) -> Unit,
        permission: Permission
    ) {
        onGranted(hasPermission(permission))
    }

    private fun hasPermission(permission: Permission) =
        checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    private fun checkSelfPermission(permission: Permission) =
        activityHolder.activity?.let {
            ContextCompat.checkSelfPermission(it, permission.value)
        } ?: PackageManager.PERMISSION_DENIED

    private companion object {
        const val REQUEST_PERMISSIONS_KEY = "requestPermissions"
    }
}
