# Android Commons

[![Continuous Delivery](https://github.com/raxden/android-commons/workflows/Continuous%20Delivery/badge.svg)](https://github.com/raxden/android-commons/actions/workflows/ci_publish.yml)
[![codecov](https://codecov.io/gh/raxden/android-commons/branch/master/graph/badge.svg?token=E55S5DHJ9B)](https://codecov.io/gh/raxden/android-commons)
[![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-bom.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-bom)
[![API](https://img.shields.io/badge/API-28%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=28)
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

| Module | Description | Documentation | Latest Version |
|--------|-------------|---------------|----------------|
| **Bill of Materials** |
| `commons-bom` | BOM for version management | - | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-bom.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-bom) |
| **Core & Android** |
| `commons-core` | Core utilities and extensions | [📖 Docs](libraries/core/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-core.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-core) |
| `commons-android` | Android-specific utilities and extensions | [📖 Docs](libraries/android/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-android.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-android) |
| `commons-android-compose` | Jetpack Compose utilities | [📖 Docs](libraries/android-compose/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-android-compose.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-android-compose) |
| `commons-android-test` | Android testing utilities | [📖 Docs](libraries/android-test/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-android-test.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-android-test) |
| **Storage (Room)** |
| `commons-android-room` | Room `BaseDao` helpers | [📖 Docs](libraries/android-room/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-android-room.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-android-room) |
| **Async & Concurrency** |
| `commons-coroutines` | Kotlin Coroutines extensions | [📖 Docs](libraries/coroutines/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-coroutines.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-coroutines) |
| `commons-coroutines-test` | Coroutines testing utilities | [📖 Docs](libraries/coroutines-test/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-coroutines-test.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-coroutines-test) |
| **Networking** |
| `commons-network` | Network utilities and interceptors | [📖 Docs](libraries/network/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-network.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-network) |
| **Pagination** |
| `commons-pagination` | Base pagination framework | [📖 Docs](libraries/pagination/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-pagination.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-pagination) |
| `commons-pagination-co` | Coroutines-based pagination | - | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-pagination-co.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-pagination-co) |
| **Permissions** |
| `commons-permissions` | Runtime permissions management | [📖 Docs](libraries/permissions/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-permissions.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-permissions) |
| **Storage** |
| `commons-preferences` | Advanced SharedPreferences wrapper | [📖 Docs](libraries/preferences/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-preferences.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-preferences) |
| **Date & Time** |
| `commons-time` | `java.time` date/time extensions (no init needed) | [📖 Docs](libraries/time/README.md) | [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-time.svg?label=version)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-time) |

## 🎯 Scope

Commons holds app-agnostic utilities with no DI framework, no Firebase/Play SDKs and no opinionated UI/design system. Framework pieces (DI, use case contracts, `Failure`, screen framework, theme) belong in android-platform.

### Removed modules

`commons-threetenabp`, `commons-threetenabp-test` (use `commons-time`) and `commons-android-binding` were removed; last published versions remain on Maven Central.

## 🚀 Getting Started

### Requirements

- **Min SDK**: 28 (Android 9)
- **Target SDK**: 36 (Android 16)
- **Compile SDK**: 36
- **Kotlin**: 2.3+
- **Java**: 17

### Installation

#### Using BOM (Bill of Materials) 🎯

The **Bill of Materials (BOM)** manages all library versions automatically, ensuring compatibility between modules.

**Latest BOM version:** [![Maven Central](https://img.shields.io/maven-central/v/com.raxdenstudios/commons-bom.svg)](https://central.sonatype.com/artifact/com.raxdenstudios/commons-bom)

```kotlin
dependencies {
    // Import the BOM with the latest version (check badge above)
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    
    // Add modules without specifying versions
    implementation("com.raxdenstudios:commons-core")
    implementation("com.raxdenstudios:commons-android")
    implementation("com.raxdenstudios:commons-coroutines")
    implementation("com.raxdenstudios:commons-network")
    implementation("com.raxdenstudios:commons-pagination")
    implementation("com.raxdenstudios:commons-pagination-co")
    implementation("com.raxdenstudios:commons-permissions")
    implementation("com.raxdenstudios:commons-preferences")
    implementation("com.raxdenstudios:commons-time")
    
    // Testing modules
    testImplementation("com.raxdenstudios:commons-coroutines-test")
    testImplementation("com.raxdenstudios:commons-android-test")
}
```

**Benefits of using BOM:**
- ✅ No need to specify versions for each module
- ✅ Guaranteed compatibility between all modules
- ✅ Easier updates - just change the BOM version
- ✅ Prevents version conflicts

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

- [Android](libraries/android/README.md)
- [Android Compose](libraries/android-compose/README.md)
- [Android Test](libraries/android-test/README.md)
- [Android Room](libraries/android-room/README.md)
- [Coroutines](libraries/coroutines/README.md)
- [Coroutines Test](libraries/coroutines-test/README.md)
- [Core](libraries/core/README.md)
- [Network](libraries/network/README.md)
- [Pagination](libraries/pagination/README.md)
- [Permissions](libraries/permissions/README.md)
- [Preferences](libraries/preferences/README.md)
- [Time](libraries/time/README.md)

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
