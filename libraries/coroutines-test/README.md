# Coroutines Test Module

Testing utilities for coroutine-based code: main dispatcher rule, test doubles for the
`commons-coroutines` providers and controllable `java.time.Clock` implementations.

## 📦 Installation

```kotlin
dependencies {
    testImplementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    testImplementation("com.raxdenstudios:commons-coroutines-test")
}
```

It exposes `kotlinx-coroutines-test`, Turbine, `androidx.arch.core:core-testing` and `commons-coroutines` as API
dependencies.

## 🚀 Usage

### MainDispatcherRule

```kotlin
@get:Rule
val mainDispatcherRule = MainDispatcherRule()
```

### TestDispatcherProvider

Returns the same dispatcher for `main`, `io` and `default` (an `UnconfinedTestDispatcher` by default).

```kotlin
val dispatchers = TestDispatcherProvider()
val viewModel = MyViewModel(dispatchers = dispatchers)

// Share the scheduler with runTest
@Test
fun test() = runTest {
    val dispatchers = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
}
```

### TestCoroutineScopeProvider

Returns the same scope for `main`, `io` and `default`. Typically used with `backgroundScope`, which is cancelled
when the test ends:

```kotlin
@Test
fun test() = runTest {
    val monitor = AppSessionMonitor(scopes = TestCoroutineScopeProvider(backgroundScope))
}
```

### MutableClock

A `java.time.Clock` moved by hand. Views created with `withZone` share the same instant.

```kotlin
val clock = MutableClock(initial = Instant.parse("2026-01-01T00:00:00Z"))

clock.advanceBy(5.minutes)               // kotlin.time.Duration
clock.advanceBy(Duration.ofSeconds(30))  // java.time.Duration
clock.set(Instant.parse("2026-01-02T00:00:00Z"))
```

### virtualClock

A `java.time.Clock` driven by the virtual time of `runTest`: its instant is `origin + testScheduler.currentTime`.

```kotlin
@Test
fun test() = runTest {
    val clock = virtualClock(origin = Instant.parse("2026-01-01T22:00:00Z"))

    advanceTimeBy(1.hours)
    // clock.instant() == 2026-01-01T23:00:00Z
}
```

## 📖 API Reference

```kotlin
class MainDispatcherRule(val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()) : TestWatcher

class TestDispatcherProvider(
    val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
) : DispatcherProvider

class TestCoroutineScopeProvider(val scope: CoroutineScope) : CoroutineScopeProvider

// com.raxdenstudios.commons.coroutines.test.time
class MutableClock(
    initial: Instant = Instant.EPOCH,
    zone: ZoneId = ZoneOffset.UTC
) : Clock {
    fun advanceBy(duration: java.time.Duration)
    fun advanceBy(duration: kotlin.time.Duration)
    fun set(instant: Instant)
}

fun TestScope.virtualClock(
    origin: Instant = Instant.EPOCH,
    zone: ZoneId = ZoneOffset.UTC
): Clock
```
