# Permissions Module

Runtime permissions management for Android with lifecycle-aware handling.

## 📦 Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-permissions")
}
```

## 🚀 Usage

### Basic Setup

```kotlin
class MainActivity : ComponentActivity() {
    private val permissionsManager = PermissionsManagerImpl()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsManager.attach(this)
    }
}
```

### Several activities

Attach every activity that may ask for permissions, each in its own `onCreate`. The manager keeps them as a stack and each one registers its own result launcher when it is created. Requests, `shouldShowRationale` and `hasPermission` use the most recently attached activity that is still alive: when the one on top is destroyed (for example a full-screen alarm screen shown over the main activity), the manager falls back to the one below. Share one manager (a singleton from DI) for this:

```kotlin
class AlarmActivity : ComponentActivity() {
    private val permissionsManager: PermissionsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsManager.attach(this)
    }
}
```

A request waiting for the dialog belongs to the activity that showed it: it is denied when that activity is destroyed for good, and kept when it is recreated after a configuration change (the new instance keeps its place in the stack). Destroying another activity does not affect it. If the activity on top is attached but not created yet, requests report `onDenied` without showing the dialog, even when an activity below could show it.

The recreated instance is matched through an id the manager keeps in the activity's saved state, so only the activity that was recreated takes the old place back. Any other activity created meanwhile (a deep link or a notification tap, for example) becomes a new entry on top. Because Android creates the new instance right after destroying the old one, the old place is dropped, and its pending request denied, as soon as another attached activity is created before it comes back.

### Notifications

Apps can use the manager instead of their own notification permission launcher:

```kotlin
val canPost = when {
    context.areNotificationsEnabled() -> true
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> false // no runtime permission: open IntentType.NotificationSettings
    else -> {
        permissionsManager.requestPermissionAfterRationale(Permission.PostNotifications)
        context.areNotificationsEnabled()
    }
}
```

- Below API 33 `POST_NOTIFICATIONS` is not a runtime permission: do not request it, check `areNotificationsEnabled()` and send the user to the notification settings.
- `Context.areNotificationsEnabled()` (`commons-android`) is the source of truth after the dialog. `Granted` only reflects the permission, not whether the user turned notifications off for the app or a channel.
- `Denied` does not tell apart no attached activity, an activity not created yet, the user saying no, or a permanent block. Use `shouldShowRationale` before asking, and the settings screen when the dialog no longer shows.


### Request Single Permission

```kotlin
permissionsManager.requestPermission(
    callbacks = PermissionsManager.Callbacks(
        onGranted = { permission -> 
            // Permission granted
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        },
        onDenied = { permission -> 
            // Permission denied
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        },
        onRationale = { permission ->
            // Show rationale to user
            showRationaleDialog(permission)
        }
    ),
    Permission.Camera
)
```

### Request Multiple Permissions

```kotlin
permissionsManager.requestPermission(
    callbacks = PermissionsManager.Callbacks(
        onGranted = { permission -> 
            handleGrantedPermission(permission)
        },
        onDenied = { permission -> 
            handleDeniedPermission(permission)
        }
    ),
    Permission.Camera,
    Permission.AccessFineLocation,
    Permission.RecordAudio
)
```

### Check Permission Status

```kotlin
permissionsManager.hasPermission(
    onGranted = { isGranted ->
        if (isGranted) {
            // Permission is granted
            startCamera()
        } else {
            // Permission not granted
            requestCameraPermission()
        }
    },
    permission = Permission.Camera
)
```

`hasPermission` works without an attached activity when the manager was given a context (see [Background use](#background-use)). It checks the most recently attached activity still alive first, then the application context, and reports `false` when neither is available.

### Background use

Pass a `Context` to `PermissionsManagerImpl` to check permissions when no activity is attached, for example from a service, worker or FCM message. Use the application context so the manager does not keep an activity alive:

```kotlin
// Manual construction
val permissionsManager: PermissionsManager = PermissionsManagerImpl(applicationContext)
```

```kotlin
// Koin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val permissionsModule = module {
    single { PermissionsManagerImpl(androidContext()) } bind PermissionsManager::class
}
```

Requests and `shouldShowRationale` still need an attached activity: without one, requests report `onDenied` (or `PermissionResult.Denied`) and `shouldShowRationale` returns `false`.

### Suspending API

`requestPermission(permission)` and `hasPermission(permission)` are `suspend` extensions in `com.raxdenstudios.commons.permissions.ext`. The request returns a `PermissionResult`:

```kotlin
when (val result = permissionsManager.requestPermission(Permission.RecordAudio)) {
    is PermissionResult.Granted -> startRecording()
    is PermissionResult.Rationale -> showRationaleDialog(result.permission)
    is PermissionResult.Denied -> showDeniedMessage()
}
```

`Rationale` is returned without showing the system dialog. Explain the permission, then call `requestPermissionAfterRationale` to show it (calling `requestPermission` again would only return `Rationale` again). Starting a new request while another is waiting for the dialog ends the earlier one with `Denied`.

### Rationale check

`shouldShowRationale(permission)` tells whether the most recently attached activity still alive should explain the permission before asking again. It returns `false` when no activity is attached, even if the manager has a context: unlike `hasPermission`, it has no application-context fallback.

`shouldShowRationale` is a new `PermissionsManager` interface member: custom implementations of the interface must add it (source-breaking).

A request made before `attach` and the activity's `onCreate` reports `onDenied` (or `PermissionResult.Denied`) instead of waiting forever.

## 📋 Available Permissions

### Predefined Permissions

- `Permission.Camera` - Camera access
- `Permission.AccessFineLocation` - Fine location
- `Permission.AccessCoarseLocation` - Coarse location
- `Permission.ReadContacts` - Read contacts
- `Permission.WriteContacts` - Write contacts
- `Permission.RecordAudio` - Record audio
- `Permission.CallPhone` - Make phone calls
- `Permission.ReadExternalStorage` - Read external storage
- `Permission.WriteExternalStorage` - Write external storage

### Custom Permissions

```kotlin
val customPermission = Permission.Other("android.permission.CUSTOM_PERMISSION")
```

## ✨ Features

- ✅ Lifecycle-aware permission handling
- ✅ Multiple permissions support
- ✅ Rationale callback for better UX
- ✅ Type-safe permission definitions
- ✅ Automatic permission status checking
- ✅ Easy integration with ComponentActivity

## 📖 API Reference

### PermissionsManager

#### Methods

- `PermissionsManagerImpl(context: Context? = null)` - Default implementation; the optional context enables `hasPermission` without an attached activity
- `attach(activity: ComponentActivity)` - Attach to activity lifecycle; several activities can be attached, the most recent one alive is used
- `requestPermission(callbacks: Callbacks, vararg permissions: Permission)` - Request permissions (needs an attached activity)
- `hasPermission(onGranted: (Boolean) -> Unit, permission: Permission)` - Check permission status (attached activity, then application context, else `false`)

### Callbacks

```kotlin
PermissionsManager.Callbacks(
    onGranted: (Permission) -> Unit = {},
    onRationale: (Permission) -> Unit = {},
    onDenied: (Permission) -> Unit = {}
)
```

## 💡 Best Practices

### Show Rationale

```kotlin
private fun showRationaleDialog(permission: Permission) {
    AlertDialog.Builder(this)
        .setTitle("Permission Required")
        .setMessage("This permission is needed to...")
        .setPositiveButton("Grant") { _, _ ->
            // Request permission again
            permissionsManager.requestPermission(callbacks, permission)
        }
        .setNegativeButton("Cancel", null)
        .show()
}
```

### Handle Already Granted Permissions

The library automatically checks if permissions are already granted before requesting them, calling `onGranted` immediately if they are.

## 🧪 Testing

```bash
./gradlew :libraries:permissions:testDebugUnitTest
```
