# Android Commons

Android helpers for context, intents, audio, ViewModel, SavedStateHandle and WorkManager. It depends on `commons-core` and `commons-coroutines`.

## Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-android")
}
```

## Features

### Intents

`com.raxdenstudios.commons.android.intent` builds the intents for common system screens.

- `IntentType`: `AppSettings`, `PlayStore`, `ShareText(subject, text)`, `NotificationSettings`, `ExactAlarmSettings`, `FullScreenIntentSettings`.
- `IntentFactory.build(type)`: returns the `Intent`; the caller starts it.

```kotlin
val intentFactory: IntentFactory = IntentFactoryImpl(applicationContext)
startActivity(intentFactory.build(IntentType.ShareText(subject = "Check this", text = "Try the app")))
```

`ExactAlarmSettings` exists only on API 31+ and `FullScreenIntentSettings` only on API 34+. Check the matching `Context` helper before offering them.

### Context

In `com.raxdenstudios.commons.android.ext`:

- `isExactAlarmAllowed()`: always `true` below API 31, otherwise `AlarmManager.canScheduleExactAlarms()`.
- `isFullScreenIntentAllowed()`: always `true` below API 34, otherwise `NotificationManager.canUseFullScreenIntent()`.
- `getPackageInfo()`: the app's `PackageInfo`, using the typed flags overload on API 33+.

### Audio

`com.raxdenstudios.commons.android.audio.HeadphonesConnectionObserver` emits `true` when a headphone-like output connects and `false` when one disconnects. Only changes are emitted.

```kotlin
HeadphonesConnectionObserverImpl(applicationContext).observeConnectionChanges()
    .collect { connected -> updateAudioRoute(connected) }
```

### ViewModel and WorkManager

- `ViewModel.safeLaunch { }`: runs work on `viewModelScope`, logging uncaught errors.
- `WorkManager.enqueueUniqueOneTime<W>(policy, uniqueName, configure)`: enqueues unique one-time work. The default policy is `ExistingWorkPolicy.KEEP`.

## Removed in 6.0.0

- `Event` and the `LiveData` extensions `observeEvent` / `observe` / `update`: use `StateFlow` / `SharedFlow` and `collectAsStateWithLifecycle`.
- `launchAndCollect` (`StateFlowExtension`): use `repeatOnLifecycle(Lifecycle.State.STARTED)`.
- `ListDiffAdapter`, `FragmentBottomNavigationDelegate`, `FragmentViewPager2Delegate`, `FragmentDialog2`, `ActivityProvider`: keep your own copies or use Compose.
- Property delegates `ActivityArgumentDelegate`, `ActivityContentViewDelegate`, `FragmentActivityByIdDelegate`, `FragmentArgumentDelegate`, `FragmentByIdDelegate`, `SharedPreferencesDelegate` and their factories (`Activity.contentView/argument/fragmentById`, `Fragment.argument/fragmentById`, `SharedPreferences.boolean/long/string/getStringNotNull`): read the intent extras or fragment arguments directly, or use `SavedStateHandle`.
- `ViewPagerExtension` and `ViewPager2Extension`: use the ViewPager2 APIs directly.
- `AppBarLayoutExtension` (including `AppBarStateChangeListener` and `AppBarScrollProgressListener`), `TextViewExtension`, `RecyclerViewExtension`, `NestedScrollViewExtension`, `SwipeRefreshLayoutExtension`, `StringExtension`, `BitmapExtension`, `DrawableExtensions`, `DialogExtension`, `MaterialAlertDialogExtensions`, `NavigationBarExtensions`: use the framework or Material APIs directly.
- `Int.toDp()` / `Int.toPx()`: use Compose `Dp` or `TypedValue.applyDimension`.
- `KeyboardExtension`: use `WindowInsetsControllerCompat.show(ime())` / `hide(ime())`.
- `ViewExtension`: `doItVisible()`, `doItGone()`, `doItInvisible()` (use `isVisible` / `isGone` / `isInvisible = true` from `androidx.core.view`), `onFocusGained`, `onFocusLost` (use `View.setOnFocusChangeListener`), `closeKeyboardWhenTouchScreen`, `clicks()`.
- `ActivityExtension`: `setStatusBarSafeArea`, `setVirtualNavigationBarSafeArea` (use `WindowInsets`), `setResultOKWithData`, `setResultOKWithDataAndFinish` (use `setResult` with an explicit `Intent`).
- `Context.createTemporalImageFile()` (use `File.createTempFile`), `AppCompatActivity.setupToolbar`, `DialogFragment.showForResult` (use the Fragment Result API), `DialogFragment.setFullscreen`.
- The `material` and `swiperefreshlayout` dependencies are no longer exposed through `commons-android` (only `appcompat` is): declare them in your own modules.

## Removed in 7.0.0

- `ActivityHolder.resumedActivity()` and `ComponentActivity.isAtLeastCreated/isAtLeastStarted/isAtLeastResumed`: check `activity?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.RESUMED)` directly.
- `ViewExtension`: `setPaddingTop`, `doItSelectable`, `doItUnselectable`, `doItClickable`, `doItNonClickable`, `visibleGone`, `isVisible()`, `isGone()`, `isInvisible()`, `getColor`, `getDrawable`, `inflateView`, `inflateViewIfEditMode`, `startFadeInAnimation`, `startFadeOutAnimation`, `setSafeOnClickListener`, `useStyledAttributes`: use the `androidx.core.view` and framework APIs directly.
- `FragmentExtension` (`Fragment.inflateView`, `Fragment.close`, `Fragment.loadFragment`, `Fragment.loadDialogFragment`) and `FragmentActivityExtension` (`FragmentActivity.loadFragment`, `closeFragment`, `loadDialogFragment`): use `FragmentManager.commit { }` directly.
- `IntentExtensions`: `intentFor`, `Intent.startActivity`, `startActivityAndFinishCurrent`, `startActivityAndFinishAll`: use `ContextCompat.startActivity` and `Intent` flags directly.
- `ActivityExtension`: `loadParcelable`, `setResultOK`, `setResultOKAndFinish` (use `setResult` and `finish`), `setFullScreen` (use `WindowInsetsControllerCompat`).
- `Application.initCompatVector`: call `AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)` directly.
- `Context.isNetworkConnected()`: use `Network.isNetworkConnected(context)`.
- `SavedStateHandle.getOrElse`, `getOrDefault` and `getOrNull`: use `SavedStateHandle.get` with Kotlin's `runCatching` / elvis operator. `getOrThrow` is kept.
- `Context.firstInstallTimeMillis()`: read `packageManager.getPackageInfo(...).firstInstallTime` directly.
- `ViewModel.launch(onError) { }`: use `viewModelScope.launch(onError = ...)` from `commons-coroutines`.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
