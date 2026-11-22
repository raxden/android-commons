# Preferences Module

`SharedPreferences` extensions that store and read kotlinx.serialization JSON.

## 📦 Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-preferences")
}
```

## 🚀 Usage

### kotlinx.serialization JSON on SharedPreferences

Extensions in `com.raxdenstudios.commons.preferences.ext` for plain `SharedPreferences`:

```kotlin
@Serializable
data class Settings(val theme: String, val notifications: Boolean)

// Read: null when the key is missing or the stored JSON cannot be decoded
val settings: Settings? = sharedPreferences.getJson<Settings>("settings")

// Write
sharedPreferences.edit()
    .putJson("settings", Settings(theme = "dark", notifications = true))
    .putOrRemove("last_hint", lastHint) // removes the key when lastHint is null
    .apply()
```

Pass a custom `Json` instance (e.g. `Json { ignoreUnknownKeys = true }`) through the `json` parameter.
Callers need the `org.jetbrains.kotlin.plugin.serialization` plugin to generate serializers for their
`@Serializable` classes.

## 📖 API Reference

### SharedPreferences Extensions

```kotlin
inline fun <reified T> SharedPreferences.getJson(key: String, json: Json = Json): T?

inline fun <reified T> SharedPreferences.Editor.putJson(
    key: String,
    value: T,
    json: Json = Json
): SharedPreferences.Editor

fun SharedPreferences.Editor.putOrRemove(key: String, value: String?): SharedPreferences.Editor
```

## 🧪 Testing

```bash
./gradlew :libraries:preferences:testDebugUnitTest
```

## Removed in 7.0.0

- `AdvancedPreferences` (`Default`, `Private`, `Editor`) and `AdvancedPreferences.edit { }`: use `SharedPreferences` with the `getJson` / `putJson` / `putOrRemove` extensions, or DataStore.
