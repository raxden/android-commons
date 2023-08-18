package com.raxdenstudios.commons.ext

fun Boolean?.orDefault(default: Boolean = false) = this ?: default
