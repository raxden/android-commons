package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.ContextCompat

inline fun <reified T : Activity> Context.intentFor(
    params: Parcelable? = null,
) = Intent(this, T::class.java).apply {
    if (params != null) putExtra("params", params)
}

/**
 * Starts this intent from [context]. When [context] is not backed by an Activity, a copy of the intent
 * with [Intent.FLAG_ACTIVITY_NEW_TASK] is started, as Android requires; this intent is never modified.
 */
fun Intent.startActivity(
    context: Context,
    options: Bundle? = null,
) {
    val intent = if (context.findActivity() == null) Intent(this).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else this
    ContextCompat.startActivity(context, intent, options)
}

/**
 * Starts this intent from the [Activity] behind [context], then finishes that activity. The Activity is found
 * by unwrapping [android.content.ContextWrapper] layers.
 *
 * @throws IllegalArgumentException when [context] is not backed by an Activity, for example an application context.
 */
fun Intent.startActivityAndFinishCurrent(
    context: Context,
    options: Bundle? = null,
) {
    val activity = requireNotNull(context.findActivity()) { "context must be backed by an Activity" }
    ContextCompat.startActivity(activity, this, options)
    activity.finish()
}

fun Intent.startActivityAndFinishAll(
    context: Context,
    options: Bundle? = null,
) {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    ContextCompat.startActivity(context, this, options)
}
