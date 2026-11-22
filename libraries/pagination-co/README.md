# Pagination Coroutines Module

`CoPagination<T>` is the coroutines-based implementation of the base `Pagination<T>` from `commons-pagination`. It runs each page request in the `CoroutineScope` you provide, cancels the previous in-flight request when a new one starts, and reports progress as `PageResult` values.

## Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-pagination-co")
}
```

## Usage

`requestFirstPage` loads the first page. `requestPage` takes the index of the last visible item (`PageIndex`) and only fires a request when the end of the loaded list is within `Config.prefetchDistance`. Compute the index from your list state and wrap it in `PageIndex` (see the `commons-pagination` README).

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

Other operations: `requestPreviousPage`, `cancelCurrentRequest()` and `clear()` (resets the loaded history, e.g. on pull-to-refresh).
