@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.android.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.raxdenstudios.commons.android.property.ActivityViewBindingDelegate

inline fun <reified T : ViewBinding> AppCompatActivity.viewBinding() =
    ActivityViewBindingDelegate(T::class.java, this)
