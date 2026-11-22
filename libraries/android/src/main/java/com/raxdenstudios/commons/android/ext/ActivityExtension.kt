package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.os.Parcelable
import android.view.View
import androidx.core.content.IntentCompat

/**
 * Enters fullscreen and hides the navigation bar by adding [View.SYSTEM_UI_FLAG_HIDE_NAVIGATION] and
 * [View.SYSTEM_UI_FLAG_FULLSCREEN] to the decor view's current system UI flags, which are kept.
 */
fun Activity.setFullScreen() {
    window.decorView.apply {
        systemUiVisibility = systemUiVisibility or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}

/**
 * Invokes [operation] with the Parcelable extra of type [T] stored under [key] in the activity's intent.
 * When the intent is null, the extra is missing, or it is not a [T], [defaultValue] is used instead.
 * When both are absent, [operation] is not called.
 */
inline fun <reified T : Parcelable> Activity.loadParcelable(
    key: String,
    defaultValue: T? = null,
    operation: (value: T) -> Unit,
) {
    val value = intent?.let { IntentCompat.getParcelableExtra(it, key, T::class.java) } ?: defaultValue
    if (value != null) operation(value)
}

fun Activity.setResultOK() {
    setResult(Activity.RESULT_OK)
}

fun Activity.setResultOKAndFinish() {
    setResultOK()
    finish()
}
