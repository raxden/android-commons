# Pagination Module

Base pagination framework for Android with support for RecyclerView and Jetpack Compose.

## 📦 Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-pagination")
    
    // For coroutines support
    implementation("com.raxdenstudios:commons-pagination-co")
}
```

## 🚀 Usage

### With Coroutines (Recommended)

```kotlin
class MyViewModel : ViewModel() {
    private val pagination = CoPagination<Item>(
        config = Pagination.Config.default,
        coroutineScope = viewModelScope
    )
    
    fun loadFirstPage() {
        pagination.requestFirstPage(
            pageRequest = { page, pageSize -> 
                repository.getItems(page.value, pageSize.value)
            },
            pageResponse = { result ->
                when (result) {
                    is PageResult.Loading -> _state.value = State.Loading
                    is PageResult.Content -> _state.value = State.Success(result.items)
                    is PageResult.Error -> _state.value = State.Error(result.error)
                    is PageResult.NoResults -> _state.value = State.Empty
                    is PageResult.NoMoreResults -> _hasMore.value = false
                }
            }
        )
    }
    
    fun loadNextPage() {
        pagination.requestNextPage(
            pageRequest = { page, pageSize -> 
                repository.getItems(page.value, pageSize.value)
            },
            pageResponse = { result -> handlePageResult(result) }
        )
    }
}
```

### With RecyclerView

```kotlin
recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val pageIndex = layoutManager.toPageIndex()
        
        pagination.requestPage(
            pageIndex = pageIndex,
            pageRequest = { page, pageSize -> loadPage(page, pageSize) },
            pageResponse = { result -> handleResult(result) }
        )
    }
})
```

### With Jetpack Compose LazyColumn

```kotlin
val lazyListState = rememberLazyListState()

LazyColumn(state = lazyListState) {
    items(items) { item ->
        ItemCard(item)
    }
}

LaunchedEffect(lazyListState) {
    snapshotFlow { lazyListState.toPageIndex() }
        .collect { pageIndex ->
            pagination.requestPage(
                pageIndex = pageIndex,
                pageRequest = { page, pageSize -> loadPage(page, pageSize) },
                pageResponse = { result -> handleResult(result) }
            )
        }
}
```

## 📋 Configuration

### Custom Configuration

```kotlin
val pagination = CoPagination<Item>(
    config = Pagination.Config(
        initialPage = Page(0),
        pageSize = PageSize(20),
        prefetchDistance = 5
    ),
    coroutineScope = viewModelScope
)
```

### Default Configuration

```kotlin
Pagination.Config.default // Page(0), PageSize(10), prefetchDistance = 3
```

## 🎯 Page Results

### PageResult Types

```kotlin
sealed class PageResult<T> {
    object Loading : PageResult<Nothing>()
    data class Content<T>(val items: List<T>) : PageResult<T>()
    data class Error(val error: Throwable) : PageResult<Nothing>()
    object NoResults : PageResult<Nothing>()
    object NoMoreResults : PageResult<Nothing>()
}
```

### Handling Results

```kotlin
when (result) {
    is PageResult.Loading -> {
        // Show loading indicator
    }
    is PageResult.Content -> {
        // Add items to list
        items.addAll(result.items)
    }
    is PageResult.Error -> {
        // Show error message
        showError(result.error)
    }
    is PageResult.NoResults -> {
        // Show empty state
    }
    is PageResult.NoMoreResults -> {
        // Hide load more button
    }
}
```

## ✨ Features

- ✅ Automatic page management
- ✅ Prefetch distance configuration
- ✅ Support for RecyclerView and Compose
- ✅ Coroutine-based async loading
- ✅ Request cancellation support
- ✅ Thread-safe implementation
- ✅ Comprehensive error handling

## 📖 API Reference

### CoPagination

#### Methods

- `requestFirstPage(pageRequest, pageResponse)` - Load first page
- `requestPreviousPage(pageRequest, pageResponse)` - Load previous page
- `requestNextPage(pageRequest, pageResponse)` - Load next page
- `requestPage(pageIndex, pageRequest, pageResponse)` - Load specific page
- `cancelCurrentRequest()` - Cancel ongoing request

### Extension Functions

- `LinearLayoutManager.toPageIndex(): PageIndex` - Get current page index
- `LazyListState.toPageIndex(): PageIndex` - Get current page index
- `LazyGridState.toPageIndex(): PageIndex` - Get current page index

## 💡 Advanced Usage

### Cancel Requests

```kotlin
// Cancel current request when user scrolls rapidly
pagination.cancelCurrentRequest()
```

### Custom Page Request

```kotlin
suspend fun loadPage(page: Page, pageSize: PageSize): PageList<Item> {
    val response = api.getItems(
        page = page.value,
        limit = pageSize.value
    )
    return PageList(
        items = response.items,
        page = page
    )
}
```

## 🧪 Testing

```bash
./gradlew :libraries:pagination:testDebugUnitTest
./gradlew :libraries:pagination-co:testDebugUnitTest
```
