package com.raxdenstudios.commons.android.ext

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
