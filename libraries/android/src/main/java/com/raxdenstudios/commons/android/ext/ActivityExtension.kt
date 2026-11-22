package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.os.Parcelable
import android.view.View

fun Activity.setFullScreen() {
    window.decorView.apply {
        systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}

fun <T : Parcelable> Activity.loadParcelable(
    key: String,
    defaultValue: T? = null,
    operation: (value: T) -> Unit,
) {
    val value = intent?.extras?.getParcelable(key) ?: defaultValue
    if (value != null) operation(value)
}

fun Activity.setResultOK() {
    setResult(Activity.RESULT_OK)
}

fun Activity.setResultOKAndFinish() {
    setResultOK()
    finish()
}
