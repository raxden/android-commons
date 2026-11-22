# Android Commons

Android helpers for activities, fragments, views, context, intents, audio and WorkManager. It depends on `commons-core` and `commons-coroutines`.

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

- `firstInstallTimeMillis()`: first install time in epoch millis, or `0` if it cannot be read.
- `isExactAlarmAllowed()`: always `true` below API 31, otherwise `AlarmManager.canScheduleExactAlarms()`.
- `isFullScreenIntentAllowed()`: always `true` below API 34, otherwise `NotificationManager.canUseFullScreenIntent()`.
- `getPackageInfo()`: the app's `PackageInfo`, using the typed flags overload on API 33+.

### ActivityHolder

`ActivityHolder.resumedActivity()` returns the attached activity only while it is resumed, or `null`.

### Audio

`com.raxdenstudios.commons.android.audio.HeadphonesConnectionObserver` emits `true` when a headphone-like output connects and `false` when one disconnects. Only changes are emitted.

```kotlin
HeadphonesConnectionObserverImpl(applicationContext).observeConnectionChanges()
    .collect { connected -> updateAudioRoute(connected) }
```

### ViewModel and WorkManager

- `ViewModel.safeLaunch { }` and `ViewModel.launch(onError) { }`: run work on `viewModelScope`, logging uncaught errors.
- `WorkManager.enqueueUniqueOneTime<W>(policy, uniqueName, configure)`: enqueues unique one-time work. The default policy is `ExistingWorkPolicy.KEEP`.

## Deprecated (removed in next major)

Marked `@Deprecated` (warning level) and scheduled for removal in the next major version. Nothing was removed and no signature changed.

- `Event` (`getContentIfNotHandled`, `peekContent`) and `observeEvent` / `observe` / `update` in `LiveDataExtension`: use `StateFlow`/`SharedFlow` and `collectAsStateWithLifecycle`.
- `launchAndCollect` (`StateFlowExtension`): use `repeatOnLifecycle(Lifecycle.State.STARTED)`.
- `ListDiffAdapter`, `FragmentBottomNavigationDelegate`, `FragmentViewPager2Delegate`, `FragmentDialog2`, `ActivityProvider`.
- Property delegates: `ActivityArgumentDelegate`, `ActivityContentViewDelegate`, `FragmentActivityByIdDelegate`, `FragmentArgumentDelegate`, `FragmentByIdDelegate`, `SharedPreferencesDelegate`, and the factories `Activity.contentView/argument/fragmentById`, `Fragment.argument/fragmentById`, `SharedPreferences.boolean/long/string/getStringNotNull`.
- `ViewPagerExtension` (use ViewPager2), `ViewPager2Extension`, `AppBarLayoutExtension` (including `AppBarStateChangeListener` and `AppBarScrollProgressListener`), `TextViewExtension`, `RecyclerViewExtension`, `NestedScrollViewExtension` (including `SHORT_DELAY`), `SwipeRefreshLayoutExtension`, `StringExtension`, `BitmapExtension`, `DrawableExtensions`, `DialogExtension`, `MaterialAlertDialogExtensions`, `NavigationBarExtensions`.
- `Int.toDp()` / `Int.toPx()`: use Compose `Dp` or `TypedValue.applyDimension`.
- `KeyboardExtension`: use `WindowInsetsControllerCompat.show(ime())` / `hide(ime())`.
- `ViewExtension`: `doItVisible()`, `doItGone()`, `doItInvisible()` (use `isVisible`/`isGone`/`isInvisible = true` from `androidx.core.view`), `onFocusGained`, `onFocusLost`, `closeKeyboardWhenTouchScreen`, `clicks()`.
- `ActivityExtension`: `setStatusBarSafeArea`, `setVirtualNavigationBarSafeArea`, `setResultOKWithData`, `setResultOKWithDataAndFinish`.
- `Context.createTemporalImageFile()`, `AppCompatActivity.setupToolbar`, `DialogFragment.showForResult`.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
