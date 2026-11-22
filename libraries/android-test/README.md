# Android Test Commons

Helpers for instrumented and Espresso tests: a base test runner, an OkHttp idling resource, a RESTMock rule and view matchers.

## Installation

```kotlin
dependencies {
    androidTestImplementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    androidTestImplementation("com.raxdenstudios:commons-android-test")
}
```

## Features

### AppTestRunner

`AppTestRunner` extends `AndroidJUnitRunner`. Subclass it to swap the `Application` used by the tests, then set your subclass as `testInstrumentationRunner`.

### OkHttp3IdlingResource

Espresso waits for OkHttp calls to finish before it checks the UI:

```kotlin
val idlingResource = OkHttp3IdlingResource.create("api", okHttpClient)
Espresso.registerIdlingResources(idlingResource)
```

Creating it replaces the dispatcher's idle callback, so use one resource per client.

### RESTMockServerRule

Starts the RESTMock server before each test and resets it after. It serves HTTPS unless `useHttps = false` is passed.

```kotlin
@get:Rule
val restMockServerRule = RESTMockServerRule()
```

Do not start RESTMock yourself in the test, because the rule already does.

### Matchers

Matchers for `ViewMatchers`, `TextInputLayoutMatchers`, `RecyclerViewMatchers`, `SwipeRefreshLayoutMatchers`, `ProgressBarMatchers` and `TabLayoutMatchers` under `com.raxdenstudios.commons.android.test.matchers`.

## Deprecated (removed in next major)

Marked `@Deprecated` (warning level) and scheduled for removal in the next major version. Nothing was removed and no signature changed.

- `TestPage`, `RESTMockServerRule` and the matcher objects `ViewMatchers`, `TextInputLayoutMatchers`, `RecyclerViewMatchers`, `SwipeRefreshLayoutMatchers`, `ProgressBarMatchers`, `TabLayoutMatchers`: consumers keep their own copies.

`AppTestRunner` is still in use (referenced from Gradle `testInstrumentationRunner`) and is not deprecated.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
