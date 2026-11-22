# Network Module

Network utilities and OkHttp interceptors for modern Android networking.

## 📦 Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-network")
}
```

## 🚀 Usage

### OkHttp Interceptors

#### Auth Token Interceptor

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthTokenInterceptor { 
        getAuthToken() // Your token provider
    })
    .build()
```

#### Retry Interceptor

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(RetryInterceptor(
        maxRetries = 3,
        delayMillis = 1000L
    ))
    .build()

// With custom retry predicate
val retryInterceptor = RetryInterceptor(
    maxRetries = 3,
    shouldRetry = { response ->
        response.code == 503 || response.code == 429
    }
)
```

### Complete Setup Example

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthTokenInterceptor { tokenProvider.getToken() })
    .addInterceptor(RetryInterceptor(maxRetries = 3))
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.example.com")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

### HttpClientFactory

`HttpClientFactory` builds an `OkHttpClient`. `DefaultHttpClientFactory` takes the optional interceptors and timeouts:

```kotlin
val factory: HttpClientFactory = DefaultHttpClientFactory(
    retryInterceptor = RetryInterceptor(maxRetries = 3),
    httpLoggingInterceptor = HttpLoggingInterceptor(),
    interceptors = listOf(AuthTokenInterceptor { tokenProvider.getToken() }),
    readTimeout = 20.seconds,
)
val client: OkHttpClient = factory.create()
```

Each `create()` call builds a new client, so create it once and reuse it. Application interceptors run in this order: `interceptors` (list order), `retryInterceptor`, `httpLoggingInterceptor`, `cacheOfflineInterceptor`. Network interceptors run after that, in this order: `cacheNetworkInterceptor`, `cacheLoggerInterceptor`, then `networkInterceptors`. Null parameters are skipped.

## 📋 Available Interceptors

| Interceptor | Description |
|-------------|-------------|
| `AuthTokenInterceptor` | Adds Authorization header with token |
| `RetryInterceptor` | Retries failed requests with configurable strategy |

## ✨ Features

- ✅ Production-ready OkHttp interceptors
- ✅ Automatic retry with exponential backoff
- ✅ Token-based authentication
- ✅ Comprehensive unit tests

## 📖 API Reference

### AuthTokenInterceptor

```kotlin
AuthTokenInterceptor(tokenProvider: () -> String?)
```

### RetryInterceptor

```kotlin
RetryInterceptor(
    maxRetries: Int = 3,             // total attempts, at least one
    initialDelayMs: Long = 1000L,    // doubled after each failed attempt
    shouldRetry: (Response) -> Boolean = { it.code == 408 || it.code == 429 || it.code in 500..599 },
    retryNonIdempotent: Boolean = false, // by default only GET, HEAD, OPTIONS, PUT and DELETE are retried
)
```

Retrying stops when the call is cancelled. When no attempt is left, the last response is returned
as is, or the last `IOException` is rethrown.

## 💡 Best Practices

### Interceptor Order

```kotlin
OkHttpClient.Builder()
    .addInterceptor(AuthTokenInterceptor(...))       // 1. Add auth
    .addInterceptor(RetryInterceptor(...))           // 2. Retry if needed
    .build()
```

## 🧪 Testing

All interceptors include comprehensive unit tests.

```bash
./gradlew :libraries:network:testDebugUnitTest
```

## Removed in 3.0.0

- `HeadersInterceptor`: implement a project-owned OkHttp `Interceptor` that adds the headers.
- `ErrorResponseInterceptor` and the exception types `HttpException`, `UnauthorizedException`, `ForbiddenException`, `NotFoundException`, `ServerException`: implement a project-owned OkHttp `Interceptor` that maps HTTP status codes to your own exceptions.
- `NetworkMonitorInterceptor` and `NoNetworkException`: implement a project-owned OkHttp `Interceptor` that checks connectivity.
- `RequestTimingInterceptor`: implement a project-owned OkHttp `Interceptor` that measures the request duration.
