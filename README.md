# Android Commons

[![Continuous Delivery](https://github.com/raxden/android-commons/workflows/Continuous%20Delivery/badge.svg)](https://github.com/raxden/android-commons/actions/workflows/deploy_library.yml)
[![codecov](https://codecov.io/gh/raxden/android-commons/branch/master/graph/badge.svg?token=E55S5DHJ9B)](https://codecov.io/gh/raxden/android-commons)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.raxdenstudios/commons-android/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.raxdenstudios/commons-android)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A comprehensive collection of modular Android libraries providing utilities, extensions, and common functionality for modern Android development.

## 📋 Overview

Android Commons is a curated set of libraries designed to accelerate Android development by providing reusable components and utilities. Each module is independent, allowing you to include only what you need, keeping your app size minimal.

### ✨ Key Features

- 🎯 **Modular Architecture** - Include only the modules you need
- 🚀 **Modern Android** - Built with Kotlin, Coroutines, and Jetpack Compose
- 🧪 **Well Tested** - Comprehensive unit test coverage
- 📦 **Small Footprint** - Minimal dependencies per module
- 🔄 **Active Maintenance** - Regular updates and improvements

## 📚 Available Modules

| Module | Description | Latest Version |
|--------|-------------|----------------|
| **Core & Android** |
| `commons-core` | Core utilities and extensions | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-core.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-core) |
| `commons-android` | Android-specific utilities and extensions | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-android.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-android) |
| `commons-android-binding` | View binding utilities | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-android-binding.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-android-binding) |
| `commons-android-compose` | Jetpack Compose utilities | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-android-compose.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-android-compose) |
| `commons-android-test` | Android testing utilities | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-android-test.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-android-test) |
| **Async & Concurrency** |
| `commons-coroutines` | Kotlin Coroutines extensions | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-coroutines.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-coroutines) |
| `commons-coroutines-test` | Coroutines testing utilities | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-coroutines-test.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-coroutines-test) |
| **Networking** |
| `commons-network` | Network utilities and interceptors | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-network.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-network) |
| **Pagination** |
| `commons-pagination` | Base pagination framework | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-pagination.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-pagination) |
| `commons-pagination-co` | Coroutines-based pagination | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-pagination-co.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-pagination-co) |
| **Permissions** |
| `commons-permissions` | Runtime permissions management | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-permissions.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-permissions) |
| **Storage** |
| `commons-preferences` | Advanced SharedPreferences wrapper | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-preferences.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-preferences) |
| **Date & Time** |
| `commons-threeten` | ThreeTen date/time utilities | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-threeten.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-threeten) |
| `commons-threeten-test` | ThreeTen testing utilities | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-threeten-test.svg)](https://search.maven.org/artifact/com.raxdenstudios/commons-threeten-test) |

## 🚀 Getting Started

### Requirements

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)
- **Kotlin**: 1.9+
- **Java**: 17

### Installation

Add the desired modules to your `build.gradle.kts`:

```kotlin
dependencies {
    // Core modules
    implementation("com.raxdenstudios:commons-core:x.y.z")
    implementation("com.raxdenstudios:commons-android:x.y.z")
    
    // Coroutines support
    implementation("com.raxdenstudios:commons-coroutines:x.y.z")
    testImplementation("com.raxdenstudios:commons-coroutines-test:x.y.z")
    
    // Networking
    implementation("com.raxdenstudios:commons-network:x.y.z")
    
    // Pagination
    implementation("com.raxdenstudios:commons-pagination:x.y.z")
    implementation("com.raxdenstudios:commons-pagination-co:x.y.z")
    
    // Permissions
    implementation("com.raxdenstudios:commons-permissions:x.y.z")
    
    // Preferences
    implementation("com.raxdenstudios:commons-preferences:x.y.z")
    
    // Date & Time
    implementation("com.raxdenstudios:commons-threeten:x.y.z")
    testImplementation("com.raxdenstudios:commons-threeten-test:x.y.z")
    
    // Testing
    testImplementation("com.raxdenstudios:commons-android-test:x.y.z")
}
```

> **Note:** Replace `x.y.z` with the latest version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.raxdenstudios/commons-android/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.raxdenstudios/commons-android)

## 💡 Usage Examples

### Preferences

```kotlin
// Create preferences instance
val preferences = AdvancedPreferences.Default(context)

// Save data with extension
preferences.edit {
    put("username", "john_doe")
    put("age", 25)
    put("settings", Settings(theme = "dark"))
}

// Read data
val username = preferences.get("username", "")
val age = preferences.get("age", 0)
val settings = preferences.get("settings", Settings())
```

### Permissions

```kotlin
class MainActivity : ComponentActivity() {
    private val permissionsManager = PermissionsManagerImpl()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsManager.attach(this)
        
        // Request permissions
        permissionsManager.requestPermission(
            callbacks = PermissionsManager.Callbacks(
                onGranted = { permission -> 
                    // Permission granted
                },
                onDenied = { permission -> 
                    // Permission denied
                }
            ),
            Permission.Camera,
            Permission.AccessFineLocation
        )
    }
}
```

### Pagination

```kotlin
// Create pagination instance
val pagination = CoPagination<Item>(
    config = Pagination.Config.default,
    coroutineScope = viewModelScope
)

// Request first page
pagination.requestFirstPage(
    pageRequest = { page, pageSize -> 
        repository.getItems(page.value, pageSize.value)
    },
    pageResponse = { result ->
        when (result) {
            is PageResult.Loading -> showLoading()
            is PageResult.Content -> showItems(result.items)
            is PageResult.Error -> showError(result.error)
            is PageResult.NoMoreResults -> hideLoadMore()
        }
    }
)
```

### Network Interceptors

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthTokenInterceptor { getAuthToken() })
    .addInterceptor(RetryInterceptor(maxRetries = 3))
    .addInterceptor(NetworkMonitorInterceptor(networkMonitor))
    .addInterceptor(HeadersInterceptor.userAgent("MyApp/1.0"))
    .build()
```

## 🧪 Testing

All modules include comprehensive unit tests. Run tests with:

```bash
./gradlew test
```

For coverage reports:

```bash
./gradlew jacocoTestReport
```

## 📖 Documentation

Detailed documentation for each module is available in their respective directories:

- [Core](libraries/core/README.md)
- [Android](libraries/android/README.md)
- [Coroutines](libraries/coroutines/README.md)
- [Network](libraries/network/README.md)
- [Pagination](libraries/pagination/README.md)
- [Permissions](libraries/permissions/README.md)
- [Preferences](libraries/preferences/README.md)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

```
Copyright 2015 Ángel Gómez

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
