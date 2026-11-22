# Pagination Module

Base pagination framework for Android. `Pagination<T>` (this module) keeps the loaded pages, the
state and the `PageResult` mapping. `CoPagination<T>` (module `commons-pagination-co`) is its
coroutines implementation and the class you instantiate.

## Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-pagination")

    // Coroutines implementation (CoPagination)
    implementation("com.raxdenstudios:commons-pagination-co")
}
```

## Usage

`CoPagination` runs each page request in the `CoroutineScope` you provide and reports progress through
`pageResponse` as `PageResult` values. `requestFirstPage` and `requestPage` are ignored while a request is
loading; only `requestPreviousPage` cancels the in-flight request before starting its own.

```kotlin
class ItemsViewModel(
    private val repository: ItemsRepository,
) : ViewModel() {

    private val pagination = CoPagination<Item>(
        config = Pagination.Config.default,
        coroutineScope = viewModelScope,
    )

    fun onFirstLoad() {
        pagination.requestFirstPage(
            pageRequest = ::loadPage,
            pageResponse = ::onPageResult,
        )
    }

    fun onScrolled(lastVisibleIndex: Int) {
        pagination.requestPage(
            pageIndex = PageIndex(lastVisibleIndex),
            pageRequest = ::loadPage,
            pageResponse = ::onPageResult,
        )
    }

    fun onRefresh() {
        pagination.cancelCurrentRequest()
        pagination.clear()
        onFirstLoad()
    }

    private suspend fun loadPage(page: Page, pageSize: PageSize): PageList<Item> =
        PageList(items = repository.getItems(page.value, pageSize.value), page = page)

    private fun onPageResult(result: PageResult<Item>) {
        when (result) {
            PageResult.Loading -> showLoading()
            is PageResult.Content -> showItems(result.items) // all items loaded so far
            is PageResult.Error -> showError(result.throwable)
            PageResult.NoResults -> showEmpty()
            PageResult.NoMoreResults -> Unit
        }
    }
}
```

### Operations

| Method | Description |
|---|---|
| `requestFirstPage(pageRequest, pageResponse)` | Loads the first page (same as `requestPage(PageIndex.first, ...)`). It is a no-op once pages are loaded, unless `clear()` was called first. |
| `requestPage(pageIndex, pageRequest, pageResponse)` | Takes the index of the last visible item. Only fires a request when the end of the loaded list is within `Config.prefetchDistance`, and returns early while another request is loading. |
| `requestPreviousPage(pageRequest, pageResponse)` | Loads the page before the current one, cancelling the in-flight request. Does nothing on the initial page. |
| `cancelCurrentRequest()` | Cancels the in-flight request and leaves the loading state so the next request is not ignored. |
| `clear()` | Resets the loaded history, the current page and the state (for example on pull-to-refresh). It does not cancel an in-flight request: call `cancelCurrentRequest()` first. |
| `getCurrentPage()` / `getItemsByPage(page)` | Reads the current page and the items loaded for a page. |

There is no `requestNextPage`: call `requestPage` with the index of the last visible item and the
pagination decides when the next page is needed.

### Feeding `requestPage` from a list

`requestPage` needs the index of the last visible item. Compute it from your list state and call
`requestPage` on every scroll change.

```kotlin
// Compose LazyColumn
val lazyListState = rememberLazyListState()

LaunchedEffect(lazyListState) {
    snapshotFlow {
        (lazyListState.firstVisibleItemIndex + lazyListState.layoutInfo.visibleItemsInfo.size - 1)
            .coerceAtLeast(0)
    }.collect { lastVisibleIndex -> viewModel.onScrolled(lastVisibleIndex) }
}
```

```kotlin
// RecyclerView
recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val lastVisibleIndex = (layoutManager.findFirstVisibleItemPosition() + layoutManager.childCount - 1)
            .coerceAtLeast(0)
        viewModel.onScrolled(lastVisibleIndex)
    }
})
```

## Configuration

```kotlin
val pagination = CoPagination<Item>(
    config = Pagination.Config(
        initialPage = Page(0),
        pageSize = PageSize(20),
        prefetchDistance = 5,
    ),
    coroutineScope = viewModelScope,
)
```

`Pagination.Config.default` is `Page(0)`, `PageSize(10)` and `prefetchDistance = 2`. `PageSize` must be
greater than 0. An optional `logger` lambda receives the prefetch decisions.

## Page results

```kotlin
sealed class PageResult<out T> {
    data object Loading : PageResult<Nothing>()
    data class Content<T>(val items: List<T>) : PageResult<T>()
    data class Error(val throwable: Throwable) : PageResult<Nothing>()
    data object NoResults : PageResult<Nothing>()
    data object NoMoreResults : PageResult<Nothing>()
}
```

- `Loading`: a request started.
- `Content`: every item loaded so far, ordered by page (not only the last page).
- `Error`: the request failed. The state becomes not-empty when items were already loaded, otherwise empty, so the next `requestPage` can retry.
- `NoResults`: the first page returned no items.
- `NoMoreResults`: the last loaded page had fewer items than `PageSize`; further `requestPage` calls only
  repeat this result.

## Page request

`pageRequest` receives the `Page` and `PageSize` to load and returns a `PageList`:

```kotlin
suspend fun loadPage(page: Page, pageSize: PageSize): PageList<Item> {
    val response = api.getItems(page = page.value, limit = pageSize.value)
    return PageList(items = response.items, page = page)
}
```

## Removed in 7.0.0

- `LinearLayoutManager.toPageIndex()`, `LazyListState.toPageIndex()` and `LazyGridState.toPageIndex()`: compute the last visible item index and wrap it in `PageIndex` directly, as in the examples above.

## Testing

```bash
./gradlew :libraries:pagination:testDebugUnitTest
./gradlew :libraries:pagination-co:testDebugUnitTest
```
