@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.android.ext

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use MaterialAlertDialogBuilder directly or a Jetpack Compose dialog.",
    level = DeprecationLevel.WARNING,
)
fun Context.showSimpleDialog(
    title: Int,
    message: Int,
    positiveButton: Int = android.R.string.ok,
) {
    showSimpleDialog(getString(title), getString(message), getString(positiveButton))
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use MaterialAlertDialogBuilder directly or a Jetpack Compose dialog.",
    level = DeprecationLevel.WARNING,
)
fun Context.showSimpleDialog(
    title: String,
    message: String,
    positiveButton: String = getString(android.R.string.ok),
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButton) { dialog, _ -> dialog.dismiss() }
        .create()
        .show()
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use MaterialAlertDialogBuilder directly or a Jetpack Compose dialog.",
    level = DeprecationLevel.WARNING,
)
@Suppress("LongParameterList")
fun Context.showDialog(
    title: Int,
    message: Int,
    positiveButton: Int = android.R.string.ok,
    onPositiveClickButton: () -> Unit = {},
    negativeButton: Int = android.R.string.cancel,
    onNegativeClickButton: () -> Unit = {},
) {
    showDialog(
        getString(title),
        getString(message),
        getString(positiveButton),
        onPositiveClickButton,
        getString(negativeButton),
        onNegativeClickButton,
    )
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use MaterialAlertDialogBuilder directly or a Jetpack Compose dialog.",
    level = DeprecationLevel.WARNING,
)
@Suppress("LongParameterList")
fun Context.showDialog(
    title: String,
    message: String,
    positiveButton: String = getString(android.R.string.ok),
    onPositiveClickButton: () -> Unit = {},
    negativeButton: String = getString(android.R.string.cancel),
    onNegativeClickButton: () -> Unit = {},
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButton) { dialog, _ ->
            onPositiveClickButton()
            dialog.dismiss()
        }
        .setNegativeButton(negativeButton) { dialog, _ ->
            onNegativeClickButton()
            dialog.dismiss()
        }
        .create()
        .show()
}
