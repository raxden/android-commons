package com.raxdenstudios.commons.android.property

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityViewBindingDelegate<T : ViewBinding>(
    bindingClass: Class<T>,
    private val activity: AppCompatActivity,
) : ReadOnlyProperty<Activity, T>, DefaultLifecycleObserver {

    private val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
    private var binding: T? = null

    init {
        activity.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        binding ?: createBinding(activity).also { binding = it }
    }

    override fun getValue(
        thisRef: Activity,
        property: KProperty<*>
    ): T = binding ?: createBinding(thisRef).also { binding = it }

    @Suppress("UNCHECKED_CAST")
    private fun createBinding(thisRef: Activity): T {
        val binding = inflateMethod.invoke(null, thisRef.layoutInflater) as T
        thisRef.setContentView(binding.root)
        return binding
    }
}
