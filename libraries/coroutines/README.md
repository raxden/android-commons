# Coroutines Module

Kotlin Coroutines utilities and extensions for safer and more convenient coroutine handling.

## 📦 Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-coroutines")
    
    // For testing
    testImplementation("com.raxdenstudios:commons-coroutines-test")
}
```

## 🚀 Usage

### Safe Launch

Launch coroutines with automatic exception handling:

```kotlin
class MyViewModel : ViewModel() {
    
    init {
        viewModelScope.safeLaunch {
            // Your coroutine code
            // Exceptions are automatically caught and logged
            val data = repository.getData()
            _state.value = State.Success(data)
        }
    }
}
```

### Launch with Error Callback

Handle errors with a custom callback:

```kotlin
viewModelScope.launch(
    onError = { throwable ->
        _state.value = State.Error(throwable.message)
        analytics.logError(throwable)
    }
) {
    val data = repository.getData()
    _state.value = State.Success(data)
}
```

### Custom Exception Handler

Use your own exception handler:

```kotlin
val customHandler = CoroutineExceptionHandler { _, throwable ->
    Log.e("MyApp", "Coroutine error", throwable)
    crashlytics.recordException(throwable)
}

viewModelScope.safeLaunch(exceptionHandler = customHandler) {
    // Your code
}
```

### Global Logger Configuration

Set up a global logger for all coroutine exceptions:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        defaultCoroutineLogger = object : CoroutineLogger {
            override fun logError(tag: String, message: String?, throwable: Throwable) {
                Log.e(tag, message, throwable)
                Crashlytics.getInstance().recordException(throwable)
            }
        }
    }
}
```

## 🎯 Answer Extensions

Work with `Answer<T, E>` type in coroutines:

### coRunCatching

Wrap suspend functions in Answer:

```kotlin
val result: Answer<User, Throwable> = coRunCatching {
    api.getUser(userId)
}

when (result) {
    is Answer.Success -> showUser(result.value)
    is Answer.Failure -> showError(result.value)
}
```

### Transform Success Values

```kotlin
val userAnswer: Answer<User, Error> = getUserAnswer()

val nameAnswer: Answer<String, Error> = userAnswer.coThen { user ->
    user.name
}
```

### Transform Failure Values

```kotlin
val result: Answer<Data, ApiError> = getDataAnswer()

val mapped: Answer<Data, UiError> = result.coThenFailure { apiError ->
    UiError.from(apiError)
}
```

### FlatMap Operations

```kotlin
val userAnswer: Answer<User, Error> = getUserAnswer()

val profileAnswer: Answer<Profile, Error> = userAnswer.coFlatMap { user ->
    getProfileAnswer(user.id)
}
```

### Side Effects

```kotlin
userAnswer
    .onCoSuccess { user ->
        analytics.trackUserLoaded(user.id)
    }
    .onCoFailure { error ->
        analytics.trackError(error)
    }
```

## 🌊 Flow Extensions

Transform Flow emissions with Answer:

### Map Flow Values

```kotlin
val userFlow: Flow<Answer<User, Error>> = getUserFlow()

val nameFlow: Flow<Answer<String, Error>> = userFlow.then { user ->
    user.name
}
```

### Map Flow Errors

```kotlin
val dataFlow: Flow<Answer<Data, ApiError>> = getDataFlow()

val uiFlow: Flow<Answer<Data, UiError>> = dataFlow.thenFailure { apiError ->
    UiError.from(apiError)
}
```

### Convert Flow to Answer

```kotlin
val itemsFlow: Flow<List<Item>> = database.observeItems()

val answerFlow: Flow<Answer<List<Item>, Throwable>> = itemsFlow.toAnswer()

answerFlow.collect { answer ->
    when (answer) {
        is Answer.Success -> updateUI(answer.value)
        is Answer.Failure -> showError(answer.value)
    }
}
```

## 🔧 Dispatcher Provider

Abstraction for coroutine dispatchers (useful for testing):

```kotlin
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

// Implementation
class DefaultDispatcherProvider : DispatcherProvider {
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
    override val default = Dispatchers.Default
}

// Usage in ViewModel
class MyViewModel(
    private val dispatchers: DispatcherProvider
) : ViewModel() {
    
    fun loadData() {
        viewModelScope.launch(dispatchers.io) {
            val data = repository.getData()
            withContext(dispatchers.main) {
                _state.value = State.Success(data)
            }
        }
    }
}
```

