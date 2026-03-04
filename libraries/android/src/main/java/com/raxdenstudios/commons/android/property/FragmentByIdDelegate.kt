package com.raxdenstudios.commons.android.property

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentByIdDelegate<T : Fragment>(
    @field:IdRes private val fragmentId: Int
) : ReadOnlyProperty<Fragment, T> {

    private var fragment: T? = null

    override fun getValue(
        thisRef: Fragment,
        property: KProperty<*>
    ): T = fragment ?: findFragmentById(thisRef).also { fragment = it }

    @Suppress("UNCHECKED_CAST")
    private fun findFragmentById(
        thisRef: Fragment,
    ): T = thisRef.childFragmentManager.findFragmentById(fragmentId) as T
}
