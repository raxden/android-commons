@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.android.ext

import android.content.SharedPreferences
import com.raxdenstudios.commons.android.property.SharedPreferencesDelegate
import kotlin.reflect.KProperty

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use DataStore or the preferences library of this repository.",
    level = DeprecationLevel.WARNING,
)
fun SharedPreferences.boolean(
    key: (KProperty<*>) -> String = KProperty<*>::name,
    defaultValue: Boolean = false,
    onAfterGetOperation: (value: Boolean) -> Unit = {},
    onAfterSetOperation: (value: Boolean) -> Unit = {},
) = SharedPreferencesDelegate(this, key, defaultValue, onAfterGetOperation, onAfterSetOperation)

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use DataStore or the preferences library of this repository.",
    level = DeprecationLevel.WARNING,
)
fun SharedPreferences.long(
    key: (KProperty<*>) -> String = KProperty<*>::name,
    defaultValue: Long = 0L,
    onAfterGetOperation: (value: Long) -> Unit = {},
    onAfterSetOperation: (value: Long) -> Unit = {},
) = SharedPreferencesDelegate(this, key, defaultValue, onAfterGetOperation, onAfterSetOperation)

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use DataStore or the preferences library of this repository.",
    level = DeprecationLevel.WARNING,
)
fun SharedPreferences.string(
    key: (KProperty<*>) -> String = KProperty<*>::name,
    defaultValue: String = "",
    onAfterGetOperation: (value: String) -> Unit = {},
    onAfterSetOperation: (value: String) -> Unit = {},
) = SharedPreferencesDelegate(this, key, defaultValue, onAfterGetOperation, onAfterSetOperation)

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use DataStore or the preferences library of this repository.",
    level = DeprecationLevel.WARNING,
)
fun SharedPreferences.getStringNotNull(
    key: String,
    defaultValue: String,
): String = getString(key, defaultValue) ?: defaultValue
