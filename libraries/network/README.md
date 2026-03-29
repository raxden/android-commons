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

#### Network Monitor Interceptor

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(NetworkMonitorInterceptor(networkMonitor))
    .build()

// Throws NoNetworkException if network is unavailable
```

#### Headers Interceptor

```kotlin
// Add custom headers
val headersInterceptor = HeadersInterceptor(
    mapOf(
        "X-API-Key" to "your-api-key",
        "X-Client-Version" to "1.0.0"
    )
)

// Or use factory methods
val userAgentInterceptor = HeadersInterceptor.userAgent("MyApp/1.0")
val apiVersionInterceptor = HeadersInterceptor.apiVersion("v1")

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(headersInterceptor)
    .addInterceptor(userAgentInterceptor)
    .build()
```

#### Request Timing Interceptor

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(RequestTimingInterceptor { url, durationMs ->
        Log.d("Network", "Request to $url took ${durationMs}ms")
        analytics.trackRequestDuration(url, durationMs)
    })
    .build()
```

#### Error Response Interceptor

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(ErrorResponseInterceptor())
    .build()

// Throws typed exceptions based on HTTP status codes:
// - UnauthorizedException (401)
// - ForbiddenException (403)
// - NotFoundException (404)
// - ServerErrorException (500+)
// - ClientErrorException (400-499)
```

### Complete Setup Example

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(NetworkMonitorInterceptor(networkMonitor))
    .addInterceptor(AuthTokenInterceptor { tokenProvider.getToken() })
    .addInterceptor(HeadersInterceptor.userAgent("MyApp/1.0"))
    .addInterceptor(RequestTimingInterceptor { url, duration ->
        analytics.trackRequest(url, duration)
    })
    .addInterceptor(RetryInterceptor(maxRetries = 3))
    .addInterceptor(ErrorResponseInterceptor())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.example.com")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

## 📋 Available Interceptors

| Interceptor | Description |
|-------------|-------------|
| `AuthTokenInterceptor` | Adds Authorization header with token |
| `RetryInterceptor` | Retries failed requests with configurable strategy |
| `NetworkMonitorInterceptor` | Checks network availability before requests |
| `HeadersInterceptor` | Adds custom headers to all requests |
| `RequestTimingInterceptor` | Measures and reports request duration |
| `ErrorResponseInterceptor` | Throws typed exceptions for HTTP errors |

## ✨ Features

- ✅ Production-ready OkHttp interceptors
- ✅ Automatic retry with exponential backoff
- ✅ Network availability checking
- ✅ Request timing and analytics
- ✅ Typed HTTP error exceptions
- ✅ Token-based authentication
- ✅ Custom headers management
- ✅ Comprehensive unit tests

## 📖 API Reference

### AuthTokenInterceptor

```kotlin
AuthTokenInterceptor(tokenProvider: () -> String?)
```

### RetryInterceptor

```kotlin
RetryInterceptor(
    maxRetries: Int = 3,
    delayMillis: Long = 1000L,
    shouldRetry: (Response) -> Boolean = { !it.isSuccessful }
)
```

### NetworkMonitorInterceptor

```kotlin
NetworkMonitorInterceptor(networkMonitor: NetworkMonitor)
```

### HeadersInterceptor

```kotlin
HeadersInterceptor(headers: Map<String, String>)

// Factory methods
HeadersInterceptor.userAgent(userAgent: String)
HeadersInterceptor.apiVersion(version: String)
```

### RequestTimingInterceptor

```kotlin
RequestTimingInterceptor(
    onRequestCompleted: (url: String, durationMs: Long) -> Unit
)
```

### ErrorResponseInterceptor

```kotlin
ErrorResponseInterceptor()

// Throws:
// - UnauthorizedException
// - ForbiddenException
// - NotFoundException
// - ServerErrorException
// - ClientErrorException
```

## 💡 Best Practices

### Interceptor Order

```kotlin
OkHttpClient.Builder()
    .addInterceptor(NetworkMonitorInterceptor(...))  // 1. Check network first
    .addInterceptor(AuthTokenInterceptor(...))       // 2. Add auth
    .addInterceptor(HeadersInterceptor(...))         // 3. Add headers
    .addInterceptor(RequestTimingInterceptor(...))   // 4. Measure timing
    .addInterceptor(RetryInterceptor(...))           // 5. Retry if needed
    .addInterceptor(ErrorResponseInterceptor())      // 6. Handle errors last
    .build()
```

### Error Handling

```kotlin
try {
    val response = api.getData()
} catch (e: UnauthorizedException) {
    // Refresh token and retry
} catch (e: ServerErrorException) {
    // Show server error message
} catch (e: NoNetworkException) {
    // Show no internet message
}
```

## 🧪 Testing

All interceptors include comprehensive unit tests.

```bash
./gradlew :libraries:network:testDebugUnitTest
```
