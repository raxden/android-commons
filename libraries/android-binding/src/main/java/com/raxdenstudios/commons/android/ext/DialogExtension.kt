package com.raxdenstudios.commons.android.ext

import android.app.Dialog
import androidx.viewbinding.ViewBinding
import com.raxdenstudios.commons.android.property.DialogBindingDelegate

inline fun <reified T : ViewBinding> Dialog.viewBinding() =
    DialogBindingDelegate(T::class.java)
