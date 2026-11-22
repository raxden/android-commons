# Android Compose Commons

A library with Jetpack Compose helpers: UI resource models, lifecycle observation, `Modifier` and `PaddingValues` extensions, a multi preview annotation and coroutine helpers for callbacks.

## Installation

```kotlin
dependencies {
    implementation("com.raxdenstudios:commons-android-compose:{version}")
}
```

The module exposes `commons-core`, the Compose BOM, `material3`, `ui`, `ui-tooling-preview` and `lifecycle-runtime-compose` as `api` dependencies.

> **Migration note:** this module used to depend on `commons-android`. It now depends on `commons-core` only.
> If your code uses APIs from `commons-android` through this module (for example `ActivityHolder` or the
> helpers in `com.raxdenstudios.commons.android.ext`), declare it explicitly:
>
> ```kotlin
> dependencies {
>     implementation("com.raxdenstudios:commons-android-compose:{version}")
>     implementation("com.raxdenstudios:commons-android:{version}")
> }
> ```

## Features

### StringResource

`com.raxdenstudios.commons.android.compose.model.StringResource` describes display text without resolving it until composition.

- `StringResource.ResourceId(resourceId, params)`: a string resource with optional format arguments.
- `StringResource.PlainText(value)`: runtime text.
- `StringResource.Empty`: no text.
- `toText()`: `@Composable` that resolves it to a `String`.

```kotlin
val title: StringResource = StringResource.ResourceId(R.string.greeting, listOf("Ana"))
Text(text = title.toText())
```

### ImageResource

`com.raxdenstudios.commons.android.compose.model.ImageResource` wraps an image source.

- `ImageResource.Drawable(image)`: a `@DrawableRes` id.
- `ImageResource.Vector(image)`: an `ImageVector`.
- `toPainter()`: `@Composable` that returns the `Painter`.

### ComposableLifecycle

`com.raxdenstudios.commons.android.compose.ComposableLifecycle(observer, lifeCycleOwner)` registers a `LifecycleEventObserver` on the `LocalLifecycleOwner` (or the given owner) and removes it when it leaves the composition.

```kotlin
ComposableLifecycle(
    observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) onResume()
    },
)
```

### Modifier extensions

In `com.raxdenstudios.commons.android.compose.ext`:

- `Modifier.mirror()`: flips the content horizontally.
- `Modifier.aspectRatio11()`, `aspectRatio43()`, `aspectRatio169()`, `aspectRatio23()`: fixed aspect ratios (1:1, 4:3, 16:9, 2:3).

### PaddingValues extensions

In `com.raxdenstudios.commons.android.compose.ext`:

- `PaddingValues.copy(start, top, end, bottom)`: `@Composable` copy that overrides only the given sides.
- `PaddingValues + PaddingValues`: sums two paddings side by side.

### LockScreenOrientation

`com.raxdenstudios.commons.android.compose.LockScreenOrientation(orientation)` locks the host activity to a `ScreenOrientation` while the composable is composed, and restores the previous orientation when it leaves. `ScreenOrientation` has `Unspecified`, `Portrait` and `Landscape`.

```kotlin
@Composable
fun VideoScreen() {
    LockScreenOrientation(orientation = ScreenOrientation.Landscape)
    // ...
}
```

If no activity is found in the context (for example in a preview), nothing changes.

### DevicePreviews

`com.raxdenstudios.commons.android.compose.preview.DevicePreviews` is a multi preview annotation that renders a composable in light and dark phone previews.

### Async helpers

In `com.raxdenstudios.commons.android`:

- `runAsync { }`: returns a `() -> Unit` that launches the block in a `rememberCoroutineScope`.
- `consumeAsync { value -> }` and `consumeAsync { a, b -> }`: return `(T) -> Unit` / `(T, R) -> Unit` callbacks that launch the block in that scope.

```kotlin
Button(onClick = runAsync { viewModel.refresh() })
```

## License

See the [LICENSE](../../LICENSE.txt) file for details.
