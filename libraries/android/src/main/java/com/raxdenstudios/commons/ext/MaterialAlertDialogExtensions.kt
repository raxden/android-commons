package com.raxdenstudios.commons.ext

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

fun Context.showSimpleDialog(
    title: Int,
    message: Int,
    positiveButton: Int = android.R.string.ok
) {
    showSimpleDialog(getString(title), getString(message), getString(positiveButton))
}

fun Context.showSimpleDialog(
    title: String,
    message: String,
    positiveButton: String = getString(android.R.string.ok)
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButton) { dialog, _ -> dialog.dismiss() }
        .create()
        .show()
}

@Suppress("LongParameterList")
fun Context.showDialog(
    title: Int,
    message: Int,
    positiveButton: Int = android.R.string.ok,
    onPositiveClickButton: () -> Unit = {},
    negativeButton: Int = android.R.string.cancel,
    onNegativeClickButton: () -> Unit = {}
) {
    showDialog(
        getString(title),
        getString(message),
        getString(positiveButton),
        onPositiveClickButton,
        getString(negativeButton),
        onNegativeClickButton
    )
}

@Suppress("LongParameterList")
fun Context.showDialog(
    title: String,
    message: String,
    positiveButton: String = getString(android.R.string.ok),
    onPositiveClickButton: () -> Unit = {},
    negativeButton: String = getString(android.R.string.cancel),
    onNegativeClickButton: () -> Unit = {}
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
