# Preferences Module

Advanced SharedPreferences wrapper with type-safe storage and Gson support.

## 📦 Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-preferences")
}
```

## 🚀 Usage

### Basic Setup

```kotlin
// Create preferences instance
val preferences = AdvancedPreferences.Default(context)

// Or create private preferences
val privatePrefs = AdvancedPreferences.Private(
    context = context,
    name = "my_preferences"
)
```

### Saving Data

```kotlin
preferences.edit {
    put("username", "john_doe")
    put("age", 25)
    put("isActive", true)
    put("score", 99.5f)
}

// With commit (synchronous)
preferences.edit(commit = true) {
    put("important_data", "value")
}
```

### Reading Data

```kotlin
val username = preferences.get("username", "default_name")
val age = preferences.get("age", 0)
val isActive = preferences.get("isActive", false)
val score = preferences.get("score", 0.0f)
```

### Supported Types

- **Primitives**: `Int`, `String`, `Boolean`, `Float`, `Long`
- **Collections**: `Set<String>`
- **JSON**: `JSONObject`, `JSONArray`
- **Custom Objects**: Any object serializable with Gson

### Custom Objects

```kotlin
data class User(val name: String, val email: String)

// Save
preferences.edit {
    put("user", User("John", "john@example.com"))
}

// Read
val user = preferences.get("user", User("", ""))
```

### Advanced Operations

```kotlin
// Check if key exists
if (preferences.contains("username")) {
    // Key exists
}

// Get all preferences
val allPrefs = preferences.getAll()

// Remove specific key
preferences.edit {
    remove("old_key")
}

// Clear all preferences
preferences.edit {
    clear()
}
```

## ✨ Features

- ✅ Type-safe storage with automatic type detection
- ✅ Gson integration for custom objects
- ✅ JSON support (JSONObject, JSONArray)
- ✅ Extension function for cleaner syntax
- ✅ Support for both apply() and commit()
- ✅ Safe null handling
- ✅ Exception handling for corrupted data

## 📖 API Reference

### AdvancedPreferences

#### Constructors

- `AdvancedPreferences.Default(context: Context, gson: Gson = Gson())`
- `AdvancedPreferences.Private(context: Context, name: String, gson: Gson = Gson())`

#### Methods

- `get(key: String, defaultValue: Any): Any` - Get value with default
- `contains(key: String): Boolean` - Check if key exists
- `getAll(): Map<String, Any?>` - Get all preferences
- `edit(): Editor` - Get editor instance

### Editor

- `put(key: String, value: Any): Editor` - Put value
- `remove(key: String): Editor` - Remove key
- `clear(): Editor` - Clear all
- `apply()` - Apply changes asynchronously
- `commit(): Boolean` - Commit changes synchronously

### Extension Functions

```kotlin
fun AdvancedPreferences.edit(
    commit: Boolean = false,
    action: AdvancedPreferences.Editor.() -> Unit
)
```

## 🧪 Testing

The module includes comprehensive unit tests covering all functionality.

```bash
./gradlew :libraries:preferences:testDebugUnitTest
```
