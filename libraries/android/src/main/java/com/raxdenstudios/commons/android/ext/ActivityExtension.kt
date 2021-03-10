package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import androidx.viewbinding.ViewBinding
import com.raxdenstudios.commons.android.property.ActivityArgumentDelegate
import com.raxdenstudios.commons.android.property.ActivityViewBindingDelegate

inline fun <reified T : Parcelable> Activity.setResultOK(value: T) {
  setResult(
    Activity.RESULT_OK,
    Intent().apply { putExtra(T::class.java.simpleName, value) }
  )
}

inline fun <reified T : Any> Activity.argument() = ActivityArgumentDelegate<T>()

inline fun <reified T : ViewBinding> Activity.viewBinding() =
  ActivityViewBindingDelegate(T::class.java)