## 🎭 Coroutine Scope Provider

Abstraction for coroutine scopes:

```kotlin
interface CoroutineScopeProvider {
    val main: CoroutineScope
    val io: CoroutineScope
    val default: CoroutineScope
}

// Usage
class MyRepository(
    private val scopes: CoroutineScopeProvider
) {
    fun loadData() {
        scopes.io.launch {
            // Background work
        }
    }
}
```

## ✨ Features

- ✅ Safe coroutine launching with automatic exception handling
- ✅ Custom error callbacks
- ✅ Global logger configuration
- ✅ Answer type integration for functional error handling
- ✅ Flow extensions for Answer transformations
- ✅ Dispatcher and Scope abstractions for testability
- ✅ Comprehensive unit tests
- ✅ Kotlin DSL for clean syntax

## 📖 API Reference

### Extension Functions

#### CoroutineScope Extensions

```kotlin
fun CoroutineScope.safeLaunch(
    exceptionHandler: CoroutineExceptionHandler = defaultExceptionHandler,
    block: suspend CoroutineScope.() -> Unit
): Job

fun CoroutineScope.launch(
    onError: (Throwable) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job
```

#### Answer Extensions

```kotlin
suspend fun <T, R> T.coRunCatching(
    function: suspend T.() -> R
): Answer<R, Throwable>

suspend fun <T, R, E> Answer<T, E>.coThen(
    function: suspend (value: T) -> R
): Answer<R, E>

suspend fun <T, R, E> Answer<T, E>.coThenFailure(
    function: suspend (value: E) -> R
): Answer<T, R>

suspend fun <T, R, E> Answer<T, E>.coFlatMap(
    function: suspend (value: T) -> Answer<R, E>
): Answer<R, E>

suspend fun <T, R, E> Answer<T, E>.coFlatMapFailure(
    function: suspend (value: E) -> Answer<T, R>
): Answer<T, R>

suspend fun <T, E> Answer<T, E>.onCoSuccess(
    function: suspend (success: T) -> Unit
): Answer<T, E>

suspend fun <T, E> Answer<T, E>.onCoFailure(
    function: suspend (failure: E) -> Unit
): Answer<T, E>
```

#### Flow Extensions

```kotlin
fun <T, R, E> Flow<Answer<T, E>>.then(
    function: suspend (value: T) -> R
): Flow<Answer<R, E>>

fun <T, R, E> Flow<Answer<T, E>>.thenFailure(
    function: (value: E) -> R
): Flow<Answer<T, R>>

fun <T> Flow<T>.toAnswer(): Flow<Answer<T, Throwable>>
```

### Interfaces

#### CoroutineLogger

```kotlin
interface CoroutineLogger {
    fun logError(tag: String, message: String?, throwable: Throwable)
}

// Global instance
var defaultCoroutineLogger: CoroutineLogger?
```

#### DispatcherProvider

```kotlin
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}
```

#### CoroutineScopeProvider

```kotlin
interface CoroutineScopeProvider {
    val main: CoroutineScope
    val io: CoroutineScope
    val default: CoroutineScope
}
```

## 💡 Best Practices

### Always Use Safe Launch in Production

```kotlin
// ✅ Good - exceptions are handled
viewModelScope.safeLaunch {
    loadData()
}

// ❌ Bad - unhandled exceptions can crash the app
viewModelScope.launch {
    loadData()
}
```

### Provide Error Callbacks for User-Facing Errors

```kotlin
viewModelScope.launch(
    onError = { error ->
        _errorState.value = error.toUserMessage()
    }
) {
    val data = repository.getData()
    _state.value = data
}
```

### Use Dispatcher Abstraction for Testing

```kotlin
class MyViewModel(
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {
    // Easy to test with TestDispatcherProvider
}
```

### Combine Answer with Flow for Reactive Error Handling

```kotlin
repository.observeData()
    .toAnswer()
    .then { data -> data.processedValue }
    .collect { answer ->
        when (answer) {
            is Answer.Success -> updateUI(answer.value)
            is Answer.Failure -> showError(answer.value)
        }
    }
```

## 🧪 Testing

The module includes comprehensive unit tests for all functionality.

```bash
./gradlew :libraries:coroutines:testDebugUnitTest
```

### Test Coverage

- ✅ safeLaunch with and without exceptions
- ✅ launch with error callbacks
- ✅ Custom exception handlers
- ✅ All Answer extension functions
- ✅ Flow transformations
- ✅ Error propagation
