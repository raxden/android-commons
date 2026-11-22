package com.raxdenstudios.commons.permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.raxdenstudios.commons.android.ActivityHolder
import com.raxdenstudios.commons.permissions.model.Permission
import java.lang.ref.WeakReference
import java.util.WeakHashMap

@Suppress("TooManyFunctions")
class PermissionsManagerImpl : PermissionsManager {

    private val activityHolder: ActivityHolder = ActivityHolder()
    private var permissionResultLauncher: ActivityResultLauncher<Array<String>>? = null
    private var launcherOwner: WeakReference<LifecycleOwner>? = null
    private var pendingRequest: PendingRequest? = null

    // Dialogs launched whose result has not arrived yet. Overlapping launches each get a result (the one that
    // loses is cancelled with an empty map), so this tells a stale cancellation from the pending one's.
    private var outstandingLaunches: Int = 0

    override fun attach(
        activity: ComponentActivity,
    ) {
        activityHolder.attach(activity)
        activity.lifecycle.addObserver(this)
    }

    override fun onCreate(
        owner: LifecycleOwner,
    ) {
        super.onCreate(owner)

        registerPermissionsResultContract(owner)
    }

    override fun onDestroy(
        owner: LifecycleOwner,
    ) {
        if (launcherOwner?.get() === owner) {
            permissionResultLauncher = null
            launcherOwner = null
            // On a configuration change the recreated activity receives the result under the same key, so
            // the pending request is kept; otherwise nothing would ever deliver it.
            val isChangingConfigurations = (owner as? Activity)?.isChangingConfigurations == true
            if (!isChangingConfigurations) {
                outstandingLaunches = 0
                cancelPendingRequest()
            }
        }
        (owner as? ComponentActivity)?.let(activityHolder::detach)
        super.onDestroy(owner)
    }

    private fun registerPermissionsResultContract(
        owner: LifecycleOwner,
    ) {
        val activity = activityHolder.activity ?: error("You must call attach() before onCreate()")

        val registry = activity.activityResultRegistry
        permissionResultLauncher = registry.register(
            requestPermissionsKey(registry, this),
            owner,
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { result -> handleRegisterPermissionsResult(result) }
        launcherOwner = WeakReference(owner)
    }

    private fun handleRegisterPermissionsResult(
        result: Map<String, Boolean>,
    ) {
        val launchesBefore = outstandingLaunches
        outstandingLaunches = (launchesBefore - 1).coerceAtLeast(0)
        val request = pendingRequest ?: return
        val isAboutPendingRequest = request.permissions.any { permission -> permission.value in result }
        when {
            isAboutPendingRequest -> deliverResult(request, result)

            // The only launch was cancelled by the system: every permission is denied.
            result.isEmpty() && launchesBefore <= 1 -> deliverResult(request, result)

            // No launch is left that could still answer the pending request: deny it instead of waiting forever.
            outstandingLaunches == 0 -> deliverResult(request, emptyMap())

            // Result (or empty cancellation) of a launch that was already replaced: ignore it.
            else -> Unit
        }
    }

    private fun deliverResult(
        request: PendingRequest,
        result: Map<String, Boolean>,
    ) {
        pendingRequest = null
        request.permissions.forEach { permission ->
            when (result[permission.value]) {
                true -> request.callbacks.onGranted(permission)
                else -> request.callbacks.onDenied(permission)
            }
        }
    }

    override fun requestPermission(
        callbacks: PermissionsManager.Callbacks,
        vararg permissions: Permission,
    ) {
        requestPermission(callbacks, permissions.toList(), skipRationale = false)
    }

    override fun requestPermissionAfterRationale(
        callbacks: PermissionsManager.Callbacks,
        vararg permissions: Permission,
    ) {
        requestPermission(callbacks, permissions.toList(), skipRationale = true)
    }

    private fun requestPermission(
        callbacks: PermissionsManager.Callbacks,
        permissions: List<Permission>,
        skipRationale: Boolean,
    ) {
        val permissionsToRequest = permissions.filter { permission ->
            when {
                hasPermission(permission) -> {
                    callbacks.onGranted(permission)
                    false
                }

                !skipRationale && shouldShowRationale(permission) -> {
                    callbacks.onRationale(permission)
                    false
                }

                else -> true
            }
        }

        performRequestPermission(callbacks, permissionsToRequest)
    }

    private fun performRequestPermission(
        callbacks: PermissionsManager.Callbacks,
        permissions: List<Permission>,
    ) {
        if (permissions.isEmpty()) return

        val launcher = permissionResultLauncher
        if (launcher == null) {
            // Not attached or not created yet: nothing can show the dialog, so report a denial.
            permissions.forEach { permission -> callbacks.onDenied(permission) }
            return
        }
        // Only one request can wait for the dialog: end the previous one instead of leaving it waiting forever.
        cancelPendingRequest()
        pendingRequest = PendingRequest(callbacks, permissions)
        outstandingLaunches++
        val arrayPermissions = permissions.map { it.value }.toTypedArray()
        launcher.launch(arrayPermissions)
    }

    private fun cancelPendingRequest() {
        val request = pendingRequest ?: return
        pendingRequest = null
        request.permissions.forEach { permission -> request.callbacks.onDenied(permission) }
    }

    override fun shouldShowRationale(
        permission: Permission,
    ): Boolean = activityHolder.activity?.shouldShowRequestPermissionRationale(permission.value) ?: false

    override fun hasPermission(
        onGranted: (Boolean) -> Unit,
        permission: Permission,
    ) {
        onGranted(hasPermission(permission))
    }

    private fun hasPermission(
        permission: Permission,
    ) = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    private fun checkSelfPermission(
        permission: Permission,
    ) = activityHolder.activity?.let {
        ContextCompat.checkSelfPermission(it, permission.value)
    } ?: PackageManager.PERMISSION_DENIED

    /** A request whose system dialog is showing, waiting for its result. */
    private class PendingRequest(
        val callbacks: PermissionsManager.Callbacks,
        val permissions: List<Permission>,
    )

    private companion object {
        const val REQUEST_PERMISSIONS_KEY_PREFIX = "requestPermissions"

        // Managers registered on each activity registry, in order. Main thread only.
        val managersByRegistry =
            WeakHashMap<ActivityResultRegistry, MutableList<WeakReference<PermissionsManagerImpl>>>()

        /**
         * Key of [manager] on [registry]: its position among the managers registered on that activity. It is
         * unique per manager, stable if the same manager registers again, and, because managers register in
         * the same order every time, the same after recreation or process death, so the registry can hand a
         * restored result back to the same manager.
         */
        fun requestPermissionsKey(
            registry: ActivityResultRegistry,
            manager: PermissionsManagerImpl,
        ): String {
            val managers = managersByRegistry.getOrPut(registry) { mutableListOf() }
            var index = managers.indexOfFirst { reference -> reference.get() === manager }
            if (index == -1) {
                managers += WeakReference(manager)
                index = managers.lastIndex
            }
            return "$REQUEST_PERMISSIONS_KEY_PREFIX#$index"
        }
    }
}
