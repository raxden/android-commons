# Android Test Commons

Helpers for instrumented and Espresso tests: a base test runner.

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

## Removed in 7.0.0

- `OkHttp3IdlingResource`: keep your own copy.
- The transitive `okhttp3` dependency: declare it in your own test modules.

## Removed in 6.0.0

- `TestPage`: keep your own page object base class.
- `RESTMockServerRule`: start `RESTMockServer` in your own rule.
- `ViewMatchers`, `TextInputLayoutMatchers`, `RecyclerViewMatchers`, `SwipeRefreshLayoutMatchers`, `ProgressBarMatchers` and `TabLayoutMatchers`: keep your own copies.
- The transitive `swiperefreshlayout`, RESTMock and MockWebServer dependencies: declare them in your own test modules.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
