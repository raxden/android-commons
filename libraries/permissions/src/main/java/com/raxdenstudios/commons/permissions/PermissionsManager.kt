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
     * right away when no launcher is registered (not attached or not created yet). A new request replaces
     * the callbacks of one still pending.
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
