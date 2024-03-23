package com.raxdenstudios.commons.android.ext

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

fun Dialog.setTransparentBackground() {
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}
