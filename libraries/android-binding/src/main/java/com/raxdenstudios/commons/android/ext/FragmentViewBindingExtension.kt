package com.raxdenstudios.commons.android.ext

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.raxdenstudios.commons.android.property.FragmentViewBindingDelegate

inline fun <reified T : ViewBinding> Fragment.viewBinding() =
    FragmentViewBindingDelegate(T::class.java, this)
