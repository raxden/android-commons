package com.raxdenstudios.commons.android.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

fun AppCompatActivity.setupToolbar(
  toolbar: Toolbar,
  titleEnabled: Boolean = false
): Toolbar = toolbar.also {
  setSupportActionBar(toolbar)
  supportActionBar?.setDisplayShowTitleEnabled(titleEnabled)
  toolbar.setNavigationOnClickListener { onBackPressed() }
}
