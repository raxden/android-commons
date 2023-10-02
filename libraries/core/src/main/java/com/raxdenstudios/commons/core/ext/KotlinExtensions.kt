package com.raxdenstudios.commons.core.ext

inline val <reified T> T?.exhaustive: T?
    get() = this
