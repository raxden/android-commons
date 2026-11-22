package com.raxdenstudios.commons.android.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use setSupportActionBar and OnBackPressedDispatcher directly.",
    level = DeprecationLevel.WARNING,
)
fun AppCompatActivity.setupToolbar(
    toolbar: Toolbar,
    titleEnabled: Boolean = false,
): Toolbar = toolbar.also {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(titleEnabled)
    toolbar.setNavigationOnClickListener { onBackPressed() }
}
