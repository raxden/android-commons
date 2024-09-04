@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.android.ext

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.raxdenstudios.commons.android.property.ViewBindingDelegate

inline fun <reified T : ViewBinding> ViewGroup.viewBinding() = ViewBindingDelegate(T::class.java)
