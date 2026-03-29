package com.raxdenstudios.commons.pagination.ext

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.recyclerview.widget.LinearLayoutManager
import com.raxdenstudios.commons.pagination.model.PageIndex

fun LinearLayoutManager.toPageIndex(): PageIndex {
    val lastVisiblePosition = findFirstVisibleItemPosition() + childCount - 1
    return PageIndex(lastVisiblePosition.coerceAtLeast(0))
}

fun LazyGridState.toPageIndex(): PageIndex {
    val lastVisibleIndex = firstVisibleItemIndex + layoutInfo.visibleItemsInfo.size - 1
    return PageIndex(lastVisibleIndex.coerceAtLeast(0))
}

fun LazyListState.toPageIndex(): PageIndex {
    val lastVisibleIndex = firstVisibleItemIndex + layoutInfo.visibleItemsInfo.size - 1
    return PageIndex(lastVisibleIndex.coerceAtLeast(0))
}
