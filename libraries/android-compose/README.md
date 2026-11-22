# Android Compose Commons

A library with Jetpack Compose helpers: UI resource models, `Modifier` and `PaddingValues` extensions, a multi preview annotation and coroutine helpers for callbacks.

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

### Modifier extensions

In `com.raxdenstudios.commons.android.compose.ext`:

- `Modifier.aspectRatio11()`: fixes the aspect ratio to 1:1.

### PaddingValues extensions

In `com.raxdenstudios.commons.android.compose.ext`:

- `PaddingValues.copy(start, top, end, bottom)`: `@Composable` copy that overrides only the given sides.
- `PaddingValues + PaddingValues`: sums two paddings side by side.

### DevicePreviews

`com.raxdenstudios.commons.android.compose.preview.DevicePreviews` is a multi preview annotation that renders a composable in light and dark phone previews.

## Removed in 3.0.0

- `runAsync` and both `consumeAsync` overloads: use `rememberCoroutineScope` and `launch` directly.

## Removed in 4.0.0

- `Modifier.mirror()`: use `Modifier.scale(scaleX = -1f, scaleY = 1f)`.
- `ComposableLifecycle`: use `LifecycleEventEffect` or `DisposableEffect` with `LocalLifecycleOwner` from `lifecycle-runtime-compose`.
- `LockScreenOrientation` and `ScreenOrientation`: set `Activity.requestedOrientation` in a `DisposableEffect`.
- `Modifier.aspectRatio43()`, `aspectRatio169()`, `aspectRatio23()`: use `Modifier.aspectRatio(ratio)`.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
