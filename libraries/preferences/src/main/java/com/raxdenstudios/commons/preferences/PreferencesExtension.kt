@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.preferences

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use SharedPreferences.edit instead.",
    level = DeprecationLevel.WARNING,
)
fun AdvancedPreferences.edit(
    commit: Boolean = false,
    action: AdvancedPreferences.Editor.() -> Unit,
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}
