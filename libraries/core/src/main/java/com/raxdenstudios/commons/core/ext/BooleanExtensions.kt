package com.raxdenstudios.commons.core.ext

fun Boolean?.orDefault(default: Boolean = false) = this ?: default
