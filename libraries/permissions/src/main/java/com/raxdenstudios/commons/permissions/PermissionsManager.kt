package com.raxdenstudios.commons.permissions

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import com.raxdenstudios.commons.permissions.model.Permission

/**
 * Requests runtime permissions for a [ComponentActivity] and reports the outcome through [Callbacks].
 *
 * Call [attach] early in the activity's `onCreate`, before any request: the result launcher is registered
 * from the lifecycle `onCreate` callback. Requests that need the system dialog before that report
 * [Callbacks.onDenied]. Results are delivered on the main thread.
 *
 * ## Setup
 *
 * ```kotlin
 * class MainActivity : ComponentActivity() {
 *     private val permissionsManager: PermissionsManager = PermissionsManagerImpl()
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         permissionsManager.attach(this)
 *     }
 * }
 * ```
 *
 * ## Usage
 *
 * ```kotlin
 * permissionsManager.requestPermission(
 *     callbacks = PermissionsManager.Callbacks(onGranted = { startCamera() }, onRationale = { explain() }),
 *     Permission.Camera,
 * )
 * ```
 */
interface PermissionsManager : DefaultLifecycleObserver {

    /**
     * Attach activity
     *
     * @param activity
     */
    fun attach(
        activity: ComponentActivity,
    )

    /**
     * Request permission. Granted permissions call [Callbacks.onGranted] and those needing a rationale call
     * [Callbacks.onRationale], without a dialog. The rest show the system dialog, or call [Callbacks.onDenied]
     * right away when no launcher is registered (not attached or not created yet). A new request that shows
     * the dialog while another is still pending ends the pending one with [Callbacks.onDenied].
     *
     * A request still waiting for the dialog is kept across a configuration change, so the recreated
     * activity delivers its result: keep [callbacks] free of references to the activity (route them through a
     * ViewModel, for example), or the old activity is retained until the result arrives.
     *
     * After showing the rationale, call [requestPermissionAfterRationale] to show the system dialog: calling
     * this again would only report [Callbacks.onRationale] again.
     *
     * @param callbacks
     * @param permissions
     */
    fun requestPermission(
        callbacks: Callbacks,
        vararg permissions: Permission,
    )

    /**
     * Like [requestPermission], but shows the system dialog even when a rationale should be shown. Call it
     * once the user has seen the rationale. Granted permissions still call [Callbacks.onGranted] without a
     * dialog.
     *
     * The default implementation delegates to [requestPermission], so implementations written before this
     * method existed keep compiling; [PermissionsManagerImpl] overrides it.
     */
    fun requestPermissionAfterRationale(
        callbacks: Callbacks,
        vararg permissions: Permission,
    ) {
        requestPermission(callbacks, *permissions)
    }

    /**
     * Reports whether [permission] is granted, without requesting it. The check uses the attached activity when
     * there is one, otherwise the application context: the one given to the manager, or else the application
     * context of the first attached activity. With no activity and no context it reports `false`.
     *
     * [onGranted] is called synchronously on the calling thread, before this function returns. When the manager
     * was created with a context, this is safe from background callers (services, workers, FCM messages) with no
     * activity attached. Without a context, it reports `false` until an activity has been attached once.
     *
     * @param onGranted receives `true` when the permission is granted.
     * @param permission the permission to check.
     */
    fun hasPermission(
        onGranted: (Boolean) -> Unit,
        permission: Permission,
    )

    /**
     * Whether the attached activity should explain [permission] before asking for it again. Returns `false`
     * when no activity is attached. Permissions never requested, or denied with "don't ask again", also return
     * `false`, so this does not tell a first request apart from a permanent denial.
     */
    fun shouldShowRationale(
        permission: Permission,
    ): Boolean

    /**
     * Callbacks
     *
     * @property onGranted
     * @property onRationale
     * @property onDenied
     * @constructor Create empty Callbacks
     */
    class Callbacks(
        val onGranted: (Permission) -> Unit = {},
        val onRationale: (Permission) -> Unit = {},
        val onDenied: (Permission) -> Unit = {},
    )
}
