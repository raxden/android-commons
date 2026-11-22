package com.raxdenstudios.commons.pagination.ext

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.recyclerview.widget.LinearLayoutManager
import com.raxdenstudios.commons.pagination.model.PageIndex

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Compute the last visible item index and wrap it in PageIndex directly.",
    level = DeprecationLevel.WARNING,
)
fun LinearLayoutManager.toPageIndex(): PageIndex {
    val lastVisiblePosition = findFirstVisibleItemPosition() + childCount - 1
    return PageIndex(lastVisiblePosition.coerceAtLeast(0))
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Compute the last visible item index and wrap it in PageIndex directly.",
    level = DeprecationLevel.WARNING,
)
fun LazyGridState.toPageIndex(): PageIndex {
    val lastVisibleIndex = firstVisibleItemIndex + layoutInfo.visibleItemsInfo.size - 1
    return PageIndex(lastVisibleIndex.coerceAtLeast(0))
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Compute the last visible item index and wrap it in PageIndex directly.",
    level = DeprecationLevel.WARNING,
)
fun LazyListState.toPageIndex(): PageIndex {
    val lastVisibleIndex = firstVisibleItemIndex + layoutInfo.visibleItemsInfo.size - 1
    return PageIndex(lastVisibleIndex.coerceAtLeast(0))
}
