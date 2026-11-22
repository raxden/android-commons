# Android Test Commons

Helpers for instrumented and Espresso tests: a base test runner, and an OkHttp idling resource.

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

## Removed in 6.0.0

- `TestPage`: keep your own page object base class.
- `RESTMockServerRule`: start `RESTMockServer` in your own rule.
- `ViewMatchers`, `TextInputLayoutMatchers`, `RecyclerViewMatchers`, `SwipeRefreshLayoutMatchers`, `ProgressBarMatchers` and `TabLayoutMatchers`: keep your own copies.
- The transitive `swiperefreshlayout`, RESTMock and MockWebServer dependencies: declare them in your own test modules.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
