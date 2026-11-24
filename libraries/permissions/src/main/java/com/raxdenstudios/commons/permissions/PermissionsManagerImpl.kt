package com.raxdenstudios.commons.permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import com.raxdenstudios.commons.permissions.model.Permission
import java.lang.ref.WeakReference
import java.util.UUID
import java.util.WeakHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Default [PermissionsManager].
 *
 * Several activities can be attached at once. They are kept as a stack: requests, [shouldShowRationale] and
 * [hasPermission] use the most recently attached activity that is still alive, so when the one on top is
 * destroyed the manager falls back to the one below. Each attached activity registers its own result launcher
 * when it is created.
 *
 * An activity destroyed by a configuration change keeps its place in the stack, and the request it launched
 * stays pending, until its new instance is created: the new instance is matched through an id kept in the
 * activity's saved state, so only the recreated activity reclaims it. When any other attached activity is
 * created first, the old place is dropped and its pending request is denied, since the recreated instance is
 * always created right after the old one is destroyed.
 *
 * @param context optional; when given (e.g. the application context from DI), [hasPermission] answers
 * correctly with no activity attached, as when a service, worker or FCM message runs in the background.
 * Without it, the application context of the first attached activity is used once one has been attached.
 */
@Suppress("TooManyFunctions")
class PermissionsManagerImpl(
    context: Context? = null,
) : PermissionsManager {

    // Attached activities, oldest first. Changed on the main thread only; copy-on-write so hasPermission() can
    // iterate it from any thread.
    private val attachedActivities = CopyOnWriteArrayList<AttachedActivity>()

    // Written on the main thread by attach(), read from any thread by hasPermission().
    @Volatile
    private var applicationContext: Context? = context?.applicationContext
    private var pendingRequest: PendingRequest? = null

    override fun attach(
        activity: ComponentActivity,
    ) {
        attachedActivities.filter { attached -> attached.isCollected() }.forEach(::remove)
        if (attachedActivities.none { attached -> attached.activity === activity }) {
            val attached = AttachedActivity(activity, savedStateKey(activity))
            attachedActivities += attached
            activity.savedStateRegistry.unregisterSavedStateProvider(attached.savedStateKey)
            activity.savedStateRegistry.registerSavedStateProvider(attached.savedStateKey) {
                bundleOf(ENTRY_ID_KEY to attached.id)
            }
        }
        if (applicationContext == null) applicationContext = activity.applicationContext
        activity.lifecycle.addObserver(this)
    }

    override fun onCreate(
        owner: LifecycleOwner,
    ) {
        super.onCreate(owner)

        check(attachedActivities.isNotEmpty()) { "You must call attach() before onCreate()" }
        // Lifecycles of owners that are not attached activities are ignored: each launcher belongs to its activity.
        val attached = attachedActivities.firstOrNull { attached -> attached.activity === owner } ?: return
        val activity = attached.activity ?: return
        if (!attached.isResolved) resolve(attached, activity)
        registerPermissionsResultContract(attached, activity)
    }

    /**
     * Runs once per entry, when its activity is created and its saved state is restored. An activity recreated
     * after a configuration change finds the id of its previous entry in its saved state and takes that entry's
     * place, keeping its position in the stack, its launches and the request it launched. Any other activity
     * stays a new entry on top, and every entry still waiting for its activity to be recreated is dropped: a
     * recreation creates the new instance right after destroying the old one, so they will not come back.
     */
    private fun resolve(
        attached: AttachedActivity,
        activity: ComponentActivity,
    ) {
        attached.isResolved = true
        val savedStateRegistry = activity.savedStateRegistry
        val restoredId = if (savedStateRegistry.isRestored) {
            savedStateRegistry.consumeRestoredStateForKey(attached.savedStateKey)?.getString(ENTRY_ID_KEY)
        } else {
            null
        }
        val previous = attachedActivities.firstOrNull { other ->
            other !== attached && other.isRecreating && other.id == restoredId
        }
        if (previous != null) {
            reclaim(attached, previous)
        } else {
            attachedActivities.filter { other -> other !== attached && other.isRecreating }.forEach(::remove)
        }
    }

    /** [attached] takes the place of [previous], the entry of the activity instance it replaces. */
    private fun reclaim(
        attached: AttachedActivity,
        previous: AttachedActivity,
    ) {
        attached.id = previous.id
        attached.outstandingLaunches = previous.outstandingLaunches
        pendingRequest?.takeIf { request -> request.attached === previous }?.attached = attached
        attachedActivities.remove(attached)
        attachedActivities[attachedActivities.indexOf(previous)] = attached
    }

    override fun onDestroy(
        owner: LifecycleOwner,
    ) {
        attachedActivities
            .firstOrNull { attached -> attached.activity === owner }
            ?.let { attached -> detach(attached, owner) }
        super.onDestroy(owner)
    }

    private fun detach(
        attached: AttachedActivity,
        owner: LifecycleOwner,
    ) {
        (owner as? ComponentActivity)?.savedStateRegistry?.unregisterSavedStateProvider(attached.savedStateKey)
        // On a configuration change the recreated activity receives the result under the same key, so the
        // pending request is kept; otherwise nothing would ever deliver it.
        if ((owner as? Activity)?.isChangingConfigurations == true) attached.markRecreating() else remove(attached)
    }

    /** Drops [attached]: no launch from it can answer anymore, so the pending request it launched is denied. */
    private fun remove(
        attached: AttachedActivity,
    ) {
        attachedActivities.remove(attached)
        attached.launcher = null
        attached.outstandingLaunches = 0
        if (pendingRequest?.attached === attached) cancelPendingRequest()
    }

    private fun registerPermissionsResultContract(
        attached: AttachedActivity,
        activity: ComponentActivity,
    ) {
        val registry = activity.activityResultRegistry
        attached.launcher = registry.register(
            requestPermissionsKey(registry, this),
            activity,
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { result -> handleRegisterPermissionsResult(attached, result) }
    }

    private fun savedStateKey(
        activity: ComponentActivity,
    ): String = "$SAVED_STATE_KEY_PREFIX:${requestPermissionsKey(activity.activityResultRegistry, this)}"

    private fun handleRegisterPermissionsResult(
        attached: AttachedActivity,
        result: Map<String, Boolean>,
    ) {
        val launchesBefore = attached.outstandingLaunches
        attached.outstandingLaunches = (launchesBefore - 1).coerceAtLeast(0)
        // A result from another activity than the one that launched the pending request belongs to a launch
        // that was already replaced: ignore it.
        val request = pendingRequest?.takeIf { request -> request.attached === attached } ?: return
        val isAboutPendingRequest = request.permissions.any { permission -> permission.value in result }
        when {
            isAboutPendingRequest -> deliverResult(request, result)

            // The only launch was cancelled by the system: every permission is denied.
            result.isEmpty() && launchesBefore <= 1 -> deliverResult(request, result)

            // No launch is left that could still answer the pending request: deny it instead of waiting forever.
            attached.outstandingLaunches == 0 -> deliverResult(request, emptyMap())

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

        val attached = topAttachedActivity()
        val launcher = attached?.launcher
        if (attached == null || launcher == null) {
            // Not attached or not created yet: nothing can show the dialog, so report a denial.
            permissions.forEach { permission -> callbacks.onDenied(permission) }
            return
        }
        // Only one request can wait for the dialog: end the previous one instead of leaving it waiting forever.
        cancelPendingRequest()
        pendingRequest = PendingRequest(callbacks, permissions, attached)
        attached.outstandingLaunches++
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
    ): Boolean = topActivity()?.shouldShowRequestPermissionRationale(permission.value) ?: false

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
    ) = (topActivity() ?: applicationContext)?.let { context ->
        ContextCompat.checkSelfPermission(context, permission.value)
    } ?: PackageManager.PERMISSION_DENIED

    /** The most recently attached activity still alive. Safe from any thread. */
    private fun topActivity(): ComponentActivity? {
        var top: ComponentActivity? = null
        for (attached in attachedActivities) attached.aliveActivity?.let { activity -> top = activity }
        return top
    }

    /** The entry of [topActivity]. Main thread only. */
    private fun topAttachedActivity(): AttachedActivity? = attachedActivities.lastOrNull { attached ->
        attached.aliveActivity != null
    }

    /** A request whose system dialog is showing on [attached], waiting for its result. */
    private class PendingRequest(
        val callbacks: PermissionsManager.Callbacks,
        val permissions: List<Permission>,
        var attached: AttachedActivity,
    )

    /**
     * An attached activity and the result launcher it registered when it was created. While the activity is
     * recreated after a configuration change, the entry stays in the stack with no activity, waiting for the new
     * instance to claim it through [id], which is kept in the activity's saved state under [savedStateKey].
     */
    private class AttachedActivity(
        activity: ComponentActivity,
        val savedStateKey: String,
    ) {

        var id: String = UUID.randomUUID().toString()

        // Read from any thread by hasPermission().
        @Volatile
        private var reference: WeakReference<ComponentActivity>? = WeakReference(activity)

        var isRecreating: Boolean = false
            private set

        // Whether the entry already looked for the entry of the instance its activity replaces.
        var isResolved: Boolean = false

        var launcher: ActivityResultLauncher<Array<String>>? = null

        // Dialogs launched from this activity whose result has not arrived yet. Overlapping launches each get a
        // result (the one that loses is cancelled with an empty map), so this tells a stale cancellation from
        // the pending one's.
        var outstandingLaunches: Int = 0

        val activity: ComponentActivity?
            get() = reference?.get()

        val aliveActivity: ComponentActivity?
            get() = activity?.takeUnless { activity -> activity.isDestroyed }

        /** Its activity was garbage collected without being destroyed through the lifecycle. */
        fun isCollected(): Boolean = !isRecreating && activity == null

        fun markRecreating() {
            isRecreating = true
            reference = null
            launcher = null
        }
    }

    private companion object {
        const val REQUEST_PERMISSIONS_KEY_PREFIX = "requestPermissions"
        const val SAVED_STATE_KEY_PREFIX = "com.raxdenstudios.commons.permissions"
        const val ENTRY_ID_KEY = "entryId"

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
