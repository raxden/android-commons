package com.raxdenstudios.commons.android.ext

import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

fun DialogFragment.setFullscreen() =
  dialog?.window?.setLayout(
    ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT
  )


