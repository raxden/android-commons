package com.raxdenstudios.commons

import androidx.activity.ComponentActivity
import java.lang.ref.WeakReference

class ActivityHolder2 {

    private var activityReference: WeakReference<ComponentActivity>? = null
    var activity: ComponentActivity?
        get() = activityReference?.get()
        private set(value) {
            activityReference = if (value == null) null
            else WeakReference(value)
        }

    fun attach(activity: ComponentActivity) {
        this.activity = activity
    }

    fun detach(activity: ComponentActivity) {
        if (this.activity == activity) this.activity = null
    }
}
