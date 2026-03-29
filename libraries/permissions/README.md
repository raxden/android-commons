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

- `attach(activity: ComponentActivity)` - Attach to activity lifecycle
- `requestPermission(callbacks: Callbacks, vararg permissions: Permission)` - Request permissions
- `hasPermission(onGranted: (Boolean) -> Unit, permission: Permission)` - Check permission status

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
