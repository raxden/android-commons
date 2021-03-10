package com.raxdenstudios.commons.kotlin.ext

fun <T> List<T>.replaceItem(newValue: T, block: (T) -> Boolean): List<T> {
  return map { list ->
    if (block(list)) newValue else list
  }
}
