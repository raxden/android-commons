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
- `areNotificationsEnabled()`: whether the app can post notifications right now (permission granted on API 33+ and notifications not turned off), from `NotificationManagerCompat.areNotificationsEnabled()`. When `false`, request `POST_NOTIFICATIONS` (API 33+, e.g. with `commons-permissions`) or open `IntentType.NotificationSettings`.
- `getPackageInfo()`: the app's `PackageInfo`, using the typed flags overload on API 33+.

### MonotonicClock

`com.raxdenstudios.commons.android.util.MonotonicClock` measures intervals (timers, play time, debounce) with `SystemClock.elapsedRealtime()`, which never jumps when the user or the network changes the wall clock. Inject `MonotonicClock.System` and pass a lambda in tests.

```kotlin
class PlayTimer(private val clock: MonotonicClock = MonotonicClock.System) {
    private val startedAt = clock.elapsedRealtimeMillis()
    fun elapsedMillis(): Long = clock.elapsedRealtimeMillis() - startedAt
}

// In a test, driven by virtual time:
val timer = PlayTimer(clock = MonotonicClock { testScheduler.currentTime })
```

For the wall-clock time and today's date, inject `java.time.Clock` (`Clock.systemDefaultZone()`) and read `LocalDate.now(clock)`; tests use `MutableClock` or `virtualClock()` from `commons-coroutines-test`.

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

## License

See the [LICENSE](../../LICENSE.txt) file for details.
