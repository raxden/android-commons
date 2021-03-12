package com.raxdenstudios.commons.pagination.model

data class PageIndex(val value: Int) {

  companion object {
    val firstPage = PageIndex(0)
    fun init(visibleItemCount: Int, pastVisibleItems: Int) =
      PageIndex(visibleItemCount + pastVisibleItems)
  }
}
