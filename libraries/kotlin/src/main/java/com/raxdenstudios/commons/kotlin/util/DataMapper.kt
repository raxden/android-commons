package com.raxdenstudios.commons.kotlin.util

abstract class DataMapper<E, T> {

  abstract fun transform(source: E): T

  fun transform(source: List<E>): List<T> {
    val destination = ArrayList<T>()
    var out: T?
    for (`in` in source) {
      out = transform(`in`)
      if (out != null) {
        destination.add(out)
      }
    }
    return destination
  }
}
