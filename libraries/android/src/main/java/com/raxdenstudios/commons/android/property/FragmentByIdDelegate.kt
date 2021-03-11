package com.raxdenstudios.commons.android.property

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentByIdDelegate<T : Fragment>(
  @IdRes private val fragmentId: Int
) : ReadOnlyProperty<Fragment, T> {

  private var fragment: T? = null

  override fun getValue(
    thisRef: Fragment,
    property: KProperty<*>
  ): T = fragment ?: findFragmentById(thisRef, property).also { fragment = it }

  private fun findFragmentById(
    thisRef: Fragment,
    property: KProperty<*>
  ): T = thisRef.childFragmentManager.findFragmentById(fragmentId) as T
}
