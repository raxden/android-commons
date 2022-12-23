package com.raxdenstudios.commons.pagination.ext

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.recyclerview.widget.LinearLayoutManager
import com.raxdenstudios.commons.pagination.model.PageIndex

fun LinearLayoutManager.toPageIndex(): PageIndex =
    PageIndex(childCount + findFirstVisibleItemPosition())

fun LazyGridState.toPageIndex(): PageIndex =
    PageIndex(layoutInfo.visibleItemsInfo.size + firstVisibleItemIndex)

fun LazyListState.toPageIndex(): PageIndex =
    PageIndex(layoutInfo.visibleItemsInfo.size + firstVisibleItemIndex)
